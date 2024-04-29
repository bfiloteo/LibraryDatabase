package library;

public class Movie {
    private int movieID;
    private String title;
    private String director;
    private int releaseYear;
    private int availableCopies;
    private int totalCopies;

    public Movie()
    {
        movieID = 0;
        title = "";
        director = "";
        releaseYear = 0;
        availableCopies = 0;
        totalCopies = 0;
    }

    public int getMovieID()
    {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    // Setters
    public void setMovieID(int movieID)
    {
        this.movieID = movieID;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setDirector(String newDirector) {
        director = newDirector;
    }

    public void setReleaseYear(int newReleaseYear) {
        releaseYear = newReleaseYear;
    }

    public void setAvailableCopies(int newAvailableCopies) {
        availableCopies = newAvailableCopies;
    }

    public void setTotalCopies(int newTotalCopies) {
        totalCopies = newTotalCopies;
    }

}
