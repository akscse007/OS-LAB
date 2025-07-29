import java.util.Scanner;

public class SJFScheduling {

    static void calculateFinalExpectedTime(Process[] processes, int n) {
        for (int i = 0; i < n; i++) {
            processes[i].finalExpectedTime = processes[i].arrivalTime + processes[i].burstTime;
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
                if (processes[j].burstTime > processes[j + 1].burstTime) {
                    if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                        swapProcesses(processes, j, j + 1);
                    } else if (processes[j].arrivalTime == processes[j + 1].arrivalTime) {
                        if (processes[j].pid > processes[j + 1].pid) {
                            swapProcesses(processes, j, j + 1);
                        }
                    }
                } else if (processes[j].burstTime == processes[j + 1].burstTime) {
                    if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                        swapProcesses(processes, j, j + 1);
                    } else if (processes[j].arrivalTime == processes[j + 1].arrivalTime) {
                        if (processes[j].pid > processes[j + 1].pid) {
                            swapProcesses(processes, j, j + 1);
                        }
                    }
                } else if (processes[j].burstTime < processes[j + 1].burstTime) {
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
    static void printOutput(Process[] processes, int n, float att, float awt) {
        // Simple Execution Timeline
        System.out.println("\nExecution Order:");
        int currentTime = 0;

        if (processes[0].arrivalTime > 0) {
            System.out.printf("IDLE (%d-%d) ", 0, processes[0].arrivalTime);
            currentTime = processes[0].arrivalTime;
        }

        for (int i = 0; i < n; i++) {
            if (currentTime < processes[i].arrivalTime) {
                System.out.printf("IDLE (%d-%d) ", currentTime, processes[i].arrivalTime);
                currentTime = processes[i].arrivalTime;
            }
            System.out.printf("P%d (%d-%d) ", processes[i].pn, currentTime,
                    currentTime + processes[i].burstTime);
            currentTime += processes[i].burstTime;
        }
        System.out.println("\n");

        // Simple Process Summary
        System.out.println("Process Details:");
        System.out.println("PID  Arrival  Burst  Completion  Turnaround  Waiting");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%-2d   %-5d   %-5d   %-9d   %-9d   %-6d\n",
                    processes[i].pn,
                    processes[i].arrivalTime,
                    processes[i].burstTime,
                    processes[i].completionTime,
                    processes[i].turnaroundTime,
                    processes[i].waitingTime);
        }

        // Averages
        System.out.printf("\nAverage Turnaround Time: %.2f\n", att);
        System.out.printf("Average Waiting Time: %.2f\n", awt);
    }
    static void sjf(Process[] processes, int n) {
        sortProcess(processes, n);
        checkFirstArrivalTime(processes, n);
        int currentTime = 0;
        int totalCompletionTime = 0;

        for (int i = 0; i < n; i++) {
            int qualifiedJobs = 0;
            for (int j = i; j < n; j++) {
                if (processes[j].arrivalTime <= currentTime) {
                    qualifiedJobs++;
                }
            }
            int[] qualifiedJobsList = new int[qualifiedJobs];
            int key = 0;
            for (int j = i; j < n; j++) {
                if (processes[j].arrivalTime <= currentTime) {
                    qualifiedJobsList[key++] = j;
                }
            }

            if (qualifiedJobs == 0) {
                currentTime = processes[i].arrivalTime;
                i--;
                continue;
            }

            int shortestBurstIndex = qualifiedJobsList[0];
            for (int j = 1; j < qualifiedJobs; j++) {
                int candidateIndex = qualifiedJobsList[j];
                if (processes[candidateIndex].burstTime < processes[shortestBurstIndex].burstTime ||
                        (processes[candidateIndex].burstTime == processes[shortestBurstIndex].burstTime &&
                                processes[candidateIndex].arrivalTime < processes[shortestBurstIndex].arrivalTime) ||
                        (processes[candidateIndex].burstTime == processes[shortestBurstIndex].burstTime &&
                                processes[candidateIndex].arrivalTime == processes[shortestBurstIndex].arrivalTime &&
                                processes[candidateIndex].pid < processes[shortestBurstIndex].pid)) {
                    shortestBurstIndex = candidateIndex;
                }
            }
            if (i != shortestBurstIndex) {
                swapProcesses(processes, i, shortestBurstIndex);
            }
            processes[i].responseTime = currentTime - processes[i].arrivalTime;
            processes[i].completionTime = currentTime + processes[i].burstTime;
            processes[i].turnaroundTime = processes[i].completionTime - processes[i].arrivalTime;
            processes[i].waitingTime = processes[i].turnaroundTime - processes[i].burstTime;
            currentTime = processes[i].completionTime;
            totalCompletionTime += processes[i].completionTime;
        }

        float avgTurnaroundTime = (float) totalCompletionTime / n;
        float avgWaitingTime = 0;
        for (int i = 0; i < n; i++) {
            avgWaitingTime += processes[i].waitingTime;
        }
        avgWaitingTime /= n;
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
        System.out.println("\n\nAnalyzing the processes in the Shortest Job First (or SJF) Algorithm (Type - Non-preemptive): \n");
        sjf(processes, n);
        scanner.close();
    }
}