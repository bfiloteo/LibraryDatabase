package swing;
import library.*;

import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

public class MoviesPage extends JFrame {
    private JTextField searchField;
    private JPanel allMoviesPanel = null;
    ArrayList<Movie> movies;    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public MoviesPage() {
        super("Movies");
        createSQLConnection();
        movies = new ArrayList<>();

        // Movies Panel
        allMoviesPanel = new JPanel();
        allMoviesPanel.setLayout(new BoxLayout(allMoviesPanel, BoxLayout.Y_AXIS));

        searchField = new JTextField("Enter the movie's title or director...");
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
                CreateSqlQuery(searchField.getText());
                updateAllMoviesPanel();
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Enter the movie's title or director...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Enter the movie's title or director...");
                }
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allMoviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(1200, 1000);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateAllMoviesPanel()
    {
        allMoviesPanel.removeAll();

        // Add movies into a list like view.
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

        revalidate();
        repaint();
    }
    
    // This class ensures that each button to the book will be its own button to the corresponding book,
    // so that when the user decides to rent the book,
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
    
    // SQL connection
    private void createSQLConnection()
    {
        try
        {
            // The newInstance() call is a work around for some broken Java implementations.
            // default for running on local:
            //conn = DriverManager.getConnection("jdbc:mysql://localhost/<database name>?" +
            //"user=<username: may be root>&password=<password>");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/library?" + "user=root&password=329761");
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }
        catch (SQLException ex)
        {
            // handle the error
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch (Exception e)
        {
            System.out.println("Error." + e.getMessage());
        }
    }   

    // Search: SQL Query
    // Search call for using the search bar in movies page.
    private void CreateSqlQuery(String searchText)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "SELECT Title, Director, TotalCopies, AvailableCopies FROM Movies " + 
                            "WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Director LIKE \"%" + searchWords[0] + "%\"";
        for (int i = 1; i < searchWords.length; i++)
            stmtString += "OR Title LIKE \"%" + searchWords[i] + "%\" OR Director LIKE \"%" + searchWords[i] + "%\"";
        stmtString += ";";
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(stmtString);
        
            // Now do something with the ResultSet ....
            if (rs != null) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                System.out.println("Columns = " + cols);
                for (int i = 0; i < cols; i++) {
                    String name = md.getColumnLabel(i + 1);
                    System.out.print(name + "\t");
                }

                System.out.println("");
                movies.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");
                    Movie movie = new Movie();
                    // Column indexes must match order of SELECT query, starting from index 1
                    movie.setTitle(rs.getString(1));
                    movie.setDirector(rs.getString(2));
                    movie.setReleaseYear(rs.getInt(3));
                    movie.setAvailableCopies(rs.getInt(4));
                    movie.setTotalCopies(rs.getInt(cols));
                    movies.add(movie);
                }
            }
        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // Release resources in a finally{} block in reverse-order of their creation
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
        
                rs = null;
            }
        
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore
        
                stmt = null;
            }
        }
    }    
}