package swing;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.*;

public class AllMembersPage extends JFrame {
    private JTextField searchField;

    public AllMembersPage() {
        super("All Members");

        searchField = new JTextField("Find a member by their name, id, etc...");
        searchField.setMinimumSize(new Dimension(200, searchField.getPreferredSize().height));

        JButton backButton = new JButton("Back to Menu");
        JButton searchButton = new JButton("Search");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LibrarianPage(); 
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality code here for when the user searches for a member
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Find a member by their name, id, etc...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Find a member by their name, id, etc...");
                }
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Articles Panel
        JPanel allMembersPanel = new JPanel();
        allMembersPanel.setLayout(new BoxLayout(allMembersPanel, BoxLayout.Y_AXIS));

        // This is how you add all the members into a list like view. This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book
        for (int i = 0; i < 10; i++) { 
            JPanel memberPanel = new JPanel();
            memberPanel.setLayout(new GridLayout(6, 1));

            JLabel memberLabel = new JLabel("Member: Placeholder Member#" + i);
            JLabel firstNameLabel = new JLabel("First Name: Placeholder Name");
            JLabel lastNameLabel = new JLabel("Last Name: Placeholder Name");
            JLabel emailLabel = new JLabel("Email: Placeholder Email");

            JButton viewTransactionsButton = new JButton("View Member Transactions");
            viewTransactionsButton.addActionListener(new TransactionButtonActionListener(memberLabel.getText()));

            memberPanel.add(memberLabel);
            memberPanel.add(firstNameLabel);
            memberPanel.add(lastNameLabel);
            memberPanel.add(emailLabel);
            memberPanel.add(viewTransactionsButton);

            JButton removeMemberButton = new JButton("Remove Member");
            removeMemberButton.addActionListener(new RemoveButtonActionListener(memberLabel.getText()));

            // Creates the black like between each article
            memberPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allMembersPanel.add(memberPanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allMembersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // This class ensures that each button to the book will be it's own button to the corresponding book, so that when the user decides to rent the book,
    // the button to rent it will know what book the user chose.
    private class TransactionButtonActionListener implements ActionListener {
        private String member;

        public TransactionButtonActionListener(String member) {
            this.member = member;
        }

        // Code to add to view the specific transactions of a member
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Member: " + member);
            new TransactionPage();
        }
    }

    private class RemoveButtonActionListener implements ActionListener {
        private String member;

        public RemoveButtonActionListener(String member) {
            this.member = member;
        }

        // Code to add to remove a member
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Member: " + member + " has been removed.");
        }
    }

}
