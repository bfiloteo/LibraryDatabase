package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class TransactionPage extends JFrame {
    public static final String closePrompt = "Close window";
    private JPanel allTransactionsPanel = null;
    Member member;
    ArrayList<Transaction> transactions; // all transactions
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public TransactionPage(int memberID) {
        super("Your Transactions"); // Ideally change this to display the first name of the member
        createSQLConnection();
        transactions = new ArrayList<>();
        JButton closeButton = new JButton(closePrompt);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        // Get member from memberID
        member = createMemberSQLQuery(memberID);

        // Search Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(closeButton, BorderLayout.WEST);

        // Transaction Panel
        allTransactionsPanel = new JPanel();
        allTransactionsPanel.setLayout(new BoxLayout(allTransactionsPanel, BoxLayout.Y_AXIS));
        createSQLQuery();
        updateAllTransactionsPanel();

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allTransactionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(750, 500);
        setResizable(true);
        setLocationRelativeTo(null); // center ui on screen
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    private void updateAllTransactionsPanel()
    {
        allTransactionsPanel.removeAll();
        // if there are no transactions, then print out an info message
        if(transactions.size() == 0)
        {
            JLabel infoLabel = new JLabel("No transactions found.");
            JPanel transactionPanel = new JPanel();
            transactionPanel.setLayout(new GridLayout(1, 4));
            transactionPanel.add(infoLabel);
            allTransactionsPanel.add(transactionPanel);
        }
        // else display all the transactions, one per row
        else
            for (Transaction transaction : transactions) { 
                JPanel transactionPanel = new JPanel();
                transactionPanel.setLayout(new GridLayout(1, 4));

                JLabel titleLabel = new JLabel(transaction.getTitle());
                JLabel mediaTypeLabel = new JLabel(transaction.getMediaType());
                JLabel transactionTypeLabel = new JLabel(transaction.getTransactionType());
                JLabel transactionDateLabel = new JLabel(transaction.getTransactionDate().toString());

                transactionPanel.add(titleLabel);
                transactionPanel.add(mediaTypeLabel);
                transactionPanel.add(transactionTypeLabel);
                transactionPanel.add(transactionDateLabel);
                // TODO: return status???
                transactionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                allTransactionsPanel.add(transactionPanel);
            }

        revalidate();
        repaint();
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
        String stmtString = "SELECT TransactionID, Title, TransactionType, MediaType, TransactionDate " + 
                            "FROM Transactions INNER JOIN Articles ON Transactions.ArticleID = Articles.ArticleID " + 
                            "WHERE MemberID = " + member.getMemberID() + " UNION " +
                            "SELECT TransactionID, Title, TransactionType, MediaType, TransactionDate " + 
                            "FROM Transactions INNER JOIN Books ON Transactions.BookID = Books.BookID " + 
                            "WHERE MemberID = " + member.getMemberID() + " UNION " +
                            "SELECT TransactionID, Title, TransactionType, MediaType, TransactionDate " + 
                            "FROM Transactions INNER JOIN Movies ON Transactions.MovieID = Movies.MovieID " + 
                            "WHERE MemberID = " + member.getMemberID() + ";";
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

                transactions.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");

                    Transaction transaction = new Transaction();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    // Column indexes must match order of SELECT query, starting from index 1
                    transaction.setTransactionID(rs.getInt(1));
                    transaction.setTitle(rs.getString(2));
                    transaction.setTransactionType(rs.getString(3));
                    transaction.setMediaType(rs.getString(4));
                    transaction.setTransactionDate(formatter.parse(rs.getString(5)));
                    transactions.add(transaction);
                }
                // TODO: match each return transaction to the previous borrow transaction to get return status
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception ex) { System.out.println(ex.getMessage());}
        finally { releaseSQLResources(); }
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
