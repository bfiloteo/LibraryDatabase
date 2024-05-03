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

public class LibrarianPage extends JFrame {
    public static final String SQLTableName = "Librarians";
    private int librarianID = 0;
    private JPanel allLibrariansPanel = null;
    ArrayList<Librarian> librarians; // all librarians
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public LibrarianPage(int librarianID) {
        super("Librarian Info");
        this.librarianID = librarianID;
        createSQLConnection();
        librarians = new ArrayList<>();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LoginPage(); 
            }
        });
        buttonPanel.add(logoutButton);

        JButton viewMembersButton = new JButton("View Members");
        viewMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new AllMembersPage(librarianID); 
            }
        });
        buttonPanel.add(viewMembersButton);

//        JButton viewLibrariansButton = new JButton("View Librarians");
//        viewMembersButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose(); 
//                new AllLibrariansPage(); 
//            }
//        });
//        buttonPanel.add(viewLibrariansButton);

        JButton editLibraryButton = new JButton("Edit Library Page");
        editLibraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LibraryPage(librarianID); 
            }
        });
        buttonPanel.add(editLibraryButton);

        // Librarians Panel
        allLibrariansPanel = new JPanel();
        allLibrariansPanel.setLayout(new BoxLayout(allLibrariansPanel, BoxLayout.Y_AXIS));
        createSQLQuery();
        updateAllLibrariansPanel();

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allLibrariansPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); 
        
        setSize(750, 500);
        setLocationRelativeTo(null); // center ui on screen
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateAllLibrariansPanel()
    {
        String[] header = {"First Name", "Last Name", "Email"};
        allLibrariansPanel.removeAll();
        allLibrariansPanel.add(makePanel(header));
        for (Librarian librarian : librarians)
        {
            String[] labels = {librarian.getFirstName(), librarian.getLastName(), librarian.getEmail()};
            allLibrariansPanel.add(makePanel(labels));
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
    // Find all librarians.
    private void createSQLQuery()
    {
        String stmtString = "SELECT LibrarianID, FirstName, LastName, Email FROM " + SQLTableName + ";";
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

                librarians.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");

                    Librarian librarian = new Librarian();
                    // Column indexes must match order of SELECT query, starting from index 1
                    librarian.setLibrarianID(rs.getInt(1));
                    librarian.setFirstName(rs.getString(2));
                    librarian.setLastName(rs.getString(3));
                    librarian.setEmail(rs.getString(4));
                    librarians.add(librarian);
                }
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

