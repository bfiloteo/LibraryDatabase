package library;

import java.util.Date;

public class Transaction {

    private int transactionID;
    private String title;
    private String transactionType; // "borrow", "return"
    private String mediaType; // "article", "book", "movie"
    private Date transactionDate;
    private Date dueDate;
    private int memberID;
    private int bookID;
    private int articleID;
    private int movieID;

    public Transaction() {
        transactionID = 0;
        title = "";
        transactionType = "";
        mediaType = "";
        transactionDate = new Date();
        dueDate = new Date();
        memberID = 0;
        bookID = 0;
        articleID = 0;
        movieID = 0;
    }

    // Getters
    public int getTransactionID() {
        return transactionID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getMemberID() {
        return memberID;
    }

    public Integer getBookID() {
        return bookID;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public Integer getMovieID() {
        return movieID;
    }

    // Setters with basic validation (optional)
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTransactionType(String transactionType) {
        if (transactionType == null || 
                !transactionType.equalsIgnoreCase("return") &&
                !transactionType.equalsIgnoreCase("borrow")) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
        this.transactionType = transactionType;
    }

    public void setMediaType(String mediaType) {
        if (mediaType == null || 
                !mediaType.equalsIgnoreCase("article") &&
                !mediaType.equalsIgnoreCase("book") &&
                !mediaType.equalsIgnoreCase("movie")) {
            throw new IllegalArgumentException("Invalid media type: " + mediaType);
        }
        this.mediaType = mediaType;
    }

    public void setTransactionDate(Date transactionDate) {
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        this.transactionDate = transactionDate;
    }

    public void setDueDate(Date dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        this.dueDate = dueDate;
    }

    public void setMemberID(int memberID) {
        // You might want to validate member ID against a database or other logic
        this.memberID = memberID;
    }

    public void setBookID(Integer bookID) {
        // Check if transaction type is "book" before setting
        if (transactionType == null || !mediaType.equalsIgnoreCase("book")) {
            throw new IllegalArgumentException("Book ID can only be set for book transactions");
        }
        this.bookID = bookID;
    }

    public void setArticleID(Integer articleID) {
        // Check if transaction type is "article" before setting
        if (transactionType == null || !mediaType.equalsIgnoreCase("article")) {
            throw new IllegalArgumentException("Article ID can only be set for article transactions");
        }
        this.articleID = articleID;
    }

    public void setMovieID(Integer movieID) {
        // Check if transaction type is "movie" before setting
        if (transactionType == null || !mediaType.equalsIgnoreCase("movie")) {
            throw new IllegalArgumentException("Movie ID can only be set for movie transactions");
        }
        this.movieID = movieID;
    }
}
