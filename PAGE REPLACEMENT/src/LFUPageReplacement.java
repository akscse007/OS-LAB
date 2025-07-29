import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class LFUPageReplacement {
    private static final int MAX_FRAMES = 10; // Reasonable limit for frames

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("LFU Page Replacement Algorithm");

        // Get number of frames with validation
        int frameCount = getValidInput(scanner, "Enter number of frames (1-" + MAX_FRAMES + "): ", 1, MAX_FRAMES);

        // Get page references
        System.out.print("Enter page reference string (space separated numbers/letters): ");
        String[] pageRefs = scanner.nextLine().trim().split("\\s+");

        // Process and display results
        processLFU(frameCount, pageRefs);
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

    private static void processLFU(int frameCount, String[] pageRefs) {
        Set<String> frames = new LinkedHashSet<>(frameCount);
        HashMap<String, Integer> pageFrequency = new HashMap<>();
        HashMap<String, Integer> pageLastUsed = new HashMap<>();
        int faults = 0;
        int hits = 0;

        System.out.println("\nPage Replacement Process:");
        System.out.println("-------------------------");

        for (int time = 0; time < pageRefs.length; time++) {
            String page = pageRefs[time].trim();
            System.out.printf("Referencing page %s: ", page);

            if (frames.contains(page)) {
                // Page hit - update frequency and last used time
                pageFrequency.put(page, pageFrequency.get(page) + 1);
                pageLastUsed.put(page, time);
                hits++;
                System.out.print("Hit");
            } else {
                // Page fault - replace if needed
                if (frames.size() == frameCount) {
                    // Find LFU page (with LRU as tiebreaker)
                    String lfuPage = findLFUPage(frames, pageFrequency, pageLastUsed);
                    frames.remove(lfuPage);
                    pageFrequency.remove(lfuPage);
                    pageLastUsed.remove(lfuPage);
                }
                frames.add(page);
                pageFrequency.put(page, 1);
                pageLastUsed.put(page, time);
                faults++;
                System.out.print("Fault");
            }

            // Display current frames with frequencies
            System.out.print(" | Frames: " + frames);
            System.out.print(" | Frequencies: " + pageFrequency + "\n");
        }

        // Print summary
        System.out.println("\nSummary:");
        System.out.println("--------");
        System.out.println("Total references: " + pageRefs.length);
        System.out.println("Page faults: " + faults);
        System.out.println("Page hits: " + hits);
        System.out.printf("Fault rate: %.2f%%\n", (faults * 100.0 / pageRefs.length));
    }

    private static String findLFUPage(Set<String> frames,
                                      HashMap<String, Integer> frequency,
                                      HashMap<String, Integer> lastUsed) {
        String lfuPage = null;
        int minFrequency = Integer.MAX_VALUE;
        int oldestTime = Integer.MAX_VALUE;

        for (String page : frames) {
            int freq = frequency.get(page);
            int time = lastUsed.get(page);

            if (freq < minFrequency ||
                    (freq == minFrequency && time < oldestTime)) {
                minFrequency = freq;
                oldestTime = time;
                lfuPage = page;
            }
        }
        return lfuPage;
    }
}