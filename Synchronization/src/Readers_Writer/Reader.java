package Readers_Writer;

class Reader extends Thread {
    private final Library library;
    private final String bookTitle;

    public Reader(Library library, String bookTitle) {
        this.library = library;
        this.bookTitle = bookTitle;
    }

    @Override
    public void run() {
        String content = library.readBook(bookTitle);
        System.out.println(Thread.currentThread().getName() + " finished reading: " + content.substring(0, Math.min(10, content.length())) + "...");
    }
}
