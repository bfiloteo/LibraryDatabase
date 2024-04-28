package library;

public class Article {

    private int articleId;
    private String author;
    private String title;
    private int volume;
    private int availableCopies;
    private int totalCopies;
    private int issue;

    // No-arg constructor (default constructor)
    public Article() {
        articleId = 0;
        author = "";
        title = "";
        volume = 0;
        availableCopies = 0;
        totalCopies = 0;
        issue = 0;
    }

    // Getters
    public int getArticleId() {
        return articleId;
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

    public int getIssue() {
        return issue;
    }

    // Setters
    public void setArticleId(int articleId) {
        this.articleId = articleId;
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

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }
}
