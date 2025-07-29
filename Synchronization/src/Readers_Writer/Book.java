package Readers_Writer;

class Book {
    private final String title;
    private String content;

    public Book(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String read() {
        return content;
    }

    public void write(String newContent) {
        this.content = newContent;
    }

    public String getTitle() {
        return title;
    }
}

