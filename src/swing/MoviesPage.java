package swing;
import library.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.*;

public class MoviesPage extends JFrame {
    private JTextField searchField;

    public MoviesPage() {
        super("Movies");

        searchField = new JTextField("Enter the movie's title, director, or genre...");
        searchField.setMinimumSize(new Dimension(200, searchField.getPreferredSize().height));

        JButton backButton = new JButton("Back to Main Menu");
        JButton searchButton = new JButton("Search");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new DashboardPage(); 
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality code here for when the user searches for a movie
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Enter the article's title, author, or genre...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Enter the article's title, author, or genre...");
                }
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Movies Panel
        JPanel allMoviesPanel = new JPanel();
        allMoviesPanel.setLayout(new BoxLayout(allMoviesPanel, BoxLayout.Y_AXIS));

        // Placeholder for doing an sql query
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 15; i++) { 
            Movie movie = new Movie();
            movie.setTitle("Title: Placeholder Title: " + i);
            movie.setMovieID(i);
            movie.setDirector("Director: Placeholder Director");
            movie.setReleaseYear(i);
            movie.setAvailableCopies(i);
            movie.setTotalCopies(i);
            movies.add(movie);
        }

        // This is how you add a movie into a list like view. This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book
        for (Movie movie : movies) {
            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new GridLayout(6, 1));

            JLabel titleLabel = new JLabel(movie.getTitle());
            JLabel directorLabel = new JLabel(movie.getDirector());
            JLabel yearOfReleaseLabel = new JLabel("" + movie.getReleaseYear());
            JLabel availableCopiesLabel = new JLabel("" + movie.getAvailableCopies());
            JLabel totalCopiesLabel = new JLabel("" + movie.getTotalCopies());

            JButton rentButton = new JButton("Rent");
            rentButton.addActionListener(new RentButtonActionListener
            (titleLabel.getText(), directorLabel.getText(), 
            directorLabel.getText(), yearOfReleaseLabel.getText(), 
            totalCopiesLabel.getText(), availableCopiesLabel.getText()));

            moviePanel.add(titleLabel);
            moviePanel.add(directorLabel);
            moviePanel.add(yearOfReleaseLabel);
            moviePanel.add(totalCopiesLabel);
            moviePanel.add(availableCopiesLabel);
            moviePanel.add(rentButton);

            // Creates the black like between each movie
            moviePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allMoviesPanel.add(moviePanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allMoviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // This class ensures that each button to the book will be it's own button to the corresponding book, so that when the user decides to rent the book,
    // the button to rent it will know what book the user chose.
    private class RentButtonActionListener implements ActionListener {
        private String title;
        private String director;
        private String yearOfRelease;
        private String totalCopies;
        private String availableCopies;

        public RentButtonActionListener(String title, String director, String genre, String yearOfRelease, String totalCopies, String availableCopies) {
            this.title = title;
            this.director = director;
            this.yearOfRelease = yearOfRelease;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Code to add after the person rents a movie.
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Title: " + title);
        }
    }
}
