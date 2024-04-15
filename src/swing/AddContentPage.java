package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddContentPage extends JFrame {
    public AddContentPage() {
        super("Add Content");

        JPanel addBookPanel = new JPanel();
        addBookPanel.setLayout(new GridLayout(5, 2));

        JTextField titleField = new JTextField();
        addBookPanel.add(new JLabel("Title:"));
        addBookPanel.add(titleField);

        JTextField authorField = new JTextField();
        addBookPanel.add(new JLabel("Author/Director:"));
        addBookPanel.add(authorField);

        JTextField contentField = new JTextField();
        addBookPanel.add(new JLabel("Content Type:")); // Movie, article, book
        addBookPanel.add(contentField);

        JTextField genreField = new JTextField();
        addBookPanel.add(new JLabel("Genre:")); // Movie, article, book
        addBookPanel.add(genreField);

        JTextField volumeField = new JTextField();
        addBookPanel.add(new JLabel("Volume:"));
        addBookPanel.add(volumeField);

        JTextField isbnField = new JTextField();
        addBookPanel.add(new JLabel("ISBN:"));
        addBookPanel.add(isbnField);

        JTextField copiesField = new JTextField();
        addBookPanel.add(new JLabel("Number of Copies:"));
        addBookPanel.add(copiesField);
    
        JButton addButton = new JButton("Add");

        addBookPanel.add(new JLabel());
        addBookPanel.add(addButton);

        // Runs after the button has been clicked
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String content = contentField.getText();
                String volume = volumeField.getText();
                String isbn = isbnField.getText();
                String copies = copiesField.getText();

                // Add code here to double check if the inputs entered are valid. We can figure out later how we can add 
                // them as files or downloadables for the people to access them. I'll leave this here as an example. Do note that some of the input panels
                // can remain empty as not all prompts are applicable to each content type. For example, we dont need to know the volume for a movie
                // compared to a book/article

                try {
                    int number = Integer.parseInt(copies);
                    JOptionPane.showMessageDialog(AddContentPage.this, title +  " has been added to the library!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddContentPage.this, "Invalid Number. Please Enter an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());

        add(addBookPanel, BorderLayout.CENTER);

        setSize(300, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
