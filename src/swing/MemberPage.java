package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class MemberPage extends JFrame {
    public static final String SQLPassword = "329761";
    public static final String backPrompt = "Back to Main Menu";
    private JPanel allLoansPanel = null;
    Member member;
    ArrayList<Transaction> loans; // all outstanding borrowed media
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public MemberPage(int memberID) {
        super("Account Info");
        createSQLConnection();
        loans = new ArrayList<>();
        JButton backButton = new JButton(backPrompt);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new DashboardPage(memberID); 
            }
        });

        // Get member from memberID
        member = createMemberSQLQuery(memberID);
        JLabel firstNameLabel = new JLabel(member.getFirstName());
        JLabel lastNameLabel = new JLabel(member.getLastName());
        JLabel emailLabel = new JLabel(member.getEmail());
        
        JPanel accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new GridLayout(1, 4));

        accountInfoPanel.add(backButton);
        accountInfoPanel.add(firstNameLabel);
        accountInfoPanel.add(lastNameLabel);
        accountInfoPanel.add(emailLabel);
        
        // Loans Panel (articles, books, movies)
        allLoansPanel = new JPanel();
        allLoansPanel.setLayout(new BoxLayout(allLoansPanel, BoxLayout.Y_AXIS));
        createSQLQuery();
        updateAllLoansPanel();

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allLoansPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(accountInfoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); 

        setSize(750, 500);
        setResizable(true);
        setLocationRelativeTo(null); // center ui on screen
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateAllLoansPanel()
    {
        String[] header = {"Title", "Media", "Borrow Date", "Due Date", ""};
        allLoansPanel.removeAll();
        allLoansPanel.add(makePanel(header));

        for (Transaction loan : loans) { 
            String[] labels = {loan.getTitle(), 
                               loan.getMediaType(), 
                               loan.getTransactionDate().toString(), 
                               loan.getDueDate().toString(),
                               "Return"};
            allLoansPanel.add(makePanelWithButton(labels, loan));
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

    private JPanel makePanelWithButton(String[] labels, Transaction loan)
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
        button.addActionListener(new ReturnButtonActionListener(loan));
        panel.add(button);

        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    private class ReturnButtonActionListener implements ActionListener {
        private Transaction transaction;

        public ReturnButtonActionListener(Transaction transaction) {
            this.transaction = transaction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            createSQLUpdate(transaction);
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
            conn = DriverManager.getConnection("jdbc:mysql://localhost/library?" + "user=root&password=" + SQLPassword);
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception e)
        {
            System.out.println("Error." + e.getMessage());
        }
    }

    private Member createMemberSQLQuery(int memberID)
    {
        String stmtString = "SELECT FirstName, LastName, Email FROM Members WHERE MemberID = " + memberID + ";";
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

                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");

                    Member member = new Member();
                    // Column indexes must match order of SELECT query, starting from index 1
                    member.setMemberID(memberID);
                    member.setFirstName(rs.getString(1));
                    member.setLastName(rs.getString(2));
                    member.setEmail(rs.getString(3));
                    releaseSQLResources();
                    return member;
                }
                System.out.println("Error: did not find memberID = " + memberID + " in Members table");
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception ex) { System.out.println(ex.getMessage());}
        finally { releaseSQLResources(); }
        return null;
    }
    // Search: SQL Query
    // Find all transactions for given member.
    private void createSQLQuery()
    {
        String stmtString = "SELECT TransactionID, Title, TransactionType, MediaType, TransactionDate, DueDate, Transactions.ArticleID, Transactions.BookID, Transactions.MovieID " + 
                            "FROM Transactions INNER JOIN Articles ON Transactions.ArticleID = Articles.ArticleID " + 
                            "WHERE MemberID = " + member.getMemberID() + " UNION " +
                            "SELECT TransactionID, Title, TransactionType, MediaType, TransactionDate, DueDate, Transactions.ArticleID, Transactions.BookID, Transactions.MovieID " + 
                            "FROM Transactions INNER JOIN Books ON Transactions.BookID = Books.BookID " + 
                            "WHERE MemberID = " + member.getMemberID() + " UNION " +
                            "SELECT TransactionID, Title, TransactionType, MediaType, TransactionDate, DueDate, Transactions.ArticleID, Transactions.BookID, Transactions.MovieID " + 
                            "FROM Transactions INNER JOIN Movies ON Transactions.MovieID = Movies.MovieID " + 
                            "WHERE MemberID = " + member.getMemberID() + " ORDER BY TransactionDate ASC;";
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

                loans.clear(); // remove previous search results
                ArrayList<Transaction> returns = new ArrayList<>();
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");

                    // if borrow transaction type
                    if(rs.getString(3).equals("borrow"))
                    {
                        // then add to loans list
                        Transaction transaction = new Transaction();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        // Column indexes must match order of SELECT query, starting from index 1
                        transaction.setTransactionID(rs.getInt(1));
                        transaction.setTitle(rs.getString(2));
                        transaction.setTransactionType(rs.getString(3));
                        transaction.setMediaType(rs.getString(4));
                        transaction.setTransactionDate(formatter.parse(rs.getString(5)));
                        transaction.setDueDate(formatter.parse(rs.getString(6)));
                        if(transaction.getMediaType().equals("article"))
                            transaction.setArticleID(rs.getInt(7));
                        else if(transaction.getMediaType().equals("book"))
                            transaction.setBookID(rs.getInt(8));
                        else if(transaction.getMediaType().equals("movie"))
                            transaction.setMovieID(rs.getInt(9));
                        else
                            System.out.println("Unknown media type " + transaction.getMediaType());
                        transaction.setMemberID(member.getMemberID());
                        loans.add(transaction);
                    }
                    else // must be return
                    {
                        // then add to returns list
                        Transaction transaction = new Transaction();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        // Column indexes must match order of SELECT query, starting from index 1
                        transaction.setTransactionID(rs.getInt(1));
                        transaction.setTitle(rs.getString(2));
                        transaction.setTransactionType(rs.getString(3));
                        transaction.setMediaType(rs.getString(4));
                        transaction.setTransactionDate(formatter.parse(rs.getString(5)));
                        transaction.setDueDate(transaction.getTransactionDate()); // no due date for returns, so just use transaction date
                        if(transaction.getMediaType().equals("article"))
                            transaction.setArticleID(rs.getInt(7));
                        else if(transaction.getMediaType().equals("book"))
                            transaction.setBookID(rs.getInt(8));
                        else if(transaction.getMediaType().equals("movie"))
                            transaction.setMovieID(rs.getInt(9));
                        else
                            System.out.println("Unknown media type " + transaction.getMediaType());
                        transaction.setMemberID(member.getMemberID());
                        returns.add(transaction);
                    }
                }
                // Go through each of the returns to find matching borrow transaction and remove from loans list
                for(Transaction transaction : returns)
                {
                    System.out.println("Loans = " + loans.size() + "; Returns = " + returns.size());
                    removeBorrowedMedia(transaction.getMediaType(), transaction.getArticleID(), transaction.getBookID(), transaction.getMovieID());
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception ex) { System.out.println(ex.getMessage());}
        finally { releaseSQLResources(); }
    }

    private void removeBorrowedMedia(String mediaType, int articleID, int bookID, int movieID)
    {
        if(mediaType.equals("article"))
        {
            int i = loans.size() - 1;
            while( i>= 0 && loans.get(i).getArticleID() != articleID)
                i--;
            if(i >= 0)
                loans.remove(i);
            else
                System.out.println("Cannot find matching borrow transaction for articleID = " + articleID);
        }
        else if(mediaType.equals("book"))
        {
            int i = loans.size() - 1;
            while( i>= 0 && loans.get(i).getBookID() != bookID)
                i--;
            if(i >= 0)
                loans.remove(i);
            else
                System.out.println("Cannot find matching borrow transaction for bookID = " + bookID);
        }
        else if(mediaType.equals("movie"))
        {
            int i = loans.size() - 1;
            while( i>= 0 && loans.get(i).getMovieID() != movieID)
                i--;
            if(i >= 0)
                loans.remove(i);
            else
                System.out.println("Cannot find matching borrow transaction for movieID = " + movieID);
        }
        else
            System.out.println("Unknown media type " + mediaType);

    }

    private void createSQLUpdate(Transaction transaction)
    {
        // Do SQL query to get number of available copies
        int availableCopies = 0;
        int totalCopies = 0;
        String stmtString = "";
        if(transaction.getMediaType().equals("article")) {
            stmtString = "SELECT AvailableCopies, TotalCopies FROM Articles WHERE ArticleID = " + transaction.getArticleID() + ";";
        }
        else if(transaction.getMediaType().equals("book")) {
            stmtString = "SELECT AvailableCopies, TotalCopies FROM Books WHERE BookID = " + transaction.getBookID() + ";";
        }
        else if(transaction.getMediaType().equals("movie")) {
            stmtString = "SELECT AvailableCopies, TotalCopies FROM Movies WHERE MovieID = " + transaction.getMovieID() + ";";
        }
        else {
            System.out.println("Unknown media type " + transaction.getMediaType());
        }
        System.out.println(stmtString);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(stmtString);
        
            // Now do something with the ResultSet ....
            if (rs != null) {
                while (rs.next()) {
                    availableCopies = rs.getInt(1);
                    System.out.println("Available Copies =\t" + availableCopies);
                    totalCopies = rs.getInt(2);
                    System.out.println("Total Copies =\t" + totalCopies);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }

        if( availableCopies < totalCopies)
        {
            // update media table to increase available copies by 1
            // and update Transactions table to return the media
            availableCopies++;
            if(transaction.getMediaType().equals("article")) {
                stmtString = "UPDATE Articles SET AvailableCopies = " + availableCopies + " WHERE ArticleID = " + transaction.getArticleID() + ";";
            }
            else if(transaction.getMediaType().equals("book")) {
                stmtString = "UPDATE Books SET AvailableCopies = " + availableCopies + " WHERE BookID = " + transaction.getBookID() + ";";
            }
            else if(transaction.getMediaType().equals("movie")) {
                stmtString = "UPDATE Movies SET AvailableCopies = " + availableCopies + " WHERE MovieID = " + transaction.getMovieID() + ";";
            }
            else {
                System.out.println("Unknown media type " + transaction.getMediaType());
            }
            System.out.println(stmtString);
            try {
                stmt = conn.createStatement();
                stmt.execute(stmtString);
            }
            catch (SQLException ex) { handleSQLException(ex); }
            finally { releaseSQLResources(); }
            removeBorrowedMedia(transaction.getMediaType(), transaction.getArticleID(), transaction.getBookID(), transaction.getMovieID());
            updateAllLoansPanel();
            LocalDate transactionDate = LocalDate.now();
            Random rand = new Random(); // generate random number for transaction ID
            int transactionID = transactionDate.hashCode() + (transaction.getMemberID() * 1024) + rand.nextInt();
            if( transactionID < 0 ) transactionID = -transactionID; // make transactionID positive
            // TODO check for collisions in Transactions table
            if(transaction.getMediaType().equals("article")) {
                stmtString = "INSERT INTO Transactions (TransactionID, TransactionType, MediaType, TransactionDate, MemberID, ArticleID) VALUES (" +
                            transactionID + ", \"return\", \"article\", \"" + transactionDate + "\", " + transaction.getMemberID() + ", " + transaction.getArticleID() + ");";
            }
            else if(transaction.getMediaType().equals("book")) {
                stmtString = "INSERT INTO Transactions (TransactionID, TransactionType, MediaType, TransactionDate, MemberID, BookID) VALUES (" +
                            transactionID + ", \"return\", \"book\", \"" + transactionDate + "\", " + transaction.getMemberID() + ", " + transaction.getBookID() + ");";
            }
            else if(transaction.getMediaType().equals("movie")) {
                stmtString = "INSERT INTO Transactions (TransactionID, TransactionType, MediaType, TransactionDate, MemberID, MovieID) VALUES (" +
                            transactionID + ", \"return\", \"movie\", \"" + transactionDate + "\", " + transaction.getMemberID() + ", " + transaction.getMovieID() + ");";
            }
            else {
                System.out.println("Unknown media type " + transaction.getMediaType());
            }
            System.out.println(stmtString);
            try {
                stmt = conn.createStatement();
                stmt.execute(stmtString);
            }
            catch (SQLException ex) { handleSQLException(ex); }
            finally { releaseSQLResources(); }
        }
        else
            System.out.println("Cannot return media, availableCopies = " + availableCopies + " >= totalCopies = " + totalCopies);

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
