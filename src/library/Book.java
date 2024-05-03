package library;

public class Book extends Item {
    private String title;
    private String author;
    private String genre;
    private String ISBN;

    // Constructor to initialize all fields
    public Book() {
        title = "";
        author = "";
        genre = "";
        ISBN = "";
    }
    
    // Getters for all fields
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public String getISBN() {
        return ISBN;
    }
    
    // Setters for all fields
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}
