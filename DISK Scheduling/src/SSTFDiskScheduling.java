import java.util.Arrays;
import java.util.Scanner;

public class SSTFDiskScheduling {
    private static final int MAX_CYLINDERS = 200;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("SSTF Disk Scheduling Algorithm");
        System.out.print("Enter initial head position (0-199): ");
        int headPosition = validateInput(scanner.nextInt());

        System.out.print("Enter number of disk requests: ");
        int numRequests = scanner.nextInt();

        int[] requests = new int[numRequests];
        System.out.println("Enter disk requests (0-199):");
        for (int i = 0; i < numRequests; i++) {
            requests[i] = validateInput(scanner.nextInt());
        }

        processRequests(headPosition, requests);
        scanner.close();
    }

    private static int validateInput(int value) {
        return Math.max(0, Math.min(value, MAX_CYLINDERS - 1));
    }

    private static void processRequests(int headStart, int[] requests) {
        System.out.println("\nProcessing order:");
        int totalMovement = 0;
        int currentPosition = headStart;
        boolean[] completed = new boolean[requests.length];

        for (int i = 0; i < requests.length; i++) {
            int nearestIndex = findNearestRequest(currentPosition, requests, completed);
            int distance = Math.abs(requests[nearestIndex] - currentPosition);
            totalMovement += distance;

            System.out.printf("Move from %3d to %3d (Seek: %3d)\n",
                    currentPosition, requests[nearestIndex], distance);
            currentPosition = requests[nearestIndex];
            completed[nearestIndex] = true;
        }

        System.out.println("\nTotal head movement: " + totalMovement);
        System.out.printf("Average seek length: %.2f\n", (double)totalMovement/requests.length);
    }

    private static int findNearestRequest(int current, int[] requests, boolean[] completed) {
        int minDistance = Integer.MAX_VALUE;
        int nearestIndex = -1;

        for (int i = 0; i < requests.length; i++) {
            if (!completed[i]) {
                int distance = Math.abs(requests[i] - current);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestIndex = i;
                }
            }
        }
        return nearestIndex;
    }
}