package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class LibraryPage extends JFrame {
    public static final String SQLTableName = "Library";
    public static final String searchPrompt = "Find media (article, book, movie) by title, author, director, or genre";
    public static final String backPrompt = "Back to Menu";
    private JTextField searchField;
    private int librarianID = 0;
    private JPanel allArticlesPanel = null;
    ArrayList<Article> articles;    
    private JPanel allBooksPanel = null;
    ArrayList<Book> books;
    private JPanel allMoviesPanel = null;
    ArrayList<Movie> movies;    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public LibraryPage(int librarianID) {
        super(SQLTableName);
        this.librarianID = librarianID;
        createSQLConnection();

        // Articles Panel
        articles = new ArrayList<>();
        allArticlesPanel = new JPanel();
        allArticlesPanel.setLayout(new BoxLayout(allArticlesPanel, BoxLayout.Y_AXIS));
        // Books Panel
        books = new ArrayList<>();
        allBooksPanel = new JPanel();
        allBooksPanel.setLayout(new BoxLayout(allBooksPanel, BoxLayout.Y_AXIS));
        // Movies Panel
        movies = new ArrayList<>();
        allMoviesPanel = new JPanel();
        allMoviesPanel.setLayout(new BoxLayout(allMoviesPanel, BoxLayout.Y_AXIS));

        searchField = new JTextField(searchPrompt);
        searchField.setMinimumSize(new Dimension(200, searchField.getPreferredSize().height));

        JButton backButton = new JButton(backPrompt);
        JButton searchButton = new JButton("Search");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LibrarianPage(librarianID); 
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality code here for when the librarian searches for something in the library
                createSQLQuery(searchField.getText(), "article");
                createSQLQuery(searchField.getText(), "book");
                createSQLQuery(searchField.getText(), "movie");
                updateAllItemsPanel();
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(searchPrompt)) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(searchPrompt);
                }
            }
        });

        JButton addHoldingButton = new JButton("Add Holding");
        addHoldingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddContentPage(); 
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // All things in the library Panel
        JPanel libraryPanel = new JPanel();
        libraryPanel.setLayout(new BoxLayout(libraryPanel, BoxLayout.Y_AXIS));
        libraryPanel.add(allArticlesPanel);
        libraryPanel.add(allBooksPanel);
        libraryPanel.add(allMoviesPanel);
        
        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(libraryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(addHoldingButton, BorderLayout.SOUTH);
    
        setLocationRelativeTo(null); // center ui on screen
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateAllItemsPanel()
    {
        updateAllArticlesPanel();
        updateAllBooksPanel();
        updateAllMoviesPanel();
    }
    private void updateAllArticlesPanel()
    {
        String[] header = {"Title", "Author", "Volume", "Issue", "Copies Available", "", ""};
        allArticlesPanel.removeAll();
        allArticlesPanel.add(makePanel(header));

        // Add articles into a list like view.
        for (Article article : articles) { 
            String[] labels = {article.getTitle(), 
                               article.getAuthor(), 
                               "" + article.getVolume(), 
                               article.getIssue(), 
                               article.getAvailableCopies() + " of " + article.getTotalCopies(), 
                               "Add copies",
                               "Remove copies"};
            allArticlesPanel.add(makePanelWithButton(labels, article, "article"));
        }

        revalidate();
        repaint();
    }

    private void updateAllBooksPanel()
    {
        String[] header = {"Title", "Author", "Genre", "ISBN", "Copies Available", "", ""};
        allBooksPanel.removeAll();
        allBooksPanel.add(makePanel(header));

        // Add books into a list like view.
        for (Book book : books) {
            String[] labels = {book.getTitle(), 
                               book.getAuthor(), 
                               book.getGenre(), 
                               book.getISBN(), 
                               book.getAvailableCopies() + " of " + book.getTotalCopies(), 
                               "Add copies",
                               "Remove copies"};
            allBooksPanel.add(makePanelWithButton(labels, book, "book"));
        }

        revalidate();
        repaint();
    }
    
    private void updateAllMoviesPanel()
    {
        String[] header = {"Title", "Director", "Release Year", "Copies Available", "", ""};
        allMoviesPanel.removeAll();
        allMoviesPanel.add(makePanel(header));

        // Add movies into a list like view.
        for (Movie movie : movies) {
            String[] labels = {movie.getTitle(), 
                               movie.getDirector(), 
                               "" + movie.getReleaseYear(), 
                               movie.getAvailableCopies() + " of " + movie.getTotalCopies(), 
                               "Add copies",
                               "Remove copies"};
            allMoviesPanel.add(makePanelWithButton(labels, movie, "movie"));
        }

        revalidate();
        repaint();
    }
    
    private JPanel makePanel(String[] labels)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, labels.length));
        for(int i = 0; i < labels.length; i++)
        {
            JLabel label = new JLabel(labels[i]);
            panel.add(label);
        }
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    private JPanel makePanelWithButton(String[] labels, Item item, String mediaType)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, labels.length));
        for(int i = 0; i < labels.length - 2; i++)
        {
            JLabel label = new JLabel(labels[i]);
            panel.add(label);
        }
        // Last two are add and remove buttons
        JButton addButton = new JButton(labels[labels.length - 2]);
        addButton.addActionListener(new AddCopiesActionListener(item, mediaType));
        panel.add(addButton);
        JButton removeButton = new JButton(labels[labels.length - 1]);
        removeButton.addActionListener(new RemoveCopiesActionListener(item, mediaType));
        panel.add(removeButton);

        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    // This class ensures that each button to the item will be it's own button to the corresponding book,
    // so that when the librarian decides to add copies to the item,
    // it will only add more copies to the specific item.

    private class AddCopiesActionListener implements ActionListener {
        private Item item;
        private String mediaType;

        public AddCopiesActionListener(Item item, String mediaType) {
            this.item = item;
            this.mediaType = mediaType;
        }

        // Code to add when adding copies to an item
        // referenced from https://stackoverflow.com/questions/8852560/how-to-make-popup-window-in-java
        @Override
        public void actionPerformed(ActionEvent e) {
            String num = JOptionPane.showInputDialog(LibraryPage.this, "How many copies do you want to add?", null);

            try {
                int number = Integer.parseInt(num);
                createSQLUpdate(item, mediaType, number);
                JOptionPane.showMessageDialog(LibraryPage.this, number +  " copies have been added", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LibraryPage.this, "Invalid Number. Please Enter an Integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(LibraryPage.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // This class ensures that each button to the book will be it's own button to the corresponding book,
    // so that when the librarian decides to remove copies to the book,
    // it will only remove more copies to the specific book.
    private class RemoveCopiesActionListener implements ActionListener {
        private Item item;
        private String mediaType;

        public RemoveCopiesActionListener(Item item, String mediaType) {
            this.item = item;
            this.mediaType = mediaType;
        }

        // Code to add when removing copies to a book.
        // Ideally if we choose to remove all copies, this would be removed from the library.
        @Override
        public void actionPerformed(ActionEvent e) {
            String num = JOptionPane.showInputDialog(LibraryPage.this, "How many copies do you want to remove from?", null);

            try {
                int number = Integer.parseInt(num);
                createSQLUpdate(item, mediaType, -number);
                JOptionPane.showMessageDialog(LibraryPage.this, number +  " copies have been removed", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LibraryPage.this, "Invalid Number. Please Enter an Integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(LibraryPage.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception e)
        {
            System.out.println("Error." + e.getMessage());
        }
    }   

    // Search: SQL Query
    // Search call for using the search bar in items page.
    private void createSQLQuery(String searchText, String mediaType)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "";
        if(mediaType.equals("article"))
        {
            stmtString = "SELECT ArticleID, Title, Author, Volume, Issue, TotalCopies, AvailableCopies FROM Articles" + 
                         " WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Author LIKE \"%" + searchWords[0] + "%\"";
            for (int i = 1; i < searchWords.length; i++)
                stmtString += " OR Title LIKE \"%" + searchWords[i] + "%\" OR Author LIKE \"%" + searchWords[i] + "%\"";
        }
        else if(mediaType.equals("book"))
        {
            stmtString = "SELECT BookID, Title, Author, Genre, ISBN, TotalCopies, AvailableCopies FROM Books" + 
                         " WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Author LIKE \"%" + searchWords[0] + "%\" OR Genre LIKE \"%" + searchWords[0] + "%\"";
            for (int i = 1; i < searchWords.length; i++)
                stmtString += " OR Title LIKE \"%" + searchWords[i] + "%\" OR Author LIKE \"%" + searchWords[i] + "%\" OR Genre LIKE \"%" + searchWords[i] + "%\"";
        }
        else if(mediaType.equals("movie"))
        {
            stmtString = "SELECT MovieID, Title, Director, ReleaseYear, TotalCopies, AvailableCopies FROM Movies" + 
                         " WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Director LIKE \"%" + searchWords[0] + "%\"";
            for (int i = 1; i < searchWords.length; i++)
                stmtString += "OR Title LIKE \"%" + searchWords[i] + "%\" OR Director LIKE \"%" + searchWords[i] + "%\"";
        }
        else
            System.out.println("Unknown media type " + mediaType);
        stmtString += ";";
        System.out.println(stmtString);
        
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

                if(mediaType.equals("article"))
                {
                    articles.clear(); // remove previous search results
                    while (rs.next()) {
                        System.out.print("Row\t");
                        for (int i = 0; i < cols; i++) {
                            String value = rs.getString(i + 1);
                            System.out.print(value + "\t");
                        }
                        System.out.println("");
                        Article item = new Article();
                        // Column indexes must match order of SELECT query, starting from index 1
                        int index = 1;
                        item.setItemID(rs.getInt(index++));
                        item.setAuthor(rs.getString(index++));
                        item.setTitle(rs.getString(index++));
                        item.setVolume(rs.getInt(index++));
                        item.setIssue(rs.getString(index++));
                        item.setTotalCopies(rs.getInt(index++));
                        item.setAvailableCopies(rs.getInt(index++));
                        articles.add(item);
                    }
                }
                else if(mediaType.equals("book"))
                {
                    books.clear(); // remove previous search results
                    while (rs.next()) {
                        System.out.print("Row\t");
                        for (int i = 0; i < cols; i++) {
                            String value = rs.getString(i + 1);
                            System.out.print(value + "\t");
                        }
                        System.out.println("");
                        Book item = new Book();
                        // Column indexes must match order of SELECT query, starting from index 1
                        int index = 1;
                        item.setItemID(rs.getInt(index++));
                        item.setTitle(rs.getString(index++));
                        item.setAuthor(rs.getString(index++));
                        item.setGenre(rs.getString(index++));
                        item.setISBN(rs.getString(index++));
                        item.setTotalCopies(rs.getInt(index++));
                        item.setAvailableCopies(rs.getInt(index++));
                        books.add(item);
                    }
                }
                else if(mediaType.equals("movie"))
                {
                    movies.clear(); // remove previous search results
                    while (rs.next()) {
                        System.out.print("Row\t");
                        for (int i = 0; i < cols; i++) {
                            String value = rs.getString(i + 1);
                            System.out.print(value + "\t");
                        }
                        System.out.println("");
                        Movie item = new Movie();
                        // Column indexes must match order of SELECT query, starting from index 1
                        int index = 1;
                        item.setItemID(rs.getInt(index++));
                        item.setTitle(rs.getString(index++));
                        item.setDirector(rs.getString(index++));
                        item.setReleaseYear(rs.getInt(index++));
                        item.setAvailableCopies(rs.getInt(index++));
                        item.setTotalCopies(rs.getInt(index++));
                        movies.add(item);
                    }    
                }
                else
                    System.out.println("Unknown media type " + mediaType);
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
    }

    // item - article, book, or movie
    // mediaType - "article", "book", "movie"
    // num - change in available copies requested (positive to add, negative to remove)
    private void createSQLUpdate(Item item, String mediaType, int num)
    {
        // Do SQL query to get number of available copies
        int availableCopies = 0;
        int totalCopies = 0;
        String capMediaType = mediaType.substring(0,1).toUpperCase() + mediaType.substring(1);
        String itemIDName = capMediaType + "ID";
        String tableName =  capMediaType + "s";

        String stmtString = "SELECT AvailableCopies, TotalCopies FROM " + tableName + " WHERE " + itemIDName + " = " + item.getItemID() + ";";
        System.out.println(stmtString);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(stmtString);
        
            // Now do something with the ResultSet ....
            if (rs != null) {
                while (rs.next()) {
                    availableCopies = rs.getInt(1);
                    totalCopies = rs.getInt(2);
                    System.out.println("Available Copies =\t" + availableCopies);
                    System.out.println("Total Copies =\t" + totalCopies);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }

        if(num == 0)
            throw new IllegalArgumentException("Invalid request: no change in totalCopies");
        else if(num < 0 && -num > totalCopies)
            throw new IllegalArgumentException("Invalid request: requested removal of " + (-num) + " copies, but there are only " + totalCopies + " copies ");
        else
        {
            // Update the appropriate SQL table with new totalCopies
            totalCopies += num;
            availableCopies += num;
            if(availableCopies < 0) availableCopies = 0; // if removing more copies than availableCopies, then set to 0
            stmtString = "UPDATE " + tableName + " SET AvailableCopies = " + availableCopies + ", TotalCopies = " + totalCopies + " WHERE " + itemIDName + " = " + item.getItemID() + ";";
            System.out.println(stmtString);
            try {
                stmt = conn.createStatement();
                stmt.execute(stmtString);
            }
            catch (SQLException ex) { handleSQLException(ex); }
            finally { releaseSQLResources(); }
            item.setAvailableCopies(availableCopies); // update item with new totalCopies
            item.setTotalCopies(totalCopies); // update item with new totalCopies
            updateAllItemsPanel();
        }
    }

    private void handleSQLException(SQLException ex)
    {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    private void releaseSQLResources()
    {
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
