package swing;
import library.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TransactionPage extends JFrame {

    public TransactionPage() {
        super("This Members Transactions"); // Ideally change this to display the first name of the member


        JButton backButton = new JButton("Back to Member's Page");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });


        // Search Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);


        // Transaction Panel
        JPanel allTransactionsPanel = new JPanel();
        allTransactionsPanel.setLayout(new BoxLayout(allTransactionsPanel, BoxLayout.Y_AXIS));

        // Placeholder code for SQL query
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            Transaction transaction = new Transaction();
            transaction.setTransactionType("" + i);
            transaction.setTransactionDate(null);
            transaction.setBookID(i);
            transaction.setMovieID(i);
            transaction.setArticleID(i);
            transaction.setMemberID(i);

            transactions.add(transaction);
        }

        // This is how you add the transactions into a list like view.
        // This is in a set size for placeholder purposes,
        // but will change when functionality comes in after searching for a specific book
        for (Transaction transaction : transactions) { 
            JPanel transactionPanel = new JPanel();
            transactionPanel.setLayout(new GridLayout(6, 1));

            JLabel isbnLabel = new JLabel("" + transaction.getBookID());
            JLabel rentedDate = new JLabel("Date of Rent: Placeholder Date");
            JLabel returnDate = new JLabel("Date of Return: Placeholder Date");
            JLabel returnLabel = new JLabel("Return Status");

            transactionPanel.add(isbnLabel);
            transactionPanel.add(rentedDate);
            transactionPanel.add(returnDate);
            transactionPanel.add(returnLabel);


            // Creates the black like between each article
            transactionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            allTransactionsPanel.add(transactionPanel);
        }

        // Scrollbar feature
        JScrollPane scrollPane = new JScrollPane(allTransactionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
