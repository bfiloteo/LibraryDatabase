package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardPage extends JFrame {

    public DashboardPage() {

        super("Select What You Want to View From Our Library");

        // Main Frame
        JPanel mainPanel = new JPanel(new GridLayout(2, 2)); // Parameters to make it will look like a 2x2 chart

        // Articles panel
        JPanel articlePanel = new JPanel();
        articlePanel.setLayout(new BoxLayout(articlePanel, BoxLayout.Y_AXIS));
        articlePanel.setBackground(Color.RED);
        articlePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        ImageIcon articleIcon = new ImageIcon(getClass().getResource("/images/article_icon.png")); 
        Image scaledArticleIcon = articleIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Resize icon
        ImageIcon scaledIconImage = new ImageIcon(scaledArticleIcon);

        JButton articlesButton = new JButton("Articles");
        articlesButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

        JLabel articleIconLabel = new JLabel(scaledIconImage); // Image icon
        articleIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the icon horizontally

        articlePanel.add(Box.createVerticalGlue());
        articlePanel.add(articleIconLabel); // Add icon label
        articlePanel.add(Box.createVerticalGlue()); 
        articlePanel.add(articlesButton);
        articlePanel.add(Box.createVerticalGlue());
        mainPanel.add(articlePanel);

        // Books panel
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));
        bookPanel.setBackground(Color.BLUE);
        bookPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        ImageIcon bookIcon = new ImageIcon(getClass().getResource("/images/book_icon.png")); 
        Image scaledBookIcon = bookIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Resize icon
        ImageIcon scaledBookIconImage = new ImageIcon(scaledBookIcon);

        JButton bookButton = new JButton("Books");
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

        JLabel bookIconLabel = new JLabel(scaledBookIconImage); // Image icon
        bookIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the icon horizontally

        bookPanel.add(Box.createVerticalGlue());
        bookPanel.add(bookIconLabel); // Add icon label
        bookPanel.add(Box.createVerticalGlue()); 
        bookPanel.add(bookButton);
        bookPanel.add(Box.createVerticalGlue());
        mainPanel.add(bookPanel);

        // Movies panel
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
        moviePanel.setBackground(Color.GREEN);
        moviePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        ImageIcon movieIcon = new ImageIcon(getClass().getResource("/images/tv_icon.png")); 
        Image scaledMovieIcon = movieIcon.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH); // Resize icon
        ImageIcon scaledMovieIconImage = new ImageIcon(scaledMovieIcon);

        JButton movieButton = new JButton("Movies");
        movieButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

        JLabel movieIconLabel = new JLabel(scaledMovieIconImage); // Image icon
        movieIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the icon horizontally

        moviePanel.add(Box.createVerticalGlue());
        moviePanel.add(movieIconLabel); // Add icon label
        moviePanel.add(Box.createVerticalGlue()); 
        moviePanel.add(movieButton);
        moviePanel.add(Box.createVerticalGlue());
        mainPanel.add(moviePanel);

        // Account Panel
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setBackground(Color.PINK);
        accountPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        ImageIcon accountIcon = new ImageIcon(getClass().getResource("/images/account_icon.png")); // Load image from resources
        Image scaledAccountIcon = accountIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH); // Resize icon
        ImageIcon scaledAccountIconImage = new ImageIcon(scaledAccountIcon);

        JLabel accountIconLabel = new JLabel(scaledAccountIconImage); // Image icon
        accountIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the icon horizontally

        accountPanel.add(Box.createVerticalGlue()); // Add vertical glue
        accountPanel.add(accountIconLabel); // Add icon label
        accountPanel.add(Box.createVerticalGlue()); // Add vertical glue

        JButton logoutButton = new JButton("Logout");
        JButton accountButton = new JButton("Account");

        // Create a panel to hold the buttons and add space between them. 
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(Color.PINK);
        buttonsPanel.add(logoutButton);

        buttonsPanel.add(Box.createHorizontalStrut(5)); // Space between buttons (20 pixels)
        buttonsPanel.add(Box.createVerticalStrut(60)); // Helps align the buttons with the one on the bottom left panel

        buttonsPanel.add(accountButton);

        accountPanel.add(buttonsPanel); // Add button panel
        accountPanel.add(Box.createVerticalGlue()); // Add vertical glue

        accountPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(accountPanel);


        // On click listeners that will open the gui to their respective button
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new BooksPage();
            }
        });

        movieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MoviesPage();
            }
        });

        articlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ArticlesPage();
            }
        });

        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MemberPage();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });

        add(mainPanel);
        setSize(500, 300);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // Center UI on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
