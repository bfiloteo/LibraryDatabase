package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPage extends JFrame {
    public RegisterPage() {
        super("Register");

        // Register page components
        JTextField emailField = new JTextField("Email");
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");

        // Register panel and adding components
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(4, 2));
        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(emailField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(passwordField);
        registerPanel.add(new JLabel("Confirm Password:"));
        registerPanel.add(confirmPasswordField);
        registerPanel.add(backButton);
        registerPanel.add(registerButton);

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
                String enteredEmail = emailField.getText();
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

        setLayout(new BorderLayout());
        add(registerPanel, BorderLayout.CENTER);

        setSize(750, 500);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}