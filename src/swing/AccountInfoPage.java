package swing;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountInfoPage extends JFrame {

    public AccountInfoPage() {
        super("Account Info");

        JButton backButton = new JButton("Back to Main Menu");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new DashboardPage(); 
            }
        });


        JLabel firstNameLabel = new JLabel("First Name: ");
        JLabel lastNameLabel = new JLabel("Last Name: ");
        JLabel emailLabel = new JLabel("Email: ");

        JPanel accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new GridLayout(3, 2));

        accountInfoPanel.add(backButton);
        accountInfoPanel.add(firstNameLabel);
        accountInfoPanel.add(lastNameLabel);
        accountInfoPanel.add(emailLabel);


        JPanel allRentedPanel = new JPanel();
        allRentedPanel.setLayout(new BoxLayout(allRentedPanel, BoxLayout.Y_AXIS));

        // Placeholder for rented books
        for (int i = 0; i < 3; i++) { 
            JPanel rentedPanel = new JPanel();
            rentedPanel.setLayout(new GridLayout(1, 2));

            JLabel titleLabel = new JLabel("Title: Placeholder Title " + (i + 1));
            JLabel creatorLabel = new JLabel("Author: Placeholder Creator " + (i + 1));

            JButton returnButton = new JButton("Return");
            returnButton.addActionListener(new ReturnButtonActionListener(titleLabel.getText(), creatorLabel.getText()));

            rentedPanel.add(titleLabel);
            rentedPanel.add(creatorLabel);


            rentedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allRentedPanel.add(rentedPanel);
        }

        JScrollPane scrollPane = new JScrollPane(allRentedPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        setLayout(new BorderLayout());

        add(accountInfoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); 


        setSize(400, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private class ReturnButtonActionListener implements ActionListener {
        private String title;
        private String creator;

        public ReturnButtonActionListener(String title, String creator) {
            this.title = title;
            this.creator = creator;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Title: " + title);
            System.out.println("Creator: " + creator);
        }
    }

}
