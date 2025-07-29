import java.util.Arrays;
import java.util.Scanner;

public class SCANDiskScheduling {
    private static final int DEFAULT_LOWER = 0;
    private static final int DEFAULT_UPPER = 199;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("SCAN (Elevator) Disk Scheduling Algorithm");

        // Get disk boundaries
        System.out.printf("Enter lower limit (default %d): ", DEFAULT_LOWER);
        int lower = getValidInput(scanner, DEFAULT_LOWER);
        System.out.printf("Enter upper limit (default %d): ", DEFAULT_UPPER);
        int upper = getValidInput(scanner, DEFAULT_UPPER);

        // Get initial position
        System.out.print("Enter initial head position: ");
        int head = getBoundedInput(scanner, lower, upper);

        // Get movement direction
        System.out.print("Enter initial direction (L for left, R for right): ");
        char direction = getValidDirection(scanner);

        // Get disk requests
        System.out.print("Enter number of disk requests: ");
        int numRequests = scanner.nextInt();
        int[] requests = new int[numRequests];
        System.out.println("Enter disk requests:");
        for (int i = 0; i < numRequests; i++) {
            requests[i] = getBoundedInput(scanner, lower, upper);
        }

        // Process requests
        processSCAN(head, requests, direction, lower, upper);
        scanner.close();
    }

    private static int getValidInput(Scanner scanner, int defaultValue) {
        try {
            String input = scanner.next();
            return input.isEmpty() ? defaultValue : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static int getBoundedInput(Scanner scanner, int min, int max) {
        while (true) {
            int value = scanner.nextInt();
            if (value >= min && value <= max) return value;
            System.out.printf("Please enter value between %d and %d: ", min, max);
        }
    }

    private static char getValidDirection(Scanner scanner) {
        while (true) {
            char dir = Character.toLowerCase(scanner.next().charAt(0));
            if (dir == 'l' || dir == 'r') return dir;
            System.out.print("Invalid direction. Enter L or R: ");
        }
    }

    private static void processSCAN(int head, int[] requests, char direction, int lower, int upper) {
        Arrays.sort(requests);

        System.out.println("\nProcessing order:");
        int totalMovement = 0;
        int current = head;

        if (direction == 'l') {
            // Move left first
            for (int i = findFirstLessThan(requests, head); i >= 0; i--) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }

            // Hit the lower bound
            totalMovement += Math.abs(current - lower);
            System.out.printf("Move from %3d to %3d (Distance: %3d) [Lower Bound]\n", current, lower, Math.abs(current - lower));
            current = lower;

            // Then move right
            for (int i = findFirstGreaterThan(requests, head); i < requests.length; i++) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }
        } else {
            // Move right first
            for (int i = findFirstGreaterThan(requests, head); i < requests.length; i++) {
                int move = Math.abs(requests[i] - current);
                totalMovement += move;
                System.out.printf("Move from %3d to %3d (Distance: %3d)\n", current, requests[i], move);
                current = requests[i];
            }

            // Hit the upper bound
            totalMovement += Math.abs(current - upper);
            System.out.printf("Move from %3d to %3d (Distance: %3d) [Upper Bound]\n", current, upper, Math.abs(current - upper));
            current = upper;

            // Then move left
            for (int i = findFirstLessThan(requests, head); i >= 0; i--) {
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