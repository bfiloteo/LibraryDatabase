package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;


public class ArticlesPage extends JFrame {
    public static final String searchPrompt = "Enter the article's title or author";
    public static final String backPrompt = "Back to Main Menu";
    public static final int WeeksToBorrow = 3;
    private JTextField searchField;
    private JPanel allArticlesPanel = null;
    ArrayList<Article> articles;    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public ArticlesPage() {
        super("Articles");
        createSQLConnection();
        articles = new ArrayList<>();

        // Articles Panel
        allArticlesPanel = new JPanel();
        allArticlesPanel.setLayout(new BoxLayout(allArticlesPanel, BoxLayout.Y_AXIS));

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
                // Add functionality code here for when the user searches for an article
                createSQLQuery(searchField.getText());
                updateAllArticlesPanel();
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
        JScrollPane scrollPane = new JScrollPane(allArticlesPanel);
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

    private void updateAllArticlesPanel()
    {
        allArticlesPanel.removeAll();

        // Add articles into a list like view.
        for (Article article : articles) { 
            JPanel articlePanel = new JPanel();
            articlePanel.setLayout(new GridLayout(1, 7));

            JLabel titleLabel = new JLabel(article.getTitle());
            JLabel authorLabel = new JLabel(article.getAuthor());
            JLabel volumeLabel = new JLabel("" + article.getVolume());
            JLabel issueLabel = new JLabel(article.getIssue());
            JLabel totalCopiesLabel = new JLabel("" + article.getTotalCopies());
            JLabel availableCopiesLabel = new JLabel("" + article.getAvailableCopies());

            JButton borrowButton = new JButton("Borrow");
            borrowButton.addActionListener(new BorrowButtonActionListener(article));

            articlePanel.add(titleLabel);
            articlePanel.add(authorLabel);
            articlePanel.add(volumeLabel);
            articlePanel.add(issueLabel);
            articlePanel.add(totalCopiesLabel);
            articlePanel.add(availableCopiesLabel);
            articlePanel.add(borrowButton);

            // Creates the black line between each article
            articlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allArticlesPanel.add(articlePanel);
        }

        revalidate();
        repaint();
    }

    // This class ensures that each button to the book will be its own button to the corresponding book,
    // so that when the user decides to borrow the book, the button to borrow it will know what book the user chose.
    private class BorrowButtonActionListener implements ActionListener {
        private Article article;

        public BorrowButtonActionListener(Article article) {
            this.article = article;
        }

        // Code to add after the person borrows an article.
        @Override
        public void actionPerformed(ActionEvent e) {
            createSQLUpdate(article);
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
    // Search call for using the search bar in articles page.
    private void createSQLQuery(String searchText)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "SELECT ArticleID, Author, Title, Volume, Issue, TotalCopies, AvailableCopies FROM Articles " + 
                            "WHERE Title LIKE \"%" + searchWords[0] + "%\" OR Author LIKE \"%" + searchWords[0] + "%\"";
        for (int i = 1; i < searchWords.length; i++)
            stmtString += "OR Title LIKE \"%" + searchWords[i] + "%\" OR Author LIKE \"%" + searchWords[i] + "%\"";
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

                articles.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");
                    Article article = new Article();
                    // Column indexes must match order of SELECT query, starting from index 1
                    article.setArticleID(rs.getInt(1));
                    article.setAuthor(rs.getString(2));
                    article.setTitle(rs.getString(3));
                    article.setVolume(rs.getInt(4));
                    article.setIssue(rs.getString(5));
                    article.setTotalCopies(rs.getInt(6));
                    article.setAvailableCopies(rs.getInt(7));
                    articles.add(article);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
    }

    private void createSQLUpdate(Article article)
    {
        // Do SQL query to get number of available copies
        int availableCopies = 0;
        String stmtString = "SELECT AvailableCopies FROM Articles WHERE ArticleID = " + article.getArticleID() + ";";
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

        // if there is an available copy, then update Articles table to reduce available copies by 1
        // and update Transactions table to borrow the article
        if( availableCopies > 0)
        {
            availableCopies--;
            stmtString = "UPDATE Articles SET AvailableCopies = " + availableCopies + " WHERE ArticleID = " + article.getArticleID() + ";";
            System.out.println(stmtString);
            try {
                stmt = conn.createStatement();
                stmt.execute(stmtString);
            }
            catch (SQLException ex) { handleSQLException(ex); }
            finally { releaseSQLResources(); }
            article.setAvailableCopies(availableCopies); // update article with decremented availableCopies
            updateAllArticlesPanel();

            LocalDate transactionDate = LocalDate.now();
            LocalDate dueDate = LocalDate.now();
            dueDate.plusWeeks(WeeksToBorrow);
            int memberID = 1; // TODO placeholder
            Random rand = new Random(); // generate random number for transaction ID
            int transactionID = transactionDate.hashCode() + article.getArticleID() + (memberID * 1024) + rand.nextInt();
            if( transactionID < 0 ) transactionID = -transactionID; // make transactionID positive
            // TODO check for collisions in Transactions table
            stmtString = "INSERT INTO Transactions (TransactionID, TransactionType, MediaType, TransactionDate, DueDate, MemberID, ArticleID) VALUES (" +
                         transactionID + ", \"borrow\", \"article\", \"" + transactionDate + "\", \"" + dueDate + "\", " + memberID + ", " + article.getArticleID() + ");";
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
