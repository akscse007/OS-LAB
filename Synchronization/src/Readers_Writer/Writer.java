package Readers_Writer;

class Writer extends Thread {
    private final Library library;
    private final String bookTitle;
    private final String newContent;

    public Writer(Library library, String bookTitle, String newContent) {
        this.library = library;
        this.bookTitle = bookTitle;
        this.newContent = newContent;
    }

    @Override
    public void run() {
        library.writeBook(bookTitle, newContent);
        System.out.println(Thread.currentThread().getName() + " finished writing to " + bookTitle);
    }
}

