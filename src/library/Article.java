package library;

/*
 * Article object.
 * 
 * Constructor, getters, setters.
 */

public class Article extends Item {

    private String author;
    private String title;
    private int volume;
    private String issue;

    // No-arg constructor (default constructor)
    public Article() {
        super();
        author = "";
        title = "";
        volume = 0;
        issue = "";
    }

    // Getters
    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getVolume() {
        return volume;
    }

    public String getIssue() {
        return issue;
    }

    // Setters
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
