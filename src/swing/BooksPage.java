package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.sql.*;

public class BooksPage extends JFrame {
    private JTextField searchField;
    private JPanel allBooksPanel = null;
    ArrayList<Book> books;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public BooksPage() {
        super("Books");
        createSQLConnection();
        books = new ArrayList<>();

        // Books Panel
        allBooksPanel = new JPanel();
        allBooksPanel.setLayout(new BoxLayout(allBooksPanel, BoxLayout.Y_AXIS));

        searchField = new JTextField("Enter the book's title, author, or genre...");
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
                // Add functionality code here for when the user searches for a book
                CreateSqlQuery(searchField.getText());
                updateAllBooksPanel();
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Enter the books title, author, or genre...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Enter the books title, author, or genre...");
                }
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allBooksPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateAllBooksPanel()
    {
        allBooksPanel.removeAll();

        // This is how you add a book into a list like view.
        // This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book
        for (Book book : books) {
            JPanel bookPanel = new JPanel();
            bookPanel.setLayout(new GridLayout(6, 1));

            JLabel titleLabel = new JLabel(book.getTitle());
            JLabel authorLabel = new JLabel(book.getAuthor());
            JLabel isbnLabel = new JLabel("" + book.getISBN());
            JLabel genreLabel = new JLabel(book.getGenre());
            JLabel availableCopiesLabel = new JLabel("" + book.getAvailableCopies());
            JLabel totalCopiesLabel = new JLabel("" + book.getTotalCopies());
            
            JButton rentButton = new JButton("Rent");
            rentButton.addActionListener(new RentButtonActionListener(titleLabel.getText(), authorLabel.getText(), 
            isbnLabel.getText(), genreLabel.getText(), totalCopiesLabel.getText(), availableCopiesLabel.getText()));

            bookPanel.add(titleLabel);
            bookPanel.add(authorLabel);
            bookPanel.add(isbnLabel);
            bookPanel.add(genreLabel);
            bookPanel.add(totalCopiesLabel);
            bookPanel.add(availableCopiesLabel);
            bookPanel.add(rentButton);

            // Creates the black like between each book
            bookPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allBooksPanel.add(bookPanel);
        }

        revalidate();
        repaint();
    }
    
    // This class ensures that each button to the book will be its own button to the corresponding book,
    // so that when the user decides to rent the book,
    // the button to rent it will know what book the user chose.
    private class RentButtonActionListener implements ActionListener {
        private String title;
        private String author;
        private String isbn;
        private String genre;
        private String totalCopies;
        private String availableCopies;

        public RentButtonActionListener(String title, String author, String isbn, String genre, String totalCopies, String availableCopies) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.genre = genre;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Code to add after the person rents a book.
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Title: " + title);
        }
    }

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
    // Search call for using the search bar in articles page.
    private void CreateSqlQuery(String searchText)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "SELECT Author, Title, Genre, TotalCopies, AvailableCopies FROM Books " + 
                            "WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Author LIKE \"%" + searchWords[0] + "%\" OR Genre LIKE \"%" + searchWords[0] + "%\"";
        for (int i = 1; i < searchWords.length; i++)
            stmtString += "OR Title LIKE \"%" + searchWords[i] + "%\" OR Author LIKE \"%" + searchWords[i] + "%\" OR Genre LIKE \"%" + searchWords[i] + "%\"";
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
                books.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");
                    // Placeholder code for SQL query
                    Book book = new Book();
                    book.setTitle(rs.getString(1));
                    book.setAuthor(rs.getString(2));
                    book.setGenre(rs.getString(3));
                    book.setTotalCopies(rs.getInt(4));
                    book.setAvailableCopies(rs.getInt(5));
                    books.add(book);
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
