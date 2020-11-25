import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client implements Runnable {
    JButton homeButton;
    JButton saveButton;
    JButton logoutButton;
    JButton deleteButton;
    JTextField nameField;
    JTextField usernameField;
    JTextField passwordField;
    JLabel nameLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == homeButton) {
                homePage();
            }
            if (e.getSource() == saveButton) {
                saveAccount();
            }
            if (e.getSource() == logoutButton) {
                logout();
            }
            if (e.getSource() == deleteButton) {
                deleteAccount();
            }
        }
    };
    // Returns to the main page
    public void homePage() {

    }
    // Updates the user's information with the new inputted info
    public void saveAccount() {

    }
    // Logs out the user. Will return to the login page
    public void logout() {

    }
    // Deletes the account and removes them from their conversations
    public void deleteAccount() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    public void run() {
        // testUser is here for testing purposes
//        User testUser = new User("Jimmy", "jimmy123", "jimmypassword");

        JFrame frame = new JFrame("Messaging App");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        // Contains all the information fields to view and edit.
        // Includes the user's "Name", "Username", and "Password"
        JPanel infoPanel = new JPanel();
        nameLabel = new JLabel("Name");
//        nameField = new JTextField(testUser.getName(), 10);
//        usernameLabel = new JLabel("Username");
//        usernameField = new JTextField(testUser.getUsername(), 10);
//        passwordLabel = new JLabel("Password");
//        passwordField = new JPasswordField(testUser.getPassword(), 10);
        infoPanel.add(nameLabel);
        infoPanel.add(nameField);
        infoPanel.add(usernameLabel);
        infoPanel.add(usernameField);
        infoPanel.add(passwordLabel);
        infoPanel.add(passwordField);
        content.add(infoPanel, BorderLayout.CENTER);

        // Shows the "Home", "Save", and "Delete" buttons at the bottom of the GUI
        JPanel buttonsPanel = new JPanel();
        homeButton = new JButton("Home");
        homeButton.addActionListener(actionListener);
        saveButton = new JButton("Save");
        saveButton.addActionListener(actionListener);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(actionListener);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(actionListener);
        buttonsPanel.add(homeButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(logoutButton);
        buttonsPanel.add(deleteButton);
        content.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
