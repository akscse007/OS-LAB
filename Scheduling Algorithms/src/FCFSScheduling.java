import java.util.Scanner;

/*public class Process {
    int pn;
    int pid;
    int arrivalTime;
    int burstTime;
    int finalExpectedTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;
}*/

public class FCFSScheduling {

    // Function to assign the final expected time to the process
    static void calculateFinalExpectedTime(Process[] processes, int n) {
        for (int i = 0; i < n; i++) {
            processes[i].finalExpectedTime = processes[i].arrivalTime + processes[i].burstTime;
        }
    }

    // Utility Function to swap two processes
    static void swapProcesses(Process[] processes, int i, int j) {
        Process temp = processes[i];
        processes[i] = processes[j];
        processes[j] = temp;
    }

    // Function to sort the processes by their process numbers
    static void sortByPn(Process[] processes, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].pn > processes[j + 1].pn) {
                    swapProcesses(processes, j, j + 1);
                }
            }
        }
    }

    // Utility Function to sort processes by arrival time
    static void sortProcess(Process[] processes, int n) {
        // Sort processes by arrival time
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                    swapProcesses(processes, j, j + 1);
                }
            }
        }
    }

    // Function to print gantt chart, table and average waiting and turn-around time
    static void printOutput(Process[] processes, int n, float att, float awt) {

        System.out.println("\nGantt Chart:");
        int currentTime = 0;
        if (processes[0].arrivalTime > 0) {
            System.out.printf("[0-IDLE-%d]", processes[0].arrivalTime);
            currentTime = processes[0].arrivalTime;
        }

        for (int i = 0; i < n; i++) {
            if (currentTime < processes[i].arrivalTime) {
                System.out.printf("[%d-IDLE-%d]", currentTime, processes[i].arrivalTime);
                currentTime = processes[i].arrivalTime;
            }
            System.out.printf("[%d-P%d-%d]", currentTime, processes[i].pn,
                    currentTime + processes[i].burstTime);
            currentTime += processes[i].burstTime;
        }
        System.out.println();

        // Process Table
        System.out.println("\nProcess Summary:");
        System.out.println("+-----+-------+------------+-----------+----------------+----------------+");
        System.out.println("| PID | Arrival | Burst Time | Completion | Turnaround Time | Waiting Time |");
        System.out.println("+-----+-------+------------+-----------+----------------+----------------+");

        for (int i = 0; i < n; i++) {
            System.out.printf("| P%-2d | %-6d | %-10d | %-9d | %-14d | %-12d |\n",
                    processes[i].pn,
                    processes[i].arrivalTime,
                    processes[i].burstTime,
                    processes[i].completionTime,
                    processes[i].turnaroundTime,
                    processes[i].waitingTime);
        }
        System.out.println("+-----+-------+------------+-----------+----------------+----------------+");

        // Averages
        System.out.printf("\nAverage Turnaround Time: %.2f\n", att);
        System.out.printf("Average Waiting Time: %.2f\n", awt);
    }

    // Function to implement the logic of fcfs scheduling algorithm
    static void fcfs(Process[] processes, int n) {
        int currentTime = 0;
        int totalCompletionTime = 0;
        int totalWaitingTime = 0;
        sortProcess(processes, n);

        for (int i = 0; i < n; i++) {
            if (currentTime < processes[i].arrivalTime) {
                currentTime = processes[i].arrivalTime;
            }

            processes[i].responseTime = currentTime - processes[i].arrivalTime;
            processes[i].completionTime = currentTime + processes[i].burstTime;
            processes[i].turnaroundTime = processes[i].completionTime - processes[i].arrivalTime;
            processes[i].waitingTime = processes[i].turnaroundTime - processes[i].burstTime;
            currentTime = processes[i].completionTime;
            totalCompletionTime += processes[i].completionTime;
            totalWaitingTime += processes[i].waitingTime;
        }

        float avgTurnaroundTime = (float) totalCompletionTime / n;
        float avgWaitingTime = (float) totalWaitingTime / n;
        printOutput(processes, n, avgTurnaroundTime, avgWaitingTime);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        System.out.println("Enter Process ID, Arrival Time, and Burst Time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            processes[i] = new Process();
            processes[i].pn = i + 1;
            System.out.print("PID: ");
            processes[i].pid = scanner.nextInt();
            System.out.print("Arrival Time: ");
            processes[i].arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            processes[i].burstTime = scanner.nextInt();
        }
        calculateFinalExpectedTime(processes, n);
        System.out.println("\n\nAnalyzing the processes in the First Come First Serve (or FCFS) Algorithm (Type - Non-preemptive): \n");
        fcfs(processes, n);
        scanner.close();
    }
}