package library;

/*
 * Article object.
 * 
 * Constructor, getters, setters.
 */

public class Article {

    private int articleID;
    private String author;
    private String title;
    private int volume;
    private String issue;
    private int availableCopies;
    private int totalCopies;

    // No-arg constructor (default constructor)
    public Article() {
        articleID = 0;
        author = "";
        title = "";
        volume = 0;
        availableCopies = 0;
        totalCopies = 0;
        issue = "";
    }

    // Getters
    public int getArticleID() {
        return articleID;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getVolume() {
        return volume;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public String getIssue() {
        return issue;
    }

    // Setters
    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

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

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

}
