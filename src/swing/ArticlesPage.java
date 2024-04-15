package swing;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.*;

public class ArticlesPage extends JFrame {
    private JTextField searchField;

    public ArticlesPage() {
        super("Articles");

        searchField = new JTextField("Enter the articles's title, author, or genre...");
        searchField.setMinimumSize(new Dimension(200, searchField.getPreferredSize().height));

        JButton backButton = new JButton("Back to Main Menu");
        JButton searchButton = new JButton("Search");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new DashboardPage(); 
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality code here for when the user searches for an article
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Enter the article's title, author, or genre...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Enter the article's title, author, or genre...");
                }
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Articles Panel
        JPanel allArticlesPanel = new JPanel();
        allArticlesPanel.setLayout(new BoxLayout(allArticlesPanel, BoxLayout.Y_AXIS));

        // This is how you add a article into a list like view. This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book
        for (int i = 0; i < 15; i++) { 
            JPanel articlePanel = new JPanel();
            articlePanel.setLayout(new GridLayout(6, 1));

            JLabel titleLabel = new JLabel("Title: Placeholder Title" + i);
            JLabel authorLabel = new JLabel("Author: Placeholder Author");
            JLabel volumeLabel = new JLabel("ISBN: Placeholder Volume");
            JLabel yearOfReleaseLabel = new JLabel("Year of Release: ");
            JLabel issueLabel = new JLabel("Genre: Placeholder Issue");
            JLabel totalCopiesLabel = new JLabel("Total Copies: Placeholder");
            JLabel availableCopiesLabel = new JLabel("Available Copies: Placeholder");

            JButton rentButton = new JButton("Rent");
            rentButton.addActionListener(new RentButtonActionListener
            (titleLabel.getText(), authorLabel.getText(), 
            volumeLabel.getText(), yearOfReleaseLabel.getText(), issueLabel.getText(), 
            totalCopiesLabel.getText(), availableCopiesLabel.getText()));

            articlePanel.add(titleLabel);
            articlePanel.add(authorLabel);
            articlePanel.add(volumeLabel);
            articlePanel.add(yearOfReleaseLabel);
            articlePanel.add(issueLabel);
            articlePanel.add(totalCopiesLabel);
            articlePanel.add(availableCopiesLabel);
            articlePanel.add(rentButton);

            // Creates the black like between each article
            articlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allArticlesPanel.add(articlePanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allArticlesPanel);
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
    private class RentButtonActionListener implements ActionListener {
        private String title;
        private String author;
        private String volume;
        private String yearOfRelease;
        private String issue;
        private String totalCopies;
        private String availableCopies;

        public RentButtonActionListener(String title, String author, String volume ,String yearOfRelease, String genre, String totalCopies, String availableCopies) {
            this.title = title;
            this.author = author;
            this.volume = volume;
            this.yearOfRelease = yearOfRelease;
            this.issue = genre;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Code to add after the person rents a article.
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Title: " + title);
        }
    }
}
