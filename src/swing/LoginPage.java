package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    public static final String SQLPassword = "329761";
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private int memberID = 0;
    private int librarianID = 0;

    public LoginPage() {
        super("Login");
        createSQLConnection();

        JPanel mainPanel = new JPanel(new GridLayout());
        
        // Left panel that displays the title and the icon
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(34, 139, 34));
        leftPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        ImageIcon icon = new ImageIcon(getClass().getResource("/images/book_icon.png"));
        Image scaledIcon = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIconImage = new ImageIcon(scaledIcon);

        JLabel iconLabel = new JLabel(scaledIconImage);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(Box.createVerticalGlue()); // https://stackoverflow.com/questions/46692485/boxlayout-using-vertical-and-horizontal-glue
        leftPanel.add(iconLabel);

        JLabel libraryLabel = new JLabel("Library Database");
        libraryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        libraryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(libraryLabel);
        leftPanel.add(Box.createVerticalGlue()); 

        // Right panel that has login information and components
        // https://www.javatpoint.com/java-gridbaglayout
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(emailField, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(buttonPanel, gbc);

        // ActionListener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = emailField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                // Check if the username and password are valid. In this case it's test and 123
                //if (enteredUsername.equals("test") && enteredPassword.equals("123")) {
                if(createSQLQuery("member", enteredUsername, enteredPassword)) {
                    JOptionPane.showMessageDialog(LoginPage.this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new DashboardPage(memberID); // Opens the member page UI
                //} else if (enteredUsername.equals("admin") && enteredPassword.equals("123")) {
                } else if(createSQLQuery("librarian", enteredUsername, enteredPassword)) {
                    JOptionPane.showMessageDialog(LoginPage.this, "Admin Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new LibrarianPage(librarianID); // Opens the librarian page UI
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Email")) {
                    emailField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Email");
                }
            }
        });

        // https://stackoverflow.com/questions/19755259/hide-show-password-in-a-jtextfield-java-swing
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setText("Password");
                    passwordField.setEchoChar((char)0);
                }
            }
        });

        // ActionListener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterPage();
                dispose();
            }
        });

        add(mainPanel);
        mainPanel.add(leftPanel);
        mainPanel.add(loginPanel);

        setSize(500, 300);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // center ui on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new LoginPage();
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

    private boolean createSQLQuery(String userType, String enteredUsername, String enteredPassword)
    {
        String stmtString;
        if(userType.equals("member"))
            stmtString = "SELECT MemberID FROM Members WHERE UserName = \"" + enteredUsername + 
                         "\" AND PasswordHash = " + enteredPassword.hashCode() + ";";
        else
            stmtString = "SELECT LibrarianID FROM Librarians WHERE UserName = \"" + enteredUsername + 
                         "\" AND PasswordHash = " + enteredPassword.hashCode() + ";";

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

                    // Column indexes must match order of SELECT query, starting from index 1
                    if(userType.equals("member"))
                    {
                        memberID = rs.getInt(1);
                        System.out.println("Found member " + memberID + " with userName = " + enteredUsername + 
                                           " and password = " + enteredPassword + " in Members table");
                    }
                    else
                    {
                        librarianID = rs.getInt(1);
                        System.out.println("Found librarian " + librarianID + " with userName = " + enteredUsername + 
                                           " and password = " + enteredPassword + " in Librarians table");
                    }
                    releaseSQLResources();
                    return true;
                }
                if(userType.equals("member"))
                    System.out.println("Error: did not find member with userName = " + enteredUsername + 
                                       " and password = " + enteredPassword + " in Members table");
                else
                    System.out.println("Error: did not find librarian with userName = " + enteredUsername + 
                                       " and password = " + enteredPassword + " in Librarians table");
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception ex) { System.out.println(ex.getMessage());}
        finally { releaseSQLResources(); }
        return false;
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
