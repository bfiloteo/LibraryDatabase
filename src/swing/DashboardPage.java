package swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPage {

    public DashboardPage() {

        // Main Frame
        JFrame dashBoardFrame = new JFrame("Select What You Want to View From Our Library");

        // Buttons
        JButton booksButton = new JButton("Books");
        JButton moviesButton = new JButton("Movies");
        JButton articlesButton = new JButton("Articles");
        JButton accountButton = new JButton("Account Info");
        JButton logoutButton = new JButton("Logout");

        // On click listeners that will open the gui to their respective button
        booksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashBoardFrame.dispose();
                new BooksPage();
            }
        });

        moviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashBoardFrame.dispose();
                new MoviesPage();
            }
        });

        articlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashBoardFrame.dispose();
                new ArticlesPage();
            }
        });

        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashBoardFrame.dispose();
                new MemberPage();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashBoardFrame.dispose();
                new LoginPage();
            }
        });

        // Buttons
        dashBoardFrame.add(booksButton);
        dashBoardFrame.add(moviesButton);
        dashBoardFrame.add(articlesButton);
        dashBoardFrame.add(accountButton);
        dashBoardFrame.add(logoutButton);

        // Will change later
        dashBoardFrame.setLayout(new BoxLayout(dashBoardFrame.getContentPane(), BoxLayout.Y_AXIS));
        dashBoardFrame.setSize(300, 300);
        dashBoardFrame.setVisible(true);
        dashBoardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
