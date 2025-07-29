import java.util.Scanner;

public class BestFitMemoryAllocation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Best-Fit Memory Allocation ***\n\n");

        System.out.print("Enter The Number of Blocks you want to add = ");
        int nBlock = scanner.nextInt();
        System.out.print("Enter The Number of Processes you want to add = ");
        int nProcess = scanner.nextInt();

        int[] blockSize = new int[nBlock];
        int[] processSize = new int[nProcess];
        int[] allocation = new int[nProcess];

        // Initialize allocation array to -1 (not allocated)
        for (int i = 0; i < nProcess; i++) {
            allocation[i] = -1;
        }

        System.out.println("\nEnter the sizes of the blocks:");
        for (int i = 0; i < nBlock; i++) {
            System.out.print("Block " + (i + 1) + " size: ");
            blockSize[i] = scanner.nextInt();
        }

        System.out.println("\nEnter the sizes of the processes:");
        for (int i = 0; i < nProcess; i++) {
            System.out.print("Process " + (i + 1) + " size: ");
            processSize[i] = scanner.nextInt();
        }

        // Best-fit allocation
        for (int i = 0; i < nProcess; i++) {
            int bestIdx = -1;
            for (int j = 0; j < nBlock; j++) {
                if (blockSize[j] >= processSize[i]) {
                    if (bestIdx == -1 || blockSize[j] < blockSize[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }

            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                blockSize[bestIdx] -= processSize[i];
            }
        }

        // Output the allocation result
        System.out.println("\n-------------------------------------------------");
        System.out.println("|  Process No. |  Process Size  |   Block no.   |");
        System.out.println("-------------------------------------------------");
        for (int i = 0; i < nProcess; i++) {
            System.out.printf("|       %-7d|       %-9d|", (i + 1), processSize[i]);
            if (allocation[i] != -1) {
                System.out.printf("       %-8d|\n", (allocation[i] + 1));
            } else {
                System.out.printf(" Not Allocated |\n");
            }
        }
        System.out.println("-------------------------------------------------");

        scanner.close();
    }
}