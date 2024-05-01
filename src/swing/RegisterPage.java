package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPage extends JFrame {
    public RegisterPage() {
        super("Register");

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

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(usernameField, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(passwordField, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(confirmPasswordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
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
                String enteredEmail = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());
                String confirmedPassword = new String(confirmPasswordField.getPassword());

                if (enteredEmail.isEmpty() || enteredPassword.isEmpty() || confirmedPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!enteredPassword.equals(confirmedPassword)) {
                    JOptionPane.showMessageDialog(RegisterPage.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Successful registration, ideally add it to where the account will be added to the database.
                    JOptionPane.showMessageDialog(RegisterPage.this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                   dispose();
                   new DashboardPage(); 
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

        add(mainPanel);
        mainPanel.add(leftPanel);
        mainPanel.add(registerPanel);

        setSize(500, 300);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // center ui on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}