package swing;
import library.*;

import javax.swing.*;

import com.mysql.cj.xdevapi.SelectStatement;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.sql.*;

public class ArticlesPage extends JFrame {
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

        searchField = new JTextField("Enter the articles's title or author...");
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
                // Add functionality code here for when the user searches for an article
                CreateSqlQuery(searchField.getText());
                AddAllArticlesPanel();
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
        
        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);

        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void AddAllArticlesPanel()
    {
        // Articles Panel
        if(allArticlesPanel != null)
            remove(allArticlesPanel);
        allArticlesPanel = new JPanel();
        allArticlesPanel.setLayout(new BoxLayout(allArticlesPanel, BoxLayout.Y_AXIS));

        // Add articles into a list like view.
        for (Article article : articles) { 
            JPanel articlePanel = new JPanel();
            articlePanel.setLayout(new GridLayout(6, 1));

            JLabel titleLabel = new JLabel(article.getTitle());
            JLabel authorLabel = new JLabel(article.getAuthor());
            JLabel volumeLabel = new JLabel("" + article.getVolume());
            JLabel issueLabel = new JLabel(article.getIssue());
            JLabel totalCopiesLabel = new JLabel("" + article.getTotalCopies());
            JLabel availableCopiesLabel = new JLabel("" + article.getAvailableCopies());

            JButton rentButton = new JButton("Rent");
            rentButton.addActionListener(new RentButtonActionListener
            (titleLabel.getText(), authorLabel.getText(),
             volumeLabel.getText(), issueLabel.getText(), 
             totalCopiesLabel.getText(), availableCopiesLabel.getText()));

            articlePanel.add(titleLabel);
            articlePanel.add(authorLabel);
            articlePanel.add(volumeLabel);
            articlePanel.add(issueLabel);
            articlePanel.add(totalCopiesLabel);
            articlePanel.add(availableCopiesLabel);
            articlePanel.add(rentButton);

            // Creates the black line between each article
            articlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allArticlesPanel.add(articlePanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allArticlesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // This class ensures that each button to the book will be its own button to the corresponding book,
    // so that when the user decides to rent the book,
    // the button to rent it will know what book the user chose.
    private class RentButtonActionListener implements ActionListener {
        private String title;
        private String author;
        private String volume;
        private String issue;
        private String totalCopies;
        private String availableCopies;

        public RentButtonActionListener(String title, String author, String volume, String genre, String totalCopies, String availableCopies) {
            this.title = title;
            this.author = author;
            this.volume = volume;
            this.issue = genre;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Code to add after the person rents a article.
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
    // Search call for using the search bar in articles page.
    private void CreateSqlQuery(String searchText)
    {
        String stmtString = "SELECT Author, Title, Volume, Issue, TotalCopies, AvailableCopies FROM Articles " + 
                            "WHERE Title LIKE \"%" + searchText + "%\" OR Author LIKE \"%" + searchText + "%\";";
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
                    article.setAuthor(rs.getString(1));
                    article.setTitle(rs.getString(2));
                    article.setVolume(rs.getInt(3));
                    article.setIssue(rs.getString(4));
                    article.setTotalCopies(rs.getInt(5));
                    article.setAvailableCopies(rs.getInt(6));
                    articles.add(article);
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
