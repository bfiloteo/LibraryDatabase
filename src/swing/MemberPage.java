package swing;
import library.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MemberPage extends JFrame {

    public MemberPage() {
        super("Account Info");

        JButton backButton = new JButton("Back to Main Menu");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new DashboardPage(); 
            }
        });

        // Placeholder code for SQL query
        ArrayList<Member> members = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            Member member = new Member();
            member.setMemberId(i);
            member.setFirstName("First Name: " + i);
            member.setLastName("Last Name: " + i);
            member.setEmail("Email: " + i);
        }

        for (Member member : members)
        {
            JLabel firstNameLabel = new JLabel(member.getFirstName());
            JLabel lastNameLabel = new JLabel(member.getLastName());
            JLabel emailLabel = new JLabel(member.getEmail());
        
            JPanel accountInfoPanel = new JPanel();
            accountInfoPanel.setLayout(new GridLayout(3, 2));

            accountInfoPanel.add(backButton);
            accountInfoPanel.add(firstNameLabel);
            accountInfoPanel.add(lastNameLabel);
            accountInfoPanel.add(emailLabel);
        

            JPanel allRentedPanel = new JPanel();
            allRentedPanel.setLayout(new BoxLayout(allRentedPanel, BoxLayout.Y_AXIS));

            // Placeholder for rented books
            for (int i = 0; i < 3; i++) { 
                JPanel rentedPanel = new JPanel();
                rentedPanel.setLayout(new GridLayout(1, 2));

                JLabel titleLabel = new JLabel("Title: Placeholder Title " + (i + 1));
                JLabel creatorLabel = new JLabel("Author: Placeholder Creator " + (i + 1));
                JLabel rentedDateLabel = new JLabel("Date of Rent: Placeholder Date");
                JLabel dueDateLabel = new JLabel("Due Date: Placeholder Date");

                JButton returnButton = new JButton("Return");
                returnButton.addActionListener(new ReturnButtonActionListener(titleLabel.getText(), creatorLabel.getText()));

                rentedPanel.add(titleLabel);
                rentedPanel.add(creatorLabel);
                rentedPanel.add(rentedDateLabel);
                rentedPanel.add(dueDateLabel);
                rentedPanel.add(returnButton);


                rentedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                allRentedPanel.add(rentedPanel);
            }

            JScrollPane scrollPane = new JScrollPane(allRentedPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


            setLayout(new BorderLayout());

            add(accountInfoPanel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER); 

        }
        setSize(400, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }

    private class ReturnButtonActionListener implements ActionListener {
        private String title;
        private String creator;

        public ReturnButtonActionListener(String title, String creator) {
            this.title = title;
            this.creator = creator;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Title: " + title);
            System.out.println("Creator: " + creator);
        }
    }

}
