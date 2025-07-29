package Readers_Writer;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Library {
    private final List<Book> books = new ArrayList<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public Library() {
        // Initialize with some books
        books.add(new Book("Java Programming", "Introduction to Java..."));
        books.add(new Book("Design Patterns", "Singleton, Factory..."));
    }

    public String readBook(String title) {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is reading " + title);
            Thread.sleep(1000); // Simulate reading time
            for (Book book : books) {
                if (book.getTitle().equals(title)) {
                    return book.read();
                }
            }
            return "Book not found";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Reading interrupted";
        } finally {
            readLock.unlock();
        }
    }

    public void writeBook(String title, String newContent) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is writing to " + title);
            Thread.sleep(1500); // Simulate writing time
            for (Book book : books) {
                if (book.getTitle().equals(title)) {
                    book.write(newContent);
                    return;
                }
            }
            books.add(new Book(title, newContent));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
        }
    }
}
