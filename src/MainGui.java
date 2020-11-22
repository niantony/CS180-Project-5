import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * CS 180 Project 5 -- MainGui.java
 *
 * Main GUI that interacts with user
 */
public class MainGui extends JComponent implements Runnable {
    private File conversationFile = new File("src/ConversationFile.txt");
    private String user;
    private String other;
    private ArrayList<Conversation> conversations;
    JButton addButton;
    JButton settingsButton;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                //add conversation
            }
            if (e.getSource() == settingsButton) {
                //settings gui
            }
        }
    };

    public MainGui() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainGui());
    }

    public void run() {
        JFrame frame = new JFrame("Messages");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        //Add conversations as buttons
//        ArrayList<Conversation> conversations = readConversationsFromFile();
//        for (Conversation conversation : conversations) {
//            messagePanel.add(new JButton(conversation.getUsers().getName()));
//        }
        content.add(messagePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(actionListener);
        bottomPanel.add(settingsButton, BorderLayout.WEST);
        addButton = new JButton("Add Conversation");
        addButton.addActionListener(actionListener);
        bottomPanel.add(addButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomPanel.setBackground(Color.white);
        content.add(bottomPanel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //Reading conversations from file into ArrayList
    private ArrayList<Conversation> readConversationsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(conversationFile))) {
            String line = (String) in.readObject();
            if (line == null) {
                return null;
            }
            String[] users = line.split("\\*");
            user = users[0];
            other = users[1];
            Conversation conversation = (Conversation) in.readObject();
            while (conversation != null) {
                conversations.add(conversation);
                conversation = (Conversation) in.readObject();
            }
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return conversations;
    }
}
