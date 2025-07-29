import java.util.Scanner;

public class FirstFitMemoryAllocation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** First-Fit Memory Allocation ***\n\n");

        // Get number of blocks and processes
        System.out.print("Enter The Number of Blocks you want to add = ");
        int nBlock = scanner.nextInt();
        System.out.print("Enter The Number of Processes you want to add = ");
        int nProcess = scanner.nextInt();

        // Initialize arrays
        int[] blockSize = new int[nBlock];
        int[] processSize = new int[nProcess];
        int[] allocation = new int[nProcess];

        // Initialize allocation to -1 (not allocated)
        for (int i = 0; i < nProcess; i++) {
            allocation[i] = -1;
        }

        // Get block sizes
        System.out.println("\nEnter the sizes of the blocks:");
        for (int i = 0; i < nBlock; i++) {
            System.out.print("Block " + (i + 1) + " size: ");
            blockSize[i] = scanner.nextInt();
        }

        // Get process sizes
        System.out.println("\nEnter the sizes of the processes:");
        for (int i = 0; i < nProcess; i++) {
            System.out.print("Process " + (i + 1) + " size: ");
            processSize[i] = scanner.nextInt();
        }

        // First-fit allocation algorithm
        for (int i = 0; i < nProcess; i++) {
            for (int j = 0; j < nBlock; j++) {
                if (blockSize[j] >= processSize[i]) {
                    allocation[i] = j;           // Allocate block j to process i
                    blockSize[j] -= processSize[i]; // Reduce available memory in this block
                    break; // Move to next process after first fit is found
                }
            }
        }

        // Print allocation results
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