package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.sql.*;

public class AllMembersPage extends JFrame {
    public static final String searchPrompt = "Find a member by their name or email";
    public static final String backPrompt = "Back to Main Menu";
    private int librarianID = 0;
    private JTextField searchField;
    private JPanel allMembersPanel = null;
    ArrayList<Member> members; // all members
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public AllMembersPage(int librarianID) {
        super("All Members");
        this.librarianID = librarianID;
        createSQLConnection();
        members = new ArrayList<>();

        // Members Panel
        allMembersPanel = new JPanel();
        allMembersPanel.setLayout(new BoxLayout(allMembersPanel, BoxLayout.Y_AXIS));

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
                createSQLQuery(searchField.getText());
                updateAllMembersPanel();
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

        createSQLQuery("");
        updateAllMembersPanel();

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allMembersPanel);
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

    // This class ensures that each button to the book will be it's own button to the corresponding book,
    // so that when the user decides to rent the book,
    // the button to rent it will know what book the user chose.
    private class TransactionButtonActionListener implements ActionListener {
        private Member member;

        public TransactionButtonActionListener(Member member) {
            this.member = member;
        }

        // Code to add to view the specific transactions of a member
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Member ID: " + member.getMemberID());
            new TransactionPage(member.getMemberID());
        }
    }

    private class RemoveButtonActionListener implements ActionListener {
        private Member member;

        public RemoveButtonActionListener(Member member) {
            this.member = member;
        }

        // Code to add to remove a member
        @Override
        public void actionPerformed(ActionEvent e) {
            createSQLRemoveMember(member);
            updateAllMembersPanel();
            System.out.println("Member " + member.getMemberID() + " has been removed.");
        }
    }
    private void updateAllMembersPanel()
    {
        String[] header = {"First Name", "Last Name", "Email", "", ""};
        allMembersPanel.removeAll();
        allMembersPanel.add(makePanel(header));

        // Add members into a list like view.
        for (Member member : members) {
            String[] labels = {member.getFirstName(), 
                               member.getLastName(), 
                               member.getEmail(),
                               "View Member Transactions",
                               "Remove Member"};
            allMembersPanel.add(makePanelWithButton(labels, member));
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

    private JPanel makePanelWithButton(String[] labels, Member member)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, labels.length));
        for(int i = 0; i < labels.length - 2; i++)
        {
            JLabel label = new JLabel(labels[i]);
            panel.add(label);
        }
        // Last two grids are buttons
        JButton tButton = new JButton(labels[labels.length - 2]);
        tButton.addActionListener(new TransactionButtonActionListener(member));
        panel.add(tButton);

        JButton rButton = new JButton(labels[labels.length - 1]);
        rButton.addActionListener(new RemoveButtonActionListener(member));
        panel.add(rButton);

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
    // Search call for using the search bar in movies page.
    private void createSQLQuery(String searchText)
    {
        String[] searchWords = searchText.split(" ");

        String stmtString = "SELECT MemberID, FirstName, LastName, Email FROM Members " + 
                            "WHERE FirstName LIKE \"%" + searchWords[0] + "%\" OR LastName LIKE \"%" + searchWords[0] + "%\" OR Email LIKE \"%" + searchWords[0] + "%\"";
        for (int i = 1; i < searchWords.length; i++)
            stmtString += " OR FirstName LIKE \"%" + searchWords[i] + "%\" OR LastName LIKE \"%" + searchWords[i] + "%\" OR Email LIKE \"%" + searchWords[i] + "%\"";
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

                members.clear(); // remove previous search results
                while (rs.next()) {
                    System.out.print("Row\t");
                    for (int i = 0; i < cols; i++) {
                        String value = rs.getString(i + 1);
                        System.out.print(value + "\t");
                    }
                    System.out.println("");
                    Member member = new Member();
                    // Column indexes must match order of SELECT query, starting from index 1
                    member.setMemberID(rs.getInt(1));
                    member.setFirstName(rs.getString(2));
                    member.setLastName(rs.getString(3));
                    member.setEmail(rs.getString(4));
                    members.add(member);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
    }    
    private void createSQLRemoveMember(Member member)
    {
        // Do SQL statement to remove member from Members table
        String stmtString = "DELETE FROM Members WHERE MemberID = " + member.getMemberID() + ";";
        System.out.println(stmtString);
        try {
            stmt = conn.createStatement();
            stmt.execute(stmtString);
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
        
        // Remove all transactions from Transactions table for deleted member
        // Do SQL statement to remove member from Members table
        stmtString = "DELETE FROM Transactions WHERE MemberID = " + member.getMemberID() + ";";
        System.out.println(stmtString);
        try {
            stmt = conn.createStatement();
            stmt.execute(stmtString);
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
        
        // Remove deleted member from members ArrayList
        members.remove(member);
        System.out.println("Members left: " + members.size());
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
