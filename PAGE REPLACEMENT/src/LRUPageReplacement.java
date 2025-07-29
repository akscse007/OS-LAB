import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class LRUPageReplacement {
    private static final int MAX_FRAMES = 10; // Reasonable limit for frames

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("LRU Page Replacement Algorithm");

        // Get number of frames with validation
        int frameCount = getValidInput(scanner, "Enter number of frames (1-" + MAX_FRAMES + "): ", 1, MAX_FRAMES);

        // Get page references
        System.out.print("Enter page reference string (space separated numbers/letters): ");
        String[] pageRefs = scanner.nextLine().split("\\s+");

        // Process and display results
        processLRU(frameCount, pageRefs);
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

    private static void processLRU(int frameCount, String[] pageRefs) {
        Set<String> frames = new LinkedHashSet<>(frameCount);
        HashMap<String, Integer> pageLastUsed = new HashMap<>();
        int faults = 0;
        int hits = 0;

        System.out.println("\nPage Replacement Process:");
        System.out.println("-------------------------");

        for (int i = 0; i < pageRefs.length; i++) {
            String page = pageRefs[i].trim();
            System.out.printf("Referencing page %s: ", page);

            if (frames.contains(page)) {
                // Page hit - update usage
                frames.remove(page);
                frames.add(page);
                hits++;
                System.out.print("Hit");
            } else {
                // Page fault - replace if needed
                if (frames.size() == frameCount) {
                    // Find LRU page (first element in the set)
                    String lru = frames.iterator().next();
                    frames.remove(lru);
                }
                frames.add(page);
                faults++;
                System.out.print("Fault");
            }

            // Update last used time
            pageLastUsed.put(page, i);

            // Display current frames
            System.out.print(" | Frames: " + frames + "\n");
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