package swing;
import library.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.*;

public class BooksPage extends JFrame {
    private JTextField searchField;

    public BooksPage() {
        super("Books");

        searchField = new JTextField("Enter the book's title, author, or genre...");
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
                // Add functionality code here for when the user searches for a book
            }
        });

        // Swing does not offer their own "placeholder text" feature for the search bars
        // so you have to do this instead where it will simply remove the text if the person decides to
        // type anything in and replaces it with the placeholder text if nothing was typed in.
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Enter the books title, author, or genre...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Enter the books title, author, or genre...");
                }
            }
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(backButton, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Book Panel
        JPanel allBooksPanel = new JPanel();
        allBooksPanel.setLayout(new BoxLayout(allBooksPanel, BoxLayout.Y_AXIS));

        // Placeholder code for SQL query
        ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            Book book = new Book();
            book.setTitle("Title: Placeholder Title" + i);
            book.setAuthor("Author: Placeholder Author" + i);
            book.setISBN(i);
            book.setGenre("Genre: Placeholder Genre");
            book.setAvailableCopies(i);
            book.setTotalCopies(i);
            books.add(book);
        }

        // This is how you add a book into a list like view.
        // This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book
        for (Book book : books) {
            
            JPanel bookPanel = new JPanel();
            bookPanel.setLayout(new GridLayout(6, 1));

            JLabel titleLabel = new JLabel(book.getTitle());
            JLabel authorLabel = new JLabel(book.getAuthor());
            JLabel isbnLabel = new JLabel("" + book.getISBN());
            JLabel genreLabel = new JLabel(book.getGenre());
            JLabel availableCopiesLabel = new JLabel("" + book.getAvailableCopies());
            JLabel totalCopiesLabel = new JLabel("" + book.getTotalCopies());
            
            JButton rentButton = new JButton("Rent");
            rentButton.addActionListener(new RentButtonActionListener(titleLabel.getText(), authorLabel.getText(), 
            isbnLabel.getText(), genreLabel.getText(), totalCopiesLabel.getText(), availableCopiesLabel.getText()));

            bookPanel.add(titleLabel);
            bookPanel.add(authorLabel);
            bookPanel.add(isbnLabel);
            bookPanel.add(genreLabel);
            bookPanel.add(totalCopiesLabel);
            bookPanel.add(availableCopiesLabel);
            bookPanel.add(rentButton);

            // Creates the black like between each book
            bookPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allBooksPanel.add(bookPanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allBooksPanel);
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
        private String isbn;
        private String genre;
        private String totalCopies;
        private String availableCopies;

        public RentButtonActionListener(String title, String author, String isbn, String genre, String totalCopies, String availableCopies) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.genre = genre;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Code to add after the person rents a book.
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Title: " + title);
        }
    }
}
