import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class FIFOPageReplacement {
    private static final int MAX_FRAMES = 100; // Reasonable limit for frames

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("FIFO Page Replacement Algorithm");

        // Get number of frames with validation
        int frameCount = getValidInput(scanner, "Enter number of frames (1-" + MAX_FRAMES + "): ", 1, MAX_FRAMES);

        // Get page references
        System.out.print("Enter page reference string (space separated numbers/letters): ");
        String[] pageRefs = scanner.nextLine().trim().split("\\s+");

        // Process and display results
        processFIFO(frameCount, pageRefs);
        scanner.close();
    }

    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.printf("Please enter a value between %d and %d\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void processFIFO(int frameCount, String[] pageRefs) {
        Set<String> frames = new LinkedHashSet<>(frameCount);
        int faults = 0;
        int hits = 0;

        System.out.println("\nPage Replacement Process:");
        System.out.println("-------------------------");

        for (String page : pageRefs) {
            page = page.trim();
            System.out.printf("Referencing page %s: ", page);

            if (frames.contains(page)) {
                hits++;
                System.out.print("Hit");
            } else {
                faults++;
                if (frames.size() == frameCount) {
                    // Remove the oldest page (first in the set)
                    String oldest = frames.iterator().next();
                    frames.remove(oldest);
                }
                frames.add(page);
                System.out.print("Fault");
            }

            // Display current frames
            System.out.println(" | Frames: " + frames);
        }

        // Print summary
        System.out.println("\nSummary:");
        System.out.println("--------");
        System.out.println("Total references: " + pageRefs.length);
        System.out.println("Page faults: " + faults);
        System.out.println("Page hits: " + hits);
        System.out.printf("Fault rate: %.2f%%\n", (faults * 100.0 / pageRefs.length));
    }
}