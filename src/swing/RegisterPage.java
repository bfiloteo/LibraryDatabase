package swing;

import javax.swing.*;

import library.Item;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterPage extends JFrame {
    public static final String usernamePrompt = "Username";
    public static final String passwordPrompt = "Password";
    public static final String firstNamePrompt = "First name";
    public static final String lastNamePrompt = "Last name";
    public static final String emailPrompt = "Email          ";

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public RegisterPage() {
        super("Register");
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


        // Right panel that has inputs to register for an account and components
        JPanel registerPanel = new JPanel(new GridBagLayout()); // https://www.youtube.com/watch?v=g2vDARb7gx8 | GBC helps a lot when organizing certain input fields and buttons in java
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        JLabel usernameLabel = new JLabel(usernamePrompt);
        JTextField usernameField = new JTextField(15);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(usernamePanel, gbc);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel(passwordPrompt);
        JPasswordField passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(passwordPanel, gbc);

        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.setLayout(new BoxLayout(confirmPasswordPanel, BoxLayout.X_AXIS));
        JLabel confirmPasswordLabel = new JLabel(passwordPrompt);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(confirmPasswordField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(confirmPasswordPanel, gbc);

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new BoxLayout(firstNamePanel, BoxLayout.X_AXIS));
        JLabel firstNameLabel = new JLabel(firstNamePrompt);
        JTextField firstNameField = new JTextField(15);
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(firstNamePanel, gbc);

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new BoxLayout(lastNamePanel, BoxLayout.X_AXIS));
        JLabel lastNameLabel = new JLabel(lastNamePrompt);
        JTextField lastNameField = new JTextField(15);
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(lastNamePanel, gbc);

        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        JLabel emailLabel = new JLabel(emailPrompt);
        JTextField emailField = new JTextField(15);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(emailPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        registerPanel.add(buttonPanel, gbc);

        // Back button functionality
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LoginPage(); 
            }
        });

        // ActionListener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmedPassword = new String(confirmPasswordField.getPassword());
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();

                if (username.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmedPassword)) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Todo: check for username collision in Members table
                    int memberID = createSQLUpdate(username, password, firstName, lastName, email);
                    // Successful registration, ideally add it to where the account will be added to the database.
                    JOptionPane.showMessageDialog(RegisterPage.this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new DashboardPage(memberID); 
                }
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Enter Username")) {
                    usernameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Enter Username");
                }
            }
        });

        // https://stackoverflow.com/questions/19755259/hide-show-password-in-a-jtextfield-java-swing
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("Create a Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setText("Create a Password");
                    passwordField.setEchoChar((char)0);
                }
            }
        });

        confirmPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (confirmPasswordField.getText().equals("Confirm Password")) {
                    confirmPasswordField.setText("");
                    confirmPasswordField.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (confirmPasswordField.getText().isEmpty()) {
                    confirmPasswordField.setText("Confirm Password");
                    confirmPasswordField.setEchoChar((char)0);
                }
            }
        });

        firstNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (firstNameField.getText().equals("Enter First Name")) {
                    firstNameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (firstNameField.getText().isEmpty()) {
                    firstNameField.setText("Enter First Name");
                }
            }
        });

        lastNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (lastNameField.getText().equals("Enter Last Name")) {
                    lastNameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (lastNameField.getText().isEmpty()) {
                    lastNameField.setText("Enter Last Name");
                }
            }
        });

        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Enter Email")) {
                    emailField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Enter Email");
                }
            }
        });

        add(mainPanel);
        mainPanel.add(leftPanel);
        mainPanel.add(registerPanel);

        setSize(500, 300);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // center ui on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            conn = DriverManager.getConnection("jdbc:mysql://localhost/library?" + "user=root&password=123456");
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }
        catch (SQLException ex) { handleSQLException(ex); }
        catch (Exception e)
        {
            System.out.println("Error." + e.getMessage());
        }
    }   

    // Update Members SQL table with new member and return memberID of new member
    private int createSQLUpdate(String username, String password, String firstName, String lastName, String email)
    {
        // Do SQL query to get the largest current memberID
        int memberID = 0;
        String stmtString = "SELECT MAX(MemberID) FROM Members;";
        System.out.println(stmtString);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(stmtString);
        
            // Now do something with the ResultSet ....
            if (rs != null) {
                while (rs.next()) {
                    memberID = rs.getInt(1);
                    System.out.println("Max memberID =\t" + memberID);
                }
            }
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }

        memberID++; // new memberID is one larger than current max memberID
        stmtString = "INSERT INTO Members (MemberID, FirstName, LastName, Email, UserName, PasswordHash)" +
                     "VALUES (" + memberID + ", '" + firstName + "', '" + lastName + "', '" + email + "', '" + username + "', " + password.hashCode() + ");";
        System.out.println(stmtString);
        try {
            stmt = conn.createStatement();
            stmt.execute(stmtString);
        }
        catch (SQLException ex) { handleSQLException(ex); }
        finally { releaseSQLResources(); }
        return memberID;
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