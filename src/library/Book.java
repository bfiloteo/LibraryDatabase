package library;

public class Book {
    private int bookID;
    private String genre;
    private String title;
    private String author;
    private int ISBN;
    private int availableCopies;
    private int totalCopies;

    // Constructor to initialize all fields
    public Book() {
        bookID = 0;
        genre = "";
        title = "";
        author = "";
        ISBN = 0;
        availableCopies = 0;
        totalCopies = 0;
    }
    
    // Getters for all fields
    public int getBookID() {
        return bookID;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public int getISBN() {
        return ISBN;
    }
    
    public int getAvailableCopies() {
        return availableCopies;
    }
    
    public int getTotalCopies() {
        return totalCopies;
    }
    
    // Setters for all fields
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }
    
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
}
