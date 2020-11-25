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
    private File messages;
    JButton addButton;
    JButton settingsButton;
    JFrame mainFrame;
    JFrame messageFrame;
    JTextField textField;
    JButton sendButton;
    JPanel messagePanel;
    User user = new User("Jack", "Jack", "0909", new File("ConversationFile.txt"));
//    JWindow mainWindow;
//    JWindow messageWindow;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                //add conversation
            } else if (e.getSource() == settingsButton) {
                //settings gui
            } else if (e.getSource() == sendButton) {
                String message = textField.getText();
                addMessage(message);
            } else {
                int index = Integer.parseInt(e.getActionCommand());
                conversationDisplayed = conversations.get(index);
                displayMessages();
            }
        }
    };

//    WindowListener windowListener = new WindowListener() {
//        @Override
//        public void windowOpened(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowClosing(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowClosed(WindowEvent e) {
//            if (e.getSource() == messageWindow) {
//                mainFrame.setVisible(true);
//            }
//        }
//
//        @Override
//        public void windowIconified(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowDeiconified(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowActivated(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowDeactivated(WindowEvent e) {
//
//        }
//    };

    public MainGui() {
        conversations = new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainGui());
    }

    public void run() {
        mainFrame = new JFrame("Messages");
//        mainWindow = new JWindow(mainFrame);
//        mainWindow.addWindowListener(windowListener);
        Container content = mainFrame.getContentPane();
        content.setLayout(new BorderLayout());

        conversations = readConversationsFromFile();
        JPanel conversationPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(conversationPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        for (int i = 0; i < conversations.size(); i++) {
            JButton button = new JButton(conversations.get(i).getName());
            button.setActionCommand(String.valueOf(i));
            button.addActionListener(actionListener);
            conversationPanel.add(button, constraints);
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

    private void displayMessages() {
//        mainFrame.setVisible(false);
        messageFrame = new JFrame(conversationDisplayed.getName());
//        messageWindow = new JWindow(messageFrame);
//        messageWindow.addWindowListener(windowListener);
        Container content = messageFrame.getContentPane();
        content.setLayout(new BorderLayout());
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        messages = conversationDisplayed.getMessages();
        String user;
        String message;
        try (BufferedReader br = new BufferedReader(new FileReader(messages))) {
            String line = br.readLine();
            while (line != null) {
                String[] userAndMessage = line.split("\\*");
                user = userAndMessage[0];
                message = userAndMessage[1];
                messagePanel.add(new JLabel(user + ": " + message));
                line = br.readLine();
            }
        } catch (IOException e) {
            //do something?
        }
        content.add(scrollPane, BorderLayout.CENTER);
        JPanel textFieldPanel = new JPanel(new BorderLayout());
        textField = new JTextField();
        textField.addActionListener(actionListener);
        textFieldPanel.add(textField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.addActionListener(actionListener);
        textFieldPanel.add(sendButton, BorderLayout.EAST);
        content.add(textFieldPanel, BorderLayout.SOUTH);
        messageFrame.setSize(600, 400);
        messageFrame.setLocationRelativeTo(null);
        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageFrame.setVisible(true);
    }

    private void addMessage(String message) {
        String formattedMessage = "\n" + user.getName() + "*" + message;
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(messages, true))) {
            pw.print(formattedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageFrame.setVisible(false);
        displayMessages();
    }
}