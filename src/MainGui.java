import javax.swing.*;
import javax.swing.border.Border;
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
    private ArrayList<User> users;
    private Conversation conversationDisplayed;
    private File messages;
    private File usersFile = new File("UsersFile.txt");
    private JButton addButton;
    private JButton settingsButton;
    private JPanel loginInputPanel;
    private JPanel loginButtonsPanel;
    private JButton signUpButton;
    private JButton loginButton;
    private JButton signUpPageButton;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame loginFrame;
    private JFrame signUpFrame;
    private JFrame mainFrame;
    private JFrame messageFrame;
    private JFrame settingsFrame;
    private JFrame addConversationFrame;
    private JTextField textField;
    private JButton sendButton;
    private JPanel messagePanel;
    private JPanel usersPanel;
    private JTextField searchUsers;
    private JButton searchButton;

    private JButton homeButton;
    private JButton saveButton;
    private JButton logoutButton;
    private JButton deleteButton;
    private JTextField settingsNameField;
    private JTextField settingsUsernameField;
    private JTextField settingsPasswordField;
    private JLabel nameLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

//    JWindow mainWindow;
//    JWindow messageWindow;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                addConversation();
            } else if (e.getSource() == signUpButton) {
                loginFrame.setVisible(false);
                signUpFrame.setVisible(true);
            } else if (e.getSource() == loginButton) {
                loginFrame.setVisible(false);
                mainFrame.setVisible(true);
            } else if (e.getSource() == searchButton) {
                String searchedUser = searchUsers.getText();
                displaySearchMatches(searchedUser);
            } else if (e.getSource() == settingsButton) {
                settingsFrame.setVisible(true);
                mainFrame.setVisible(false);
            } else if (e.getSource() == sendButton) {
                String message = textField.getText();
                addMessage(message);
            } else if (e.getSource() == homeButton) {
                settingsFrame.setVisible(false);
                mainFrame.setVisible(true);
            } else if (e.getSource() == saveButton) {
                // Saves the inputted info
            } else if (e.getSource() == logoutButton) {
                // Stops the thread and creates a new one
                // Logs the user out
                settingsFrame.setVisible(false);
                loginFrame.setVisible(true);
            } else if (e.getSource() == deleteButton) {
                // Deletes the user
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
        users = new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainGui());
    }

    public void run() {
        /**
         * Main Login Screen
         */
        readConversationsFromFile();
        readUsers();
        try {
            User user = new User("John", "johnuser", "johnpass");
            users.add(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginFrame = new JFrame("Login");
        Container loginContent = loginFrame.getContentPane();
        loginContent.setLayout(new BorderLayout());

        JPanel loginButtonsPanel = new JPanel(new GridLayout(3, 2));
        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(actionListener);
        loginButtonsPanel.add(loginButton, BorderLayout.EAST);
        loginButtonsPanel.add(signUpButton, BorderLayout.WEST);
        loginContent.add(loginButtonsPanel, BorderLayout.SOUTH);

        JPanel loginInputPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username ");
        usernameLabel.setSize(10, 10);
        JLabel passwordLabel = new JLabel("Password: ");
        usernameField = new JTextField("");
        passwordField = new JPasswordField("");
        loginInputPanel.add(usernameLabel);
        loginInputPanel.add(usernameField);
        loginInputPanel.add(passwordLabel);
        loginInputPanel.add(passwordField);
        loginContent.add(loginInputPanel);

        loginFrame.setSize(600, 400);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
        /**
         * Sign Up Screen
         */
        signUpFrame = new JFrame("Sign Up");
        Container signUpContent = loginFrame.getContentPane();
        signUpContent.setLayout(new BorderLayout());

        JPanel signUpPanel = new JPanel(new BorderLayout());
        signUpPageButton = new JButton("Sign Up");
        signUpPageButton.addActionListener(actionListener);
//        signUpPanel.add(signUpPageButton, BorderLayout.CENTER);
        signUpContent.add(signUpPanel, BorderLayout.SOUTH);

        signUpFrame.setSize(600, 400);
        signUpFrame.setLocationRelativeTo(null);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setVisible(false);
        /**
         * Main Page
         */
        mainFrame = new JFrame("Messages");
        Container content = mainFrame.getContentPane();
        content.setLayout(new BorderLayout());

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
        addButton = new JButton("New Conversation");
        addButton.addActionListener(actionListener);
        bottomPanel.add(addButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomPanel.setBackground(Color.white);
        content.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(false);
        /**
         * Settings Page
         */
        settingsFrame = new JFrame("Settings");
        Container settingsContent = settingsFrame.getContentPane();
        settingsContent.setLayout(new BorderLayout());

        // Contains all the information fields to view and edit.
        // Includes the user's "Name", "Username", and "Password"
        JPanel infoPanel = new JPanel();
        nameLabel = new JLabel("Name");
        nameField = new JTextField(10);
        usernameLabel = new JLabel("Username");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField(10);
        infoPanel.add(nameLabel);
        infoPanel.add(nameField);
        infoPanel.add(usernameLabel);
        infoPanel.add(usernameField);
        infoPanel.add(passwordLabel);
        infoPanel.add(passwordField);
        settingsContent.add(infoPanel, BorderLayout.CENTER);

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
        settingsContent.add(buttonsPanel, BorderLayout.SOUTH);

        settingsFrame.setSize(600, 400);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsFrame.setVisible(false);
    }



    /**
     * Reading conversations from file into ArrayList
     */
    private void readConversationsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("ConversationFile.txt"))) {
            Conversation c = (Conversation) in.readObject();
            while (c != null) {
                conversations.add(c);
                c = (Conversation) in.readObject();
            }
        } catch (EOFException e) {
            //end of file
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads users from file into Arraylist
     */
    private void readUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(usersFile))) {
            User u = (User) in.readObject();
            while (u != null) {
                users.add(u);
                u = (User) in.readObject();
            }
        } catch (EOFException e) {
            //end of file
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display messages frame for conversation
     */
    private void displayMessages() {
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

    /**
     * Add message to conversation
     * @param message message to be added
     */
    private void addMessage(String message) {
    //    String formattedMessage = "\n" + user.getName() + "*" + message;
    //    try (PrintWriter pw = new PrintWriter(new FileOutputStream(messages, true))) {
    //        pw.print(formattedMessage);
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //    messagePanel.add(new JLabel(user.getName() + ": " + message));
        messageFrame.setVisible(true);
    }

    /**
     * Displays new frame that allows user to add a new conversation
     */
    private void addConversation() {
        addConversationFrame = new JFrame("New Conversation");
        Container content = addConversationFrame.getContentPane();
        content.setLayout(new BorderLayout());
        usersPanel = new JPanel();
        JScrollPane usersScrollPane = new JScrollPane(usersPanel);
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchUsers = new JTextField("Search for other users");
        searchUsers.addActionListener(actionListener);
        searchButton = new JButton("Search");
        searchButton.addActionListener(actionListener);
        searchPanel.add(searchUsers, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        content.add(searchPanel, BorderLayout.NORTH);
        content.add(usersScrollPane, BorderLayout.CENTER);
        addConversationFrame.setSize(600, 400);
        addConversationFrame.setLocationRelativeTo(null);
        addConversationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addConversationFrame.setVisible(true);
    }

    /**
     * Displays the users that match the user's search
     *
     * @param name name being searched
     */
    private void displaySearchMatches(String name) {
        usersPanel.removeAll();
        ArrayList<User> userMatches = new ArrayList<>();
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) {
                userMatches.add(u);
            } else if (u.getUsername().equalsIgnoreCase(name)) {
                userMatches.add(u);
            }
        }
        for (User u : userMatches) {
            usersPanel.add(new JButton(u.getName()));
        }
        usersPanel.revalidate();
        usersPanel.repaint();
        addConversationFrame.setVisible(true);
    }
}