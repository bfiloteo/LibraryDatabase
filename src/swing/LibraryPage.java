package swing;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.*;

public class LibraryPage extends JFrame {
    private JTextField searchField;

    public LibraryPage() {
        super("Library");

        searchField = new JTextField("Find a book by their name, author, etc...");
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
                // Add functionality code here for when the librarian searches for something in the library
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Find a book by their name, author, etc...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Find a book by their name, author, etc...");
                }
            }
        });

        JButton addHoldingButton = new JButton("Add Holding");

        addHoldingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddContentPage(); 
            }
        });


        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // All things in the library Panel
        JPanel libraryPanel = new JPanel();
        libraryPanel.setLayout(new BoxLayout(libraryPanel, BoxLayout.Y_AXIS));

        // This is how you add all the things our library has into a list like view. This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book/article/movie
        for (int i = 0; i < 10; i++) { 
            JPanel holdingsPanel = new JPanel();
            holdingsPanel.setLayout(new GridLayout(6, 1));

            JLabel titleLabel = new JLabel("Title: Placeholder Title" + i);
            JLabel authorLabel = new JLabel("Author Name: Placeholder name");
            JLabel holdingType = new JLabel("Type: Placeholder type"); // Type means book, article, or movie.
            JLabel totalCopiesLabel = new JLabel("Total Copies: Placeholder");
            JLabel availableCopiesLabel = new JLabel("Available Copies: Placeholder");
            JLabel rentedOutCopiesLabel = new JLabel("Rented Out Copies: Placeholder");

            JButton addCopiesButton = new JButton("Add More Copies");
            addCopiesButton.addActionListener(new AddCopiesActionListener(titleLabel.getText()));

            JButton removeCopiesButton = new JButton("Remove More Copies");
            removeCopiesButton.addActionListener(new RemoveCopiesActionListener(titleLabel.getText()));

            holdingsPanel.add(titleLabel);
            holdingsPanel.add(authorLabel);
            holdingsPanel.add(holdingType);
            holdingsPanel.add(totalCopiesLabel);
            holdingsPanel.add(availableCopiesLabel);
            holdingsPanel.add(rentedOutCopiesLabel);
            holdingsPanel.add(addCopiesButton);
            holdingsPanel.add(removeCopiesButton);

            // Creates the black like border between each article
            holdingsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            libraryPanel.add(holdingsPanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(libraryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(addHoldingButton, BorderLayout.SOUTH);
    

        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // This class ensures that each button to the book will be it's own button to the corresponding book, so that when the librarian decides to add copies to the book,
    // it will only add more copies to the specific book.

    private class AddCopiesActionListener implements ActionListener {
        private String title;

        public AddCopiesActionListener(String title) {
            this.title = title;
        }

        // Code to add when adding copies to a book
        // referenced from https://stackoverflow.com/questions/8852560/how-to-make-popup-window-in-java
        @Override
        public void actionPerformed(ActionEvent e) {
            String num = JOptionPane.showInputDialog(LibraryPage.this, "How many copies do you want to add to " + title + "?", null);

            try {
                int number = Integer.parseInt(num);
                JOptionPane.showMessageDialog(LibraryPage.this, number +  " more copies have been added", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LibraryPage.this, "Invalid Number. Please Enter an Integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // This class ensures that each button to the book will be it's own button to the corresponding book, so that when the librarian decides to remove copies to the book,
    // it will only remove more copies to the specific book.
    private class RemoveCopiesActionListener implements ActionListener {
        private String title;

        public RemoveCopiesActionListener(String title) {
            this.title = title;
        }

        // Code to add when removing copies to a book. Ideally if we choose to remove all copies, this would be removed from the library.
        @Override
        public void actionPerformed(ActionEvent e) {
            String num = JOptionPane.showInputDialog(LibraryPage.this, "How many copies do you want to remove from " + title + "?", null);

            try {
                int number = Integer.parseInt(num);
                JOptionPane.showMessageDialog(LibraryPage.this, number +  " more copies have been removed", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LibraryPage.this, "Invalid Number. Please Enter an Integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
