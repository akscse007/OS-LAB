import java.util.Arrays;
import java.util.Scanner;

public class CSCANDiskScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("C-SCAN Disk Scheduling Algorithm");

        // Get disk boundaries
        System.out.print("Enter lower limit: ");
        int lower = scanner.nextInt();
        System.out.print("Enter upper limit: ");
        int upper = scanner.nextInt();

        // Get initial position
        System.out.print("Enter initial head position: ");
        int head = scanner.nextInt();

        // Get movement direction
        System.out.print("Enter initial direction (L for left, R for right): ");
        char direction = Character.toLowerCase(scanner.next().charAt(0));

        // Get disk requests
        System.out.print("Enter number of disk requests: ");
        int numRequests = scanner.nextInt();
        int[] requests = new int[numRequests];
        System.out.println("Enter disk requests:");
        for (int i = 0; i < numRequests; i++) {
            requests[i] = scanner.nextInt();
        }

        // Process requests
        processCSCAN(head, requests, direction, lower, upper);
        scanner.close();
    }

    private static void processCSCAN(int head, int[] requests, char direction, int lower, int upper) {
        Arrays.sort(requests);

        System.out.println("\nProcessing order:");
        int totalMovement = 0;
        int current = head;

        if (direction == 'l') {
            // Move left to lower limit
            for (int i = findFirstLessThan(requests, head); i >= 0; i--) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }

            // Jump to upper limit
            int jump = Math.abs(current - upper);
            totalMovement += jump;
            System.out.printf("Jump from %3d to %3d (Distance: %3d) [Boundary]\n", current, upper, jump);
            current = upper;

            // Continue scanning left from upper limit
            for (int i = requests.length - 1; i >= 0 && requests[i] > head; i--) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }
        } else {
            // Move right to upper limit
            for (int i = findFirstGreaterThan(requests, head); i < requests.length; i++) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }

            // Jump to lower limit
            int jump = Math.abs(current - lower);
            totalMovement += jump;
            System.out.printf("Jump from %3d to %3d (Distance: %3d) [Boundary]\n", current, lower, jump);
            current = lower;

            // Continue scanning right from lower limit
            for (int i = 0; i < requests.length && requests[i] < head; i++) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }
        }

        System.out.println("\nTotal head movement: " + totalMovement);
        System.out.printf("Average seek length: %.2f\n", (double)totalMovement/requests.length);
    }

    private static int findFirstLessThan(int[] arr, int value) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] <= value) return i;
        }
        return -1;
    }

    private static int findFirstGreaterThan(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= value) return i;
        }
        return arr.length;
    }
}