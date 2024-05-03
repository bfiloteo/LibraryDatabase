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
    public static final String SQLTableName = "Books";
    public static final String SQLItemIDName = "BookID";
    public static final String mediaType = "book";
    public static final String searchPrompt = "Enter the book's title, author, or genre";
    public static final String backPrompt = "Back to Main Menu";
    public static final int WeeksToBorrow = 3;
    private int memberID = 0;
    private JTextField searchField;
    private JPanel allItemsPanel = null;
    ArrayList<Book> items;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public BooksPage(int memberID) {
        super(SQLTableName);
        this.memberID = memberID;
        createSQLConnection();
        items = new ArrayList<>();
        // Items Panel
        allItemsPanel = new JPanel();
        allItemsPanel.setLayout(new BoxLayout(allItemsPanel, BoxLayout.Y_AXIS));

        searchField = new JTextField(searchPrompt);
        searchField.setMinimumSize(new Dimension(200, searchField.getPreferredSize().height));

        JButton backButton = new JButton(backPrompt);
        JButton searchButton = new JButton("Search");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new DashboardPage(memberID); 
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality code here for when the user searches for a item
                createSQLQuery(searchField.getText());
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

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allItemsPanel);
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

    private void updateAllItemsPanel()
    {
        String[] header = {"Title", "Author", "Genre", "ISBN", "Copies Available", ""};
        allItemsPanel.removeAll();
        allItemsPanel.add(makePanel(header));

        // Add items into a list like view.
        for (Book item : items) {
            String[] labels = {item.getTitle(), 
                               item.getAuthor(), 
                               item.getGenre(), 
                               item.getISBN(), 
                               item.getAvailableCopies() + " of " + item.getTotalCopies(), 
                               item.getAvailableCopies() > 0 ? "Borrow" : "Unavailable"};
            if( item.getAvailableCopies() > 0)
                allItemsPanel.add(makePanelWithButton(labels, item));
            else
                // TODO: turn into Hold button
                allItemsPanel.add(makePanel(labels));
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

    private JPanel makePanelWithButton(String[] labels, Book item)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, labels.length));
        for(int i = 0; i < labels.length - 1; i++)
        {
            JLabel label = new JLabel(labels[i]);
            panel.add(label);
        }
        // Last grid is button
        JButton button = new JButton(labels[labels.length - 1]);
        button.addActionListener(new BorrowButtonActionListener(item));
        panel.add(button);

        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    // This class ensures that each button to the item will be its own button to the corresponding item,
    // so that when the user decides to rent the item,
    // the button to rent it will know what item the user chose.
    private class BorrowButtonActionListener implements ActionListener {
        private Book item;

        public BorrowButtonActionListener(Book item) {
            this.item = item;
        }

        // Code to add after the person borrows a item.
        @Override
        public void actionPerformed(ActionEvent e) {
            createSQLUpdate(item);
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
            conn = DriverManager.getConnection("jdbc:mysql://localhost/library?" + "user=root&password=123456");
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
    private void createSQLQuery(String searchText)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "SELECT " + SQLItemIDName + ", Title, Author, Genre, ISBN, TotalCopies, AvailableCopies FROM " + SQLTableName +
                            " WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Author LIKE \"%" + searchWords[0] + "%\" OR Genre LIKE \"%" + searchWords[0] + "%\"";
        for (int i = 1; i < searchWords.length; i++)
            stmtString += " OR Title LIKE \"%" + searchWords[i] + "%\" OR Author LIKE \"%" + searchWords[i] + "%\" OR Genre LIKE \"%" + searchWords[i] + "%\"";
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

                items.clear(); // remove previous search results
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
                    items.add(item);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
   }
   private void createSQLUpdate(Book item)
   {
       // Do SQL query to get number of available copies
       int availableCopies = 0;
       String stmtString = "SELECT AvailableCopies FROM " + SQLTableName + " WHERE " + SQLItemIDName + " = " + item.getItemID() + ";";
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

       // if there is an available copy, then update Items table to reduce available copies by 1
       // and update Transactions table to borrow the item
       if( availableCopies > 0)
       {
           availableCopies--;
           stmtString = "UPDATE " + SQLTableName + " SET AvailableCopies = " + availableCopies + " WHERE " + SQLItemIDName + " = " + item.getItemID() + ";";
           System.out.println(stmtString);
           try {
               stmt = conn.createStatement();
               stmt.execute(stmtString);
           }
           catch (SQLException ex) { handleSQLException(ex); }
           finally { releaseSQLResources(); }
           item.setAvailableCopies(availableCopies); // update item with decremented availableCopies
           updateAllItemsPanel();

           LocalDate transactionDate = LocalDate.now();
           LocalDate dueDate = LocalDate.now();
           dueDate.plusWeeks(WeeksToBorrow);
           Random rand = new Random(); // generate random number for transaction ID
           int transactionID = transactionDate.hashCode() + item.getItemID() + (memberID * 1024) + rand.nextInt();
           if( transactionID < 0 ) transactionID = -transactionID; // make transactionID positive
           // TODO check for collisions in Transactions table
           stmtString = "INSERT INTO Transactions (TransactionID, TransactionType, MediaType, TransactionDate, DueDate, MemberID, " + SQLItemIDName + ") VALUES (" +
                        transactionID + ", \"borrow\", \"" + mediaType + "\", \"" + transactionDate + "\", \"" + dueDate + "\", " + memberID + ", " + item.getItemID() + ");";
           System.out.println(stmtString);
           try {
               stmt = conn.createStatement();
               stmt.execute(stmtString);
           }
           catch (SQLException ex) { handleSQLException(ex); }
           finally { releaseSQLResources(); }
       }
       else
           System.out.println("Cannot borrow item " + item.getItemID() + " since available copies = " + item.getAvailableCopies());
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
