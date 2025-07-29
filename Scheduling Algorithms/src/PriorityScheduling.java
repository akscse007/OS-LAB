import java.util.Scanner;



public class PriorityScheduling {

    static void findMaxMinPriority(Process[] processes, int n, int[] maxMinPriority, boolean order) {
        if (order) {
            maxMinPriority[0] = Integer.MIN_VALUE;
            maxMinPriority[1] = Integer.MAX_VALUE;
        } else {
            maxMinPriority[0] = Integer.MAX_VALUE;
            maxMinPriority[1] = Integer.MIN_VALUE;
        }

        for (int i = 0; i < n; i++) {
            if (order) {
                if (processes[i].priority > maxMinPriority[0]) {
                    maxMinPriority[0] = processes[i].priority;
                }
                if (processes[i].priority < maxMinPriority[1]) {
                    maxMinPriority[1] = processes[i].priority;
                }
            } else {
                if (processes[i].priority < maxMinPriority[0]) {
                    maxMinPriority[0] = processes[i].priority;
                }
                if (processes[i].priority > maxMinPriority[1]) {
                    maxMinPriority[1] = processes[i].priority;
                }
            }
        }
    }

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

    static void checkFirstArrivalTime(Process[] processes, int n, boolean order) {
        for (int j = 1; j < n; j++) {
            if (processes[j].arrivalTime < processes[0].arrivalTime) {
                swapProcesses(processes, j, 0);
            } else if (processes[j].arrivalTime == processes[0].arrivalTime) {
                if ((processes[j].priority > processes[0].priority && !order) ||
                        (processes[j].priority < processes[0].priority && order)) {
                    swapProcesses(processes, j, 0);
                }
            }
        }
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

    static void sortByCompletionTime(Process[] processes, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].completionTime > processes[j + 1].completionTime) {
                    swapProcesses(processes, j, j + 1);
                }
            }
        }
    }

    static void sortProcesses(Process[] processes, int n, boolean order, int currentTime) {
        currentTime--;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].arrivalTime <= currentTime && processes[j].remainingTime > 0 &&
                        processes[j + 1].remainingTime > 0) {

                    if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                        if ((processes[j].priority > processes[j + 1].priority && !order) ||
                                (processes[j].priority < processes[j + 1].priority && order)) {
                            swapProcesses(processes, j, j + 1);
                        } else if (processes[j].priority == processes[j + 1].priority) {
                            if (processes[j].pn > processes[j + 1].pn) {
                                swapProcesses(processes, j, j + 1);
                            }
                        }
                    } else if (processes[j].arrivalTime == processes[j + 1].arrivalTime) {
                        if ((processes[j].priority > processes[j + 1].priority && !order) ||
                                (processes[j].priority < processes[j + 1].priority && order)) {
                            swapProcesses(processes, j, j + 1);
                        } else if (processes[j].priority == processes[j + 1].priority) {
                            if (processes[j].pn > processes[j + 1].pn) {
                                swapProcesses(processes, j, j + 1);
                            }
                        }
                    } else if (processes[j].arrivalTime < processes[j + 1].arrivalTime) {
                        if ((processes[j].priority > processes[j + 1].priority && !order) ||
                                (processes[j].priority < processes[j + 1].priority && order)) {
                            swapProcesses(processes, j, j + 1);
                        } else if (processes[j].priority == processes[j + 1].priority) {
                            if (processes[j].pn > processes[j + 1].pn) {
                                swapProcesses(processes, j, j + 1);
                            }
                        }
                    }
                }
            }
        }
    }

    static void printOutputPreemptive(Process[] processes, int n, int[] idPlaces,
                                      int[] runningTimes, int[] idleTimePlaces,
                                      int runningTimeIndex, int processIdIndex) {
        System.out.println("Gantt Chart by Process Number:");
        System.out.print("  ");
        for (int i = 0; i < processIdIndex; i++) {
            if (idPlaces[i] == -1) {
                System.out.print("| IDLE | ");
            } else {
                System.out.print("| P" + idPlaces[i] + " | ");
            }
        }
        System.out.println(" ");

        int space = 1;
        int j = 0;
        for (int i = 0; i < runningTimeIndex; i++) {
            if (runningTimes[i] >= 10 && i == idleTimePlaces[j]) {
                System.out.print("|" + runningTimes[i] + "|    ");
                j++;
            } else if (runningTimes[i] >= 10) {
                System.out.print("|" + runningTimes[i] + "|  ");
            } else if (i == idleTimePlaces[j]) {
                System.out.print("|" + runningTimes[i] + "|     ");
                j++;
            } else {
                System.out.print("|" + runningTimes[i] + "|   ");
            }

            for (int k = 0; k < space; k++) {
                if (runningTimes[i] >= 10) {
                    break;
                }
                System.out.print(" ");
            }

            if (space % 2 == 0) {
                space++;
            }
        }

        sortByPn(processes, n);
        System.out.println("\n\nProcess Table:");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("| Process Number | Process ID | Arrival Time | Final Expected Time | Burst Time | Priority | Completion Time | Turnaround Time | Waiting Time | Response Time |");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < n; i++) {
            System.out.printf("|       P%-7d |    PID-%-3d |      %-7d |          %-10d |    %-7d |    %-6d|       %-9d |       %-9d |       %-6d |       %-7d |\n",
                    processes[i].pn, processes[i].pid, processes[i].arrivalTime, processes[i].finalExpectedTime,
                    processes[i].burstTime, processes[i].priority, processes[i].completionTime,
                    processes[i].turnaroundTime, processes[i].waitingTime, processes[i].responseTime);
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");

        float totalWaitingTime = 0, totalTurnaroundTime = 0;
        for (int i = 0; i < n; i++) {
            totalWaitingTime += processes[i].waitingTime;
            totalTurnaroundTime += processes[i].turnaroundTime;
        }

        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTurnaroundTime / n);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaitingTime / n);
    }

    static int allProcessesSumBurstTime(Process[] processes, int n) {
        int burstTimeSum = processes[0].burstTime;
        for (int i = 1; i < n; i++) {
            burstTimeSum += processes[i].burstTime;
        }
        return burstTimeSum;
    }

    static void priorityPreemptive(Process[] processes, int n, boolean order) {
        int time = 0;
        int completed = 0;
        int prevProcId = -1;
        int burstTimeSum = allProcessesSumBurstTime(processes, n);
        int[] runningTimes = new int[burstTimeSum];
        int[] idPlaces = new int[burstTimeSum];
        int[] idleTimePlaces = new int[n];
        int indexOfRunningTime = 0, indexOfIdleTime = 0, indexOfProcessId = 0;

        checkFirstArrivalTime(processes, n, order);

        while (completed != n) {
            int idx = -1;
            int priority = order ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= time && processes[i].remainingTime > 0) {
                    boolean higherPriority = order ? (processes[i].priority > priority) :
                            (processes[i].priority < priority);
                    if (higherPriority) {
                        if (idx == -1) {
                            priority = processes[i].priority;
                            idx = i;
                        } else if (processes[i].arrivalTime < processes[idx].arrivalTime &&
                                processes[idx].remainingTime > 0) {
                            priority = processes[i].priority;
                            idx = i;
                        } else if ((processes[idx].priority > processes[i].priority && !order) ||
                                (processes[idx].priority < processes[i].priority && order)) {
                            priority = processes[i].priority;
                            idx = i;
                        }
                    }
                }
            }

            if (idx == -1) {
                if (prevProcId != -1) {
                    idleTimePlaces[indexOfIdleTime++] = indexOfRunningTime;
                    runningTimes[indexOfRunningTime++] = time;
                    idPlaces[indexOfProcessId++] = -1;
                    prevProcId = -1;
                } else {
                    time++;
                    sortProcesses(processes, n, order, time);
                    continue;
                }
            } else {
                if (prevProcId == -1) {
                    runningTimes[indexOfRunningTime++] = time;
                    idPlaces[indexOfProcessId++] = processes[idx].pn;
                } else if (prevProcId != processes[idx].pid) {
                    runningTimes[indexOfRunningTime++] = time;
                    idPlaces[indexOfProcessId++] = processes[idx].pn;
                }

                processes[idx].remainingTime--;
                if (prevProcId != processes[idx].pid) {
                    prevProcId = processes[idx].pid;
                }
                if (!processes[idx].cpuGivenOrNot) {
                    processes[idx].cpuGivenOrNot = true;
                    processes[idx].cpuFirstGivenTime = time;
                }

                if (processes[idx].remainingTime == 0) {
                    if (completed == (n - 1)) {
                        runningTimes[indexOfRunningTime++] = time + 1;
                    }
                    processes[idx].completionTime = time + 1;
                    processes[idx].turnaroundTime = processes[idx].completionTime - processes[idx].arrivalTime;
                    processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;
                    processes[idx].responseTime = processes[idx].cpuFirstGivenTime - processes[idx].arrivalTime;
                    completed++;
                }
            }
            time++;
            sortProcesses(processes, n, order, time);
        }

        idleTimePlaces[indexOfIdleTime++] = -1;
        printOutputPreemptive(processes, n, idPlaces, runningTimes, idleTimePlaces,
                indexOfRunningTime, indexOfProcessId);
    }

    static void priorityNonPreemptive(Process[] processes, int n, boolean order) {
        int completed = 0;
        int time = 0;
        checkFirstArrivalTime(processes, n, order);

        while (completed != n) {
            int idx = -1;
            int priority = order ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= time && processes[i].remainingTime > 0) {
                    boolean higherPriority = order ? (processes[i].priority > priority) :
                            (processes[i].priority < priority);
                    if (higherPriority) {
                        if (idx == -1) {
                            priority = processes[i].priority;
                            idx = i;
                        } else if (processes[i].arrivalTime < processes[idx].arrivalTime &&
                                processes[idx].remainingTime > 0) {
                            priority = processes[i].priority;
                            idx = i;
                        } else if ((processes[idx].priority > processes[i].priority && !order) ||
                                (processes[idx].priority < processes[i].priority && order)) {
                            priority = processes[i].priority;
                            idx = i;
                        }
                    }
                }
            }

            if (idx != -1) {
                time += processes[idx].remainingTime;
                processes[idx].remainingTime = 0;
                processes[idx].completionTime = time;
                processes[idx].turnaroundTime = processes[idx].completionTime - processes[idx].arrivalTime;
                processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;
                completed++;
            } else {
                time++;
            }
        }
    }

    static void printOutputNonPreemptive(Process[] processes, int n) {
        sortByCompletionTime(processes, n);
        System.out.println("Gantt Chart by Process Number:");
        System.out.print("  ");
        int time = processes[0].arrivalTime;
        if (processes[0].arrivalTime != 0) {
            System.out.print("| IDLE | ");
        }

        for (int i = 0; i < n; i++) {
            if (time == processes[i].arrivalTime ||
                    (i > 0 && time == processes[i-1].completionTime && time >= processes[i].arrivalTime)) {
                System.out.print("| P" + processes[i].pn + " | ");
            } else {
                System.out.print("| IDLE | ");
                time = processes[i].arrivalTime;
                System.out.print("| P" + processes[i].pn + " | ");
            }
            time += processes[i].burstTime;
        }

        System.out.println(" ");
        time = processes[0].arrivalTime;
        int space = 1;
        if (processes[0].arrivalTime != 0) {
            System.out.print("|0|      ");
        }

        for (int i = 0; i < n; i++) {
            if (time == processes[i].arrivalTime ||
                    (i > 0 && time == processes[i-1].completionTime && time >= processes[i].arrivalTime)) {
                System.out.print("|" + time + "|   ");
            } else {
                if (time < 10) {
                    System.out.print("|" + time + "|      ");
                } else {
                    System.out.print("|" + time + "|     ");
                }
                time = processes[i].arrivalTime;
                System.out.print("|" + time + "|   ");
            }

            for (int j = 0; j < space; j++) {
                if (time >= 10) {
                    break;
                }
                System.out.print(" ");
            }

            if (space % 2 == 0) {
                space++;
            }
            time += processes[i].burstTime;
        }

        System.out.println("|" + time + "|       ");
        sortByPn(processes, n);
        System.out.println("\n\nProcess Table:");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("| Process Number | Process ID | Arrival Time | Final Expected Time | Burst Time | Priority | Completion Time | Turnaround Time | Waiting Time |");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < n; i++) {
            System.out.printf("|       P%-7d |    PID-%-3d |      %-7d |          %-10d |    %-7d |    %-6d|       %-9d |       %-9d |       %-6d |\n",
                    processes[i].pn, processes[i].pid, processes[i].arrivalTime, processes[i].finalExpectedTime,
                    processes[i].burstTime, processes[i].priority, processes[i].completionTime,
                    processes[i].turnaroundTime, processes[i].waitingTime);
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");

        float totalWaitingTime = 0, totalTurnaroundTime = 0;
        for (int i = 0; i < n; i++) {
            totalWaitingTime += processes[i].waitingTime;
            totalTurnaroundTime += processes[i].turnaroundTime;
        }

        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTurnaroundTime / n);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaitingTime / n);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();
        Process[] processes = new Process[n];

        System.out.println("Enter PID, Arrival Time, Burst Time, and Priority for each process:");
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
            System.out.print("Priority: ");
            processes[i].priority = scanner.nextInt();
            processes[i].remainingTime = processes[i].burstTime;
        }

        System.out.print("Enter 1 for Preemptive, 0 for Non-Preemptive: ");
        int mode = scanner.nextInt();
        if (mode > 1 || mode < 0) {
            System.out.println("Invalid Choice !!!");
            System.exit(0);
        }

        boolean preemptive = (mode == 1);

        System.out.print("Enter 1 if higher number means higher priority, 0 if higher number means lower priority: ");
        int numOrder = scanner.nextInt();
        if (numOrder > 1 || numOrder < 0) {
            System.out.println("Invalid Choice !!!");
            System.exit(0);
        }

        boolean order = (numOrder == 1);
        int[] maxMinPriority = new int[2];
        findMaxMinPriority(processes, n, maxMinPriority, order);
        calculateFinalExpectedTime(processes, n);
        sortProcesses(processes, n, order, 0);

        if (preemptive) {
            System.out.println("\n\nAnalyzing the processes in the Priority Algorithm (Type - Preemptive): ");
            System.out.println("Highest Priority - " + maxMinPriority[0] + " and Lowest Priority - " + maxMinPriority[1] +
                    " according to " + (order ? "Higher Number, Higher Priority" : "Higher Number, Lower Priority"));
            System.out.println();
            priorityPreemptive(processes, n, order);
        } else {
            System.out.println("\n\nAnalyzing the processes in the Priority Algorithm (Type - Non-Preemptive): ");
            System.out.println("Highest Priority - " + maxMinPriority[0] + " and Lowest Priority - " + maxMinPriority[1] +
                    " according to " + (order ? "Higher Number, Higher Priority" : "Higher Number, Lower Priority"));
            System.out.println();
            priorityNonPreemptive(processes, n, order);
            printOutputNonPreemptive(processes, n);
        }

        scanner.close();
    }
}