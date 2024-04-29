package library;

import java.util.Date;

public class Transaction {

    private int transactionId;
    private String title;
    private Date transactionDate;
    private String transactionType; // "buy", "rent", or "book"
    private Date dueDate;
    private int memberId;
    private int bookId;
    private int articleId;
    private int movieId;

    public Transaction() {
        transactionId = 0;
        title = "";
        transactionDate = new Date();
        transactionType = "";
        dueDate = new Date();
        memberId = 0;
        bookId = 0;
        articleId = 0;
        movieId = 0;
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public String getTitle()
    {
        return title;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    // Setters with basic validation (optional)
    public void setTransactionDate(Date transactionDate) {
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        this.transactionDate = transactionDate;
    }

    public void setTransactionType(String transactionType) {
        if (transactionType == null || !transactionType.equalsIgnoreCase("buy") &&
                !transactionType.equalsIgnoreCase("rent") &&
                !transactionType.equalsIgnoreCase("book")) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
        this.transactionType = transactionType;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setMemberId(int memberId) {
        // You might want to validate member ID against a database or other logic
        this.memberId = memberId;
    }

    public void setBookId(Integer bookId) {
        // Check if transaction type is "book" before setting
        if (transactionType == null || !transactionType.equalsIgnoreCase("book")) {
            throw new IllegalArgumentException("Book ID can only be set for book transactions");
        }
        this.bookId = bookId;
    }

    public void setArticleId(Integer articleId) {
        // Check if transaction type is "article" before setting
        if (transactionType == null || !transactionType.equalsIgnoreCase("article")) {
            throw new IllegalArgumentException("Article ID can only be set for article transactions");
        }
        this.articleId = articleId;
    }

    public void setMovieId(Integer movieId) {
        // Check if transaction type is "movie" before setting
        if (transactionType == null || !transactionType.equalsIgnoreCase("movie")) {
            throw new IllegalArgumentException("Movie ID can only be set for movie transactions");
        }
        this.movieId = movieId;
    }
}
