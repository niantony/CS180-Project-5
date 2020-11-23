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
    private ArrayList<Conversation> conversations;
    private Conversation conversationDisplayed;
    JButton addButton;
    JButton settingsButton;
    JFrame mainFrame;
    JFrame messageFrame;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                //add conversation
            } else if (e.getSource() == settingsButton) {
                //settings gui
            } else {
                int index = Integer.parseInt(e.getActionCommand());
                conversationDisplayed = conversations.get(index);
                mainFrame.setVisible(false);
                messageFrame = new JFrame(conversationDisplayed.getName());
                //display messages
                messageFrame.setSize(600, 400);
                messageFrame.setLocationRelativeTo(null);
                messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                messageFrame.setVisible(true);
            }
        }
    };

    public MainGui() {
        conversations = new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainGui());
    }

    public void run() {
        mainFrame = new JFrame("Messages");
        Container content = mainFrame.getContentPane();
        content.setLayout(new BorderLayout());

        conversations = readConversationsFromFile();
        JPanel messagePanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        for (int i = 0; i < conversations.size(); i++) {
            JButton button = new JButton(conversations.get(i).getName());
            button.setActionCommand(String.valueOf(i));
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = GridBagConstraints.RELATIVE;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0;
            constraints.anchor = GridBagConstraints.NORTH;
//            button.setPreferredSize(new Dimension(400, 100));
            button.addActionListener(actionListener);
            messagePanel.add(button, constraints);
        }
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        content.add(scrollPane, BorderLayout.CENTER);

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

        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    //Reading conversations from file into ArrayList
    private ArrayList<Conversation> readConversationsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("ConversationFile.txt"))) {
            Conversation c = (Conversation) in.readObject();
            while (c != null) {
                conversations.add(c);
                c = (Conversation) in.readObject();
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return conversations;
    }
}
