package library;

public class Movie extends Item {
    private String title;
    private String director;
    private int releaseYear;

    public Movie()
    {
        title = "";
        director = "";
        releaseYear = 0;
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

    // Setters
    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setDirector(String newDirector) {
        director = newDirector;
    }

    public void setReleaseYear(int newReleaseYear) {
        releaseYear = newReleaseYear;
    }
}
