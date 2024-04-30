package swing;
import library.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

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

        // Placeholder code for SQL query
        ArrayList<Librarian> librarians = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            Librarian librarian = new Librarian();
            librarian.setLibrarianID(i);
            librarian.setFirstName("First Name: " + i);
            librarian.setLastName("Last Name: " + i);
            librarian.setEmail("Email: ");
            librarians.add(librarian);
        }

        for (Librarian librarian : librarians)
        {
            JLabel firstNameLabel = new JLabel(librarian.getFirstName());
            JLabel lastNameLabel = new JLabel(librarian.getLastName());
            JLabel emailLabel = new JLabel(librarian.getEmail());
        

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
        }
        
        setSize(750, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
