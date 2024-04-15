package swing;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibrarianPage extends JFrame {

    public LibrarianPage() {
        super("Librarian Info");

        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LoginPage(); 
            }
        });


        JLabel firstNameLabel = new JLabel("First Name: ");
        JLabel lastNameLabel = new JLabel("Last Name: ");
        JLabel emailLabel = new JLabel("Email: ");

        JPanel librarianPanel = new JPanel();
        librarianPanel.setLayout(new GridLayout(3, 2));

        librarianPanel.add(logoutButton);
        librarianPanel.add(firstNameLabel);
        librarianPanel.add(lastNameLabel);
        librarianPanel.add(emailLabel);

        JButton viewMembersButton = new JButton("View Members");
        viewMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new AllMembersPage(); 
            }
        });

        librarianPanel.add(viewMembersButton);

        JButton editLibraryButton = new JButton("Edit Library Page");

        editLibraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new LibraryPage(); 
            }
        });

        librarianPanel.add(editLibraryButton);

        setLayout(new BorderLayout());
        add(librarianPanel, BorderLayout.NORTH);

        setSize(400, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
