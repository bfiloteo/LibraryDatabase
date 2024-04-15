package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        super("Login");

        // Login Page Components
        JTextField emailField = new JTextField("Email");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Login Panel and adding the components
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

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
                    new DashboardPage(); // Opens the dashboard page ui
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars so you have to do this instead where it will simply remove the text if the person decides to
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

        // ActionListener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterPage();
                dispose();
            }
        });

        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.CENTER);

        setSize(300, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
