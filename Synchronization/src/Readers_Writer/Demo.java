package Readers_Writer;



public class Demo {
    public static void main(String[] args) {
        Library library = new Library();

        // Create multiple readers and writers
        Thread[] readers = new Thread[10];
        Thread[] writers = new Thread[12];

        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Reader(library, i % 2 == 0 ? "Java Programming" : "Design Patterns");
            readers[i].setName("Reader-" + (i + 1));
        }

        for (int i = 0; i < writers.length; i++) {
            writers[i] = new Writer(library,
                    i == 0 ? "Java Programming" : "Design Patterns",
                    "Updated content " + System.currentTimeMillis());
            writers[i].setName("Writer-" + (i + 1));
        }

        // Start all threads
        for (Thread writer : writers) {
            writer.start();
        }

        for (Thread reader : readers) {
            reader.start();
        }

        // Wait for all threads to complete
        try {
            for (Thread writer : writers) {
                writer.join();
            }
            for (Thread reader : readers) {
                reader.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All reading/writing operations completed");
    }
}