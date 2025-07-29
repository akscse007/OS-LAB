import java.util.Arrays;
import java.util.Scanner;

public class FCFSDiskScheduling {
    private static final int MAX_CYLINDERS = 200;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("FCFS Disk Scheduling Algorithm");
        System.out.print("Enter initial head position (0-199): ");
        int headPosition = scanner.nextInt();

        System.out.print("Enter number of disk requests: ");
        int numRequests = scanner.nextInt();

        int[] requests = new int[numRequests];
        System.out.println("Enter disk requests (0-199):");
        for (int i = 0; i < numRequests; i++) {
            requests[i] = scanner.nextInt();
        }

        processRequests(headPosition, requests);
        scanner.close();
    }

    private static void processRequests(int headStart, int[] requests) {
        System.out.println("\nProcessing order:");
        int totalMovement = 0;
        int currentPosition = headStart;

        for (int request : requests) {
            int movement = Math.abs(request - currentPosition);
            totalMovement += movement;
            System.out.printf("Move from %3d to %3d (Distance: %3d)\n",
                    currentPosition, request, movement);
            currentPosition = request;
        }

        System.out.println("\nTotal head movement: " + totalMovement);
        System.out.println("Average seek length: " + (double)totalMovement/requests.length);

        visualizeMovement(headStart, requests);
    }

    private static void visualizeMovement(int headStart, int[] requests) {
        System.out.println("\nVisualization:");

        // Create timeline with all positions
        int[] timeline = Arrays.copyOf(requests, requests.length + 1);
        timeline[requests.length] = headStart;
        Arrays.sort(timeline);

        // Print position markers
        for (int pos : timeline) {
            System.out.printf("%4d", pos);
        }
        System.out.println();

        // Print movement path
        int current = headStart;
        for (int request : requests) {
            int from = Math.min(current, request);
            int to = Math.max(current, request);

            for (int pos : timeline) {
                if (pos >= from && pos <= to) {
                    System.out.print("  * ");
                } else {
                    System.out.print("    ");
                }
            }
            System.out.println();
            current = request;
        }
    }
}