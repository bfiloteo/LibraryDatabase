package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
    public LoginPage() {
        super("Login");

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
                if (enteredUsername.equals("test") && enteredPassword.equals("123")) {
                    JOptionPane.showMessageDialog(LoginPage.this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new DashboardPage(); // Opens the librarian page ui
                } else if (enteredUsername.equals("admin") && enteredPassword.equals("123")) {
                    JOptionPane.showMessageDialog(LoginPage.this, "Admin Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new LibrarianPage();
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
}
