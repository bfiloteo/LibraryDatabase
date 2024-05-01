package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.time.LocalDate;
import java.sql.*;

public class BooksPage extends JFrame {
    public static final String searchPrompt = "Enter the book's title, author, or genre";
    public static final String backPrompt = "Back to Main Menu";
    public static final int WeeksToBorrow = 3;
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

        searchField = new JTextField(searchPrompt);
        searchField.setMinimumSize(new Dimension(200, searchField.getPreferredSize().height));

        JButton backButton = new JButton(backPrompt);
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
                CreateSQLQuery(searchField.getText());
                updateAllBooksPanel();
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

        setSize(750, 500);
        setResizable(true);
        setLocationRelativeTo(null); // center ui on screen
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateAllBooksPanel()
    {
        allBooksPanel.removeAll();

        // Add books into a list like view.
        for (Book book : books) {
            JPanel bookPanel = new JPanel();
            bookPanel.setLayout(new GridLayout(1, 7));

            JLabel titleLabel = new JLabel(book.getTitle());
            JLabel authorLabel = new JLabel(book.getAuthor());
            JLabel isbnLabel = new JLabel("" + book.getISBN());
            JLabel genreLabel = new JLabel(book.getGenre());
            JLabel availableCopiesLabel = new JLabel("" + book.getAvailableCopies());
            JLabel totalCopiesLabel = new JLabel("" + book.getTotalCopies());
            
            JButton borrowButton = new JButton("Borrow");
            borrowButton.addActionListener(new BorrowButtonActionListener(book));

            bookPanel.add(titleLabel);
            bookPanel.add(authorLabel);
            bookPanel.add(isbnLabel);
            bookPanel.add(genreLabel);
            bookPanel.add(totalCopiesLabel);
            bookPanel.add(availableCopiesLabel);
            bookPanel.add(borrowButton);

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
    private class BorrowButtonActionListener implements ActionListener {
        private Book book;

        public BorrowButtonActionListener(Book book) {
            this.book = book;
        }

        // Code to add after the person borrows a book.
        @Override
        public void actionPerformed(ActionEvent e) {
            createSQLUpdate(book);
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
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception e)
        {
            System.out.println("Error." + e.getMessage());
        }
    }   

    // Search: SQL Query
    // Search call for using the search bar in books page.
    private void CreateSQLQuery(String searchText)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "SELECT BookID, Author, Title, Genre, TotalCopies, AvailableCopies FROM Books " + 
                            "WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Author LIKE \"%" + searchWords[0] + "%\" OR Genre LIKE \"%" + searchWords[0] + "%\"";
        for (int i = 1; i < searchWords.length; i++)
            stmtString += "OR Title LIKE \"%" + searchWords[i] + "%\" OR Author LIKE \"%" + searchWords[i] + "%\" OR Genre LIKE \"%" + searchWords[i] + "%\"";
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

                books.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");
                    Book book = new Book();
                    // Column indexes must match order of SELECT query, starting from index 1
                    book.setBookID(rs.getInt(1));
                    book.setTitle(rs.getString(2));
                    book.setAuthor(rs.getString(3));
                    book.setGenre(rs.getString(4));
                    book.setTotalCopies(rs.getInt(5));
                    book.setAvailableCopies(rs.getInt(6));
                    books.add(book);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
   }
   private void createSQLUpdate(Book book)
   {
       // Do SQL query to get number of available copies
       int availableCopies = 0;
       String stmtString = "SELECT AvailableCopies FROM Books WHERE BookID = " + book.getBookID() + ";";
       System.out.println(stmtString);
       try {
           stmt = conn.createStatement();
           rs = stmt.executeQuery(stmtString);
       
           // Now do something with the ResultSet ....
           if (rs != null) {
               while (rs.next()) {
                   availableCopies = rs.getInt(1);
                   System.out.println("Available Copies =\t" + availableCopies);
               }
           }
       }
       catch (SQLException ex) { handleSQLException(ex); }
       finally { releaseSQLResources(); }

       // if there is an available copy, then update Books table to reduce available copies by 1
       // and update Transactions table to borrow the book
       if( availableCopies > 0)
       {
           availableCopies--;
           stmtString = "UPDATE Books SET AvailableCopies = " + availableCopies + " WHERE BookID = " + book.getBookID() + ";";
           System.out.println(stmtString);
           try {
               stmt = conn.createStatement();
               stmt.execute(stmtString);
           }
           catch (SQLException ex) { handleSQLException(ex); }
           finally { releaseSQLResources(); }
           book.setAvailableCopies(availableCopies); // update book with decremented availableCopies
           updateAllBooksPanel();

           LocalDate transactionDate = LocalDate.now();
           LocalDate dueDate = LocalDate.now();
           dueDate.plusWeeks(WeeksToBorrow);
           int memberID = 1; // TODO placeholder
           Random rand = new Random(); // generate random number for transaction ID
           int transactionID = transactionDate.hashCode() + book.getBookID() + (memberID * 1024) + rand.nextInt();
           if( transactionID < 0 ) transactionID = -transactionID; // make transactionID positive
           // TODO check for collisions in Transactions table
           stmtString = "INSERT INTO Transactions (TransactionID, TransactionType, MediaType, TransactionDate, DueDate, MemberID, BookID) VALUES (" +
                        transactionID + ", \"borrow\", \"book\", \"" + transactionDate + "\", \"" + dueDate + "\", " + memberID + ", " + book.getBookID() + ");";
           System.out.println(stmtString);
           try {
               stmt = conn.createStatement();
               stmt.execute(stmtString);
           }
           catch (SQLException ex) { handleSQLException(ex); }
           finally { releaseSQLResources(); }
       }
       // TODO else display error message indicating no available copies to borrow
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
