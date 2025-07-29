import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;


public class RRScheduling {

    static void assignFinalExpectedTimeAndInitialRemainingBurstTime(Process[] processes, int n) {
        for (int i = 0; i < n; i++) {
            processes[i].finalExpectedTime = processes[i].arrivalTime + processes[i].burstTime;
            processes[i].remainingTime = processes[i].burstTime;
        }
    }

    static void rr(Process[] processes, int n, int timeQuantum) {
        int currentTime = 0;
        int completedProcesses = 0;
        Queue<Process> readyQueue = new LinkedList<>();

        // Initialize remaining time and add processes arriving at time 0
        for (Process p : processes) {
            p.remainingTime = p.burstTime;
            if (p.arrivalTime == 0) {
                readyQueue.add(p);
            }
        }

        System.out.println("Gantt Chart:");
        System.out.print("|");

        while (completedProcesses < n) {
            if (readyQueue.isEmpty()) {
                // Handle idle time
                System.out.print(" IDLE |");
                currentTime++;
                // Check for new arrivals
                for (Process p : processes) {
                    if (p.arrivalTime == currentTime) {
                        readyQueue.add(p);
                    }
                }
                continue;
            }

            Process current = readyQueue.poll();

            // Mark response time if this is the first time the process gets CPU
            if (current.responseTime == -1) {
                current.responseTime = currentTime - current.arrivalTime;
            }

            System.out.print(" P" + current.pn + " |");

            // Execute for the time quantum or remaining time, whichever is smaller
            int executionTime = Math.min(current.remainingTime, timeQuantum);
            currentTime += executionTime;
            current.remainingTime -= executionTime;

            // Check for new arrivals during this execution
            for (Process p : processes) {
                if (p.arrivalTime > currentTime - executionTime && p.arrivalTime <= currentTime && !readyQueue.contains(p)){
                    readyQueue.add(p);
                }
            }

            // If process isn't finished, add it back to the queue
            if (current.remainingTime > 0) {
                readyQueue.add(current);
            } else {
                // Process completed
                completedProcesses++;
                current.completionTime = currentTime;
                current.turnaroundTime = current.completionTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
            }
        }

        // Print timeline
        System.out.println("\nTimeline:");
        System.out.println("0" + " to " + currentTime);

        // Calculate and print statistics
        System.out.println("\nProcess Table:");
        System.out.println("---------------------------------------------------------------");
        System.out.println("| Process | Arrival | Burst | Finish | Turnaround | Waiting |");
        System.out.println("---------------------------------------------------------------");

        float avgTurnaround = 0, avgWaiting = 0;
        for (Process p : processes) {
            System.out.printf("| P%-6d | %7d | %5d | %6d | %10d | %7d |\n",
                    p.pn, p.arrivalTime, p.burstTime, p.completionTime,
                    p.turnaroundTime, p.waitingTime);
            avgTurnaround += p.turnaroundTime;
            avgWaiting += p.waitingTime;
        }

        System.out.println("---------------------------------------------------------------");
        System.out.printf("\nAverage Turnaround Time: %.2f\n", avgTurnaround / n);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaiting / n);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();
        System.out.print("Enter the time-quantum: ");
        int timeQuantum = scanner.nextInt();

        Process[] processes = new Process[n];

        System.out.println("Enter Process ID, Arrival Time, and Burst Time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            processes[i] = new Process();
            processes[i].pn = i + 1;
            processes[i].cpuGivenOrNot = false;
            processes[i].processStarted = false;
            System.out.print("PID: ");
            processes[i].pid = scanner.nextInt();
            System.out.print("Arrival Time: ");
            processes[i].arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            processes[i].burstTime = scanner.nextInt();
        }

        assignFinalExpectedTimeAndInitialRemainingBurstTime(processes, n);
        System.out.println("\n\nAnalyzing the processes in the Round Robin (or RR) Algorithm (Type - Preemptive): \n");
        rr(processes, n, timeQuantum);

        scanner.close();
    }
}