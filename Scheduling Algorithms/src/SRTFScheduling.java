import java.util.Scanner;



public class SRTFScheduling {

    static void assignFinalExpectedTimeAndInitialRemainingBurstTime(Process[] processes, int n) {
        for (int i = 0; i < n; i++) {
            processes[i].finalExpectedTime = processes[i].arrivalTime + processes[i].burstTime;
            processes[i].remainingTime = processes[i].burstTime;
        }
    }

    static void swapProcesses(Process[] processes, int i, int j) {
        Process temp = processes[i];
        processes[i] = processes[j];
        processes[j] = temp;
    }

    static void sortByPn(Process[] processes, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].pn > processes[j + 1].pn) {
                    swapProcesses(processes, j, j + 1);
                }
            }
        }
    }

    static void sortProcess(Process[] processes, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].remainingTime > processes[j + 1].remainingTime) {
                    if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                        swapProcesses(processes, j, j + 1);
                    } else if (processes[j].arrivalTime == processes[j + 1].arrivalTime) {
                        if (processes[j].pid > processes[j + 1].pid) {
                            swapProcesses(processes, j, j + 1);
                        }
                    }
                } else if (processes[j].remainingTime == processes[j + 1].remainingTime) {
                    if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                        swapProcesses(processes, j, j + 1);
                    } else if (processes[j].arrivalTime == processes[j + 1].arrivalTime) {
                        if (processes[j].pid > processes[j + 1].pid) {
                            swapProcesses(processes, j, j + 1);
                        }
                    }
                } else if (processes[j].remainingTime < processes[j + 1].remainingTime) {
                    if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                        swapProcesses(processes, j, j + 1);
                    } else if (processes[j].arrivalTime == processes[j + 1].arrivalTime) {
                        if (processes[j].pid > processes[j + 1].pid) {
                            swapProcesses(processes, j, j + 1);
                        }
                    }
                }
            }
        }
    }

    static void checkFirstArrivalTime(Process[] processes, int n) {
        for (int j = 1; j < n; j++) {
            if (processes[j].arrivalTime < processes[0].arrivalTime) {
                swapProcesses(processes, j, 0);
            } else if (processes[j].arrivalTime == processes[0].arrivalTime) {
                if (processes[j].burstTime > processes[0].burstTime) {
                    swapProcesses(processes, j, 0);
                }
            }
        }
    }

    static int allProcessesSumBurstTime(Process[] processes, int n) {
        int burstTimeSum = processes[0].burstTime;
        for (int i = 1; i < n; i++) {
            burstTimeSum += processes[i].burstTime;
        }
        return burstTimeSum;
    }

    static void srtf(Process[] processes, int n) {
        // Sort processes by arrival time
        sortProcess(processes, n);

        int currentTime = 0;
        int completed = 0;
        float totalTurnaround = 0, totalWaiting = 0;

        System.out.println("\nExecution Timeline:");

        while (completed < n) {
            // Find process with shortest remaining time
            int shortestIdx = -1;
            int shortestTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].remainingTime > 0 &&
                        processes[i].arrivalTime <= currentTime &&
                        processes[i].remainingTime < shortestTime) {
                    shortestIdx = i;
                    shortestTime = processes[i].remainingTime;
                }
            }

            // Handle CPU idle time
            if (shortestIdx == -1) {
                if (currentTime == 0 || processes[completed].completionTime != currentTime) {
                    System.out.printf("[IDLE @%d] ", currentTime);
                }
                currentTime++;
                continue;
            }

            // Mark first CPU access for response time
            if (!processes[shortestIdx].cpuGivenOrNot) {
                processes[shortestIdx].cpuGivenOrNot = true;
                processes[shortestIdx].responseTime = currentTime - processes[shortestIdx].arrivalTime;
                System.out.printf("[P%d starts @%d] ", processes[shortestIdx].pn, currentTime);
            }

            // Execute for 1 time unit
            processes[shortestIdx].remainingTime--;
            currentTime++;

            // Check if process completed
            if (processes[shortestIdx].remainingTime == 0) {
                processes[shortestIdx].completionTime = currentTime;
                processes[shortestIdx].turnaroundTime = currentTime - processes[shortestIdx].arrivalTime;
                processes[shortestIdx].waitingTime = processes[shortestIdx].turnaroundTime - processes[shortestIdx].burstTime;

                totalTurnaround += processes[shortestIdx].turnaroundTime;
                totalWaiting += processes[shortestIdx].waitingTime;
                completed++;

                System.out.printf("[P%d completes @%d] ", processes[shortestIdx].pn, currentTime);
            }
        }

        // Calculate averages
        float avgTurnaround = totalTurnaround / n;
        float avgWaiting = totalWaiting / n;

        // Print process summary
        System.out.println("\n\nProcess Summary:");
        System.out.println("PID  Arrival  Burst  Completion  Turnaround  Waiting  Response");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%-2d   %-6d  %-5d  %-9d  %-9d  %-6d  %-7d\n",
                    processes[i].pn,
                    processes[i].arrivalTime,
                    processes[i].burstTime,
                    processes[i].completionTime,
                    processes[i].turnaroundTime,
                    processes[i].waitingTime,
                    processes[i].responseTime);
        }

        // Print averages
        System.out.printf("\nAverage Turnaround Time: %.2f\n", avgTurnaround);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaiting);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of processes: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        System.out.println("Enter Process ID, Arrival Time, and Burst Time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            processes[i] = new Process();
            processes[i].pn = i + 1;
            processes[i].cpuGivenOrNot = false;
            System.out.print("PID: ");
            processes[i].pid = scanner.nextInt();
            System.out.print("Arrival Time: ");
            processes[i].arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            processes[i].burstTime = scanner.nextInt();
        }

        assignFinalExpectedTimeAndInitialRemainingBurstTime(processes, n);
        System.out.println("\n\nAnalyzing the processes in the Shortest Remaining Time First (or SRTF) Algorithm (Type - Preemptive): \n");
        srtf(processes, n);

        scanner.close();
    }
}