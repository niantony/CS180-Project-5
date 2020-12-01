import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * CS 180 Project 5 -- MainGui.java
 * <p>
 * Main GUI that interacts with user
 */
public class MainGui extends JComponent implements Runnable {
    private static MainGui clientConnection;
    private ArrayList<Conversation> conversations;
    private ArrayList<User> users;
    private ArrayList<User> userMatches;
    private ArrayList<User> usersToAdd;
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
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField createNameField;
    private JTextField createUsernameField;
    private JPasswordField createPasswordField;
    private JFrame loginFrame;
    private JFrame signUpFrame;
    private JFrame mainFrame;
    private JFrame messageFrame;
    private JFrame addConversationFrame;
    private JFrame addConversationFields;
    private JTextField textField;
    private JButton sendButton;
    private JPanel messagePanel;
    private JPanel usersPanel;
    private JTextField searchUsers;
    private JButton searchButton;
    private JButton submitFields;
    private JButton addOtherUsers;
    private JTextField conversationNameField;
    private User user;
    private boolean successfulLogin = true;
    private boolean successfulAdditionToFile;

    public static Socket socket;
    public static BufferedReader bfr;
    public static PrintWriter outputToServer;
    public static ObjectInputStream obj;

    private String host;
    private int port;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                usersToAdd = new ArrayList<>();
                outputToServer.println("AddConversation*");
                addConversation();
            } else if (e.getSource() == signUpButton) {
                loginFrame.setVisible(false);
                displaySignUp();
            } else if (e.getSource() == signUpPageButton) {
                signUp();
            } else if (e.getSource() == submitFields) {
                successfulAdditionToFile = addConversationToFile();
                if (!successfulAdditionToFile) {
                    addConversationFields.setVisible(true);
                }

            } else if (e.getSource() == loginButton) {
                //send message to server login*username*password
                loginFrame.setVisible(false);
                logIn();

                if (successfulLogin) {
                    mainScreen();

                } else {
                    loginFrame.setVisible(true);
                }

            } else if (e.getSource() == addOtherUsers) {
                addConversationFields.setVisible(false);
                addConversation();
            } else if (e.getSource() == searchButton) {
                String searchedUser = searchUsers.getText();
                displaySearchMatches(searchedUser);

            } else if (e.getSource() == settingsButton) {
//                try {
//                    socket.close();
//                    bfr.close();
//                    outputToServer.close();
//                    obj.close();
//                } catch (IOException exception) {
//                    exception.printStackTrace();
//                }
            } else if (e.getSource() == sendButton) {
                String message = textField.getText();
                addMessage(message);
                clientConnection.start();
            } else {
                int index = Integer.parseInt(e.getActionCommand());
                conversationDisplayed = conversations.get(index);
                displayMessages();
            }
        }
    };

    ActionListener addUserToConversation = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            User userToBeAdded = userMatches.get(Integer.parseInt(e.getActionCommand()));
            addConversationFrame.setVisible(false);
            fillConversationFields(userToBeAdded);
        }
    };

    private boolean success;

    public MainGui(String host, int port) {
        this.host = host;
        this.port = port;
        conversations = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static void main(String[] args) {
        clientConnection = new MainGui("localhost", 8080);
        clientConnection.run();
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            try {
                System.out.println("Time task running");
                readUsers();
                readConversationsFromFile();
                String messageToAdd = bfr.readLine();
                if (messageToAdd.contains("update*")) {
                    System.out.println("Update message received");
                    System.out.println(messageToAdd);
                    if (messageFrame.isActive()) {
                        String[] parsedMessage = messageToAdd.split("\\*");
                        String nameOfSender = parsedMessage[1];
                        String message = parsedMessage[2];
                        messagePanel.add(new JLabel(nameOfSender + ": " + message));
                        System.out.println("added to panel");
                        messageFrame.setVisible(true);
                    } else {
                        task.cancel();
                    }
                } else {
                    task.cancel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void start() {
        timer.scheduleAtFixedRate(task, 20000, 2000);
    }

    public void run() {
        try {
            try {
                this.socket = new Socket(host, port);
                bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputToServer = new PrintWriter(socket.getOutputStream(), true);
                obj = new ObjectInputStream(socket.getInputStream());
                System.out.println("Connected to server");
            } catch (IOException i) {
                System.out.println("Error connecting to server");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Main Login Screen
         */
        loginFrame = new JFrame("Login");
        Container loginContent = loginFrame.getContentPane();
        loginContent.setLayout(new BorderLayout());

        loginButtonsPanel = new JPanel(new GridLayout(3, 2));
        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(actionListener);
        loginButtonsPanel.add(loginButton, BorderLayout.EAST);
        loginButtonsPanel.add(signUpButton, BorderLayout.WEST);
        loginContent.add(loginButtonsPanel, BorderLayout.SOUTH);

        loginInputPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username ");
        usernameLabel.setSize(10, 10);
        JLabel passwordLabel = new JLabel("Password: ");
        usernameField = new JTextField();
        usernameField.addActionListener(actionListener);
        passwordField = new JPasswordField();
        passwordField.addActionListener(actionListener);
        loginInputPanel.add(usernameLabel);
        loginInputPanel.add(usernameField);
        loginInputPanel.add(passwordLabel);
        loginInputPanel.add(passwordField);
        loginContent.add(loginInputPanel);

        loginFrame.setSize(600, 400);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);

    }

    private void logIn() {
//        try {
//            obj.reset();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean success = false;

        readUsers();
        StringBuilder sb = new StringBuilder();
        sb.append("LogIn*");
        sb.append(username + "*");
        sb.append(password);

        outputToServer.println(sb.toString());
        try {
            success = Boolean.parseBoolean(bfr.readLine());
        } catch (IOException e) {
            System.out.println("no response");
        }

        if (!success) {
            JOptionPane.showMessageDialog(null, "Wrong username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            successfulLogin = false;
        } else {
            successfulLogin = true;
            conversations = new ArrayList<>();
            users = new ArrayList<>();
//            try {
//                user = (User) obj.readObject();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            readUsers();
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    user = u;
                }
            }
            readConversationsFromFile();
        }
    }

    private void displaySignUp() {
        signUpFrame = new JFrame("Sign Up");
        Container signUpContent = signUpFrame.getContentPane();
        signUpContent.setLayout(new BorderLayout());

        JLabel usernameLabel = new JLabel("Username ");
        usernameLabel.setSize(10, 10);
        JLabel passwordLabel = new JLabel("Password: ");
        JLabel nameLabel = new JLabel("Full Name: ");
        JPanel signUpPanel = new JPanel(new GridLayout(4, 2));
        createUsernameField = new JTextField();
        createUsernameField.addActionListener(actionListener);
        signUpPanel.add(usernameLabel);
        signUpPanel.add(createUsernameField);
        createPasswordField = new JPasswordField();
        createPasswordField.addActionListener(actionListener);
        signUpPanel.add(passwordLabel);
        signUpPanel.add(createPasswordField);
        createNameField = new JTextField();
        createNameField.addActionListener(actionListener);
        signUpPanel.add(nameLabel);
        signUpPanel.add(createNameField);

        signUpPageButton = new JButton("Sign Up");
        signUpPageButton.addActionListener(actionListener);
        signUpPanel.add(new JLabel());
        signUpPanel.add(signUpPageButton);
        signUpContent.add(signUpPanel, BorderLayout.CENTER);

        signUpFrame.setSize(600, 400);
        signUpFrame.setLocationRelativeTo(null);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setVisible(true);
    }

    private void signUp() {
        readUsers();
        String username = createUsernameField.getText();
//        for (User u : users) {
//            if (u.getUsername().equals(username)) {
//                //invalid username
//                JOptionPane.showMessageDialog(null, "Invalid username", "Signup Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
        String password = createPasswordField.getText();
        String name = createNameField.getText();

        StringBuilder sb = new StringBuilder();
        sb.append("SignUp*");
        sb.append(name + "*");
        sb.append(username + "*");
        sb.append(password);

        outputToServer.println(sb.toString());

//        String fileName = username + ".txt";
//        File file = new File(fileName);
//        User newUser = null;

//        try {
//            file.createNewFile();
//            newUser = new User(name, username, password, file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        if (users.isEmpty()) {
//            try {
//                usersFile.createNewFile();
//            } catch (IOException e) {
//                //unable to create file
//            }
//
//            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {
//                out.writeObject(newUser);
//                out.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {
//                for (User u : users) {
//                    out.writeObject(u);
//                    out.flush();
//                }
//
//                out.writeObject(newUser);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (!usersFile.exists()) {
//            try {
//                usersFile.createNewFile();
//                newFileCreated = true;
//            } catch (IOException e) {
//                //unable to create file
//            }
//        }
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile, true))) {
//            if (newFileCreated) {
//                out.writeObject(newUser);
//                out.flush();
//                newFileCreated = false;
//            } else {
//                try (AppendingObjectOutputStream appendingStream = new AppendingObjectOutputStream(out)) {
//                    appendingStream.writeObject(newUser);
//                    appendingStream.flush();
//                } catch (IOException e) {
//                    //unable to append to file
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            //unable to add to file
//            e.printStackTrace();
//        }
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile, true))) {
//            out.writeObject(newUser);
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        readUsers();

        try {
            if (Boolean.parseBoolean(bfr.readLine())) {
                signUpFrame.setVisible(false);
                loginFrame.setVisible(true);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Displays main screen
     */
    private void mainScreen() {
        mainFrame = new JFrame("Messages");
        Container content = mainFrame.getContentPane();
        content.setLayout(new BorderLayout());

        readConversationsFromFile();
//        readUsers();
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
        mainFrame.setVisible(true);
    }

    /**
     * Reading conversations from file into ArrayList
     */
    private void readConversationsFromFile() {
        conversations = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(user.getConversations()))) {
            Conversation c = (Conversation) in.readObject();
            while (c != null) {
                conversations.add(c);
                c = (Conversation) in.readObject();
            }
        } catch (EOFException | NullPointerException e) {
            //end of file
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads users from file into Arraylist
     */
    private void readUsers() {
        users = new ArrayList<>();
//        try {
//            FileInputStream fis = new FileInputStream(usersFile);
//            boolean done = false;
//            while (!done) {
//                ObjectInputStream in = new ObjectInputStream(fis);
//                try {
//                    User u = (User) in.readObject();
//                    users.add(u);
//                } catch (NullPointerException | EOFException e) {
//                    done = true;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (EOFException e) {
//            //end of file
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(usersFile))) {
            User u = (User) in.readObject();
            while (u != null) {
                users.add(u);
                u = (User) in.readObject();
            }
        } catch (EOFException | FileNotFoundException e) {
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
                if (!line.equals("")) {
                    String[] userAndMessage = line.split("\\*");
                    user = userAndMessage[0];
                    message = userAndMessage[1];
                    messagePanel.add(new JLabel(user + ": " + message));
                }
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
     *
     * @param message message to be added
     */
    private void addMessage(String message) {
        boolean success = false;
        outputToServer.println(messages);
        System.out.println("Sent " + messages);
        String formattedMessage = "Message*" + user.getName() + "*" + message;
        System.out.println("Sent " + formattedMessage);
        outputToServer.println(formattedMessage);

        try {
            success = Boolean.parseBoolean(bfr.readLine());
        } catch (IOException e) {
            System.out.println("no response");
        }
        if (!success) {
            JOptionPane.showMessageDialog(null, "Couldn't log the message", "Message Error", JOptionPane.ERROR_MESSAGE);
        } else {
            messagePanel.add(new JLabel(user.getName() + ": " + message));
            outputToServer.println("update*" + user.getName() + "*"+ message);
            System.out.println("update message sent to server");
            messageFrame.setVisible(true);
        }
    }

    /**
     * Displays new frame that allows user to add a new conversation
     */
    private void addConversation() {
//        readConversationsFromFile();
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
        name = name.toLowerCase().trim();
        userMatches = new ArrayList<>();

        outputToServer.println("SearchUser*" + name);

        try {
            userMatches = (ArrayList<User>) obj.readObject();
        } catch (IOException i) {
            System.out.println("Connection failed while searching for a user");
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found whilst searching for a user");
        }

        for (int i = 0; i < userMatches.size(); i++) {
            JButton userButton = new JButton(userMatches.get(i).getName());
            userButton.setActionCommand(String.valueOf(i));
            userButton.addActionListener(addUserToConversation);
            usersPanel.add(userButton);
        }

        usersPanel.revalidate();
        usersPanel.repaint();
        addConversationFrame.setVisible(true);
    }

    private void fillConversationFields(User otherUser) {
        addConversationFields = new JFrame("Add new conversation");
        Container content = addConversationFields.getContentPane();
        content.setLayout(new BorderLayout());

        JPanel fieldsToFill = new JPanel(new GridLayout(3, 2));
        JPanel fillFieldButtons = new JPanel(new GridLayout(1, 2));
        submitFields = new JButton("Create Conversation");
        submitFields.addActionListener(actionListener);

        addOtherUsers = new JButton("Add More Users");
        addOtherUsers.addActionListener(actionListener);
        fillFieldButtons.add(addOtherUsers);
        fillFieldButtons.add(submitFields);

        content.add(fillFieldButtons, BorderLayout.SOUTH);
        JLabel nameLabel = new JLabel("Name of Conversation: ");
        nameLabel.setSize(10, 10);
        fieldsToFill.add(nameLabel);
        conversationNameField = new JTextField();
        conversationNameField.addActionListener(actionListener);
        fieldsToFill.add(conversationNameField);
//        if (!usersToAdd.contains(user)) {
//            usersToAdd.add(user);
//        }
//        usersToAdd.add(otherUser);
        outputToServer.println("AddUserToConversation*" + otherUser.getUsername());
        try {
            usersToAdd = null;
            usersToAdd = (ArrayList<User>) obj.readObject();
            for (User u : usersToAdd) {
                System.out.println("1 " + u.getName());
            }
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        String names = "";
        for (User u : usersToAdd) {
            System.out.println(u.getName());
            if (!u.getUsername().equals(user.getUsername())) {
                if (names.equals("")) {
                    names += u.getName();
                } else {
                    names += ", " + u.getName();
                }
            }
        }
        JLabel otherUserLabel = new JLabel("Conversation members: ");
        JLabel otherUserName = new JLabel(names);
        fieldsToFill.add(otherUserLabel);
        fieldsToFill.add(otherUserName);
        content.add(fieldsToFill, BorderLayout.CENTER);
        addConversationFields.setSize(600, 400);
        addConversationFields.setLocationRelativeTo(null);
        addConversationFields.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addConversationFields.setVisible(true);
    }

    private boolean addConversationToFile() {
        readConversationsFromFile();
        String nameOfConversation = conversationNameField.getText();
        outputToServer.println("CreateConversation*" + nameOfConversation);
        boolean successfulAddition = false;
        try {
            successfulAddition = Boolean.parseBoolean(bfr.readLine());
            System.out.println(successfulAddition);
//            conversations = (ArrayList<Conversation>) obj.readObject();
        } catch (IOException e) {
            System.out.println("Failed to add conversation");
            e.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            System.out.println("Failed to add conversation");
//            e.printStackTrace();
//        }
        if (!successfulAddition) {
            JOptionPane.showMessageDialog(null, "Invalid conversation name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        readConversationsFromFile();
        addConversationFields.setVisible(false);
        mainFrame.setVisible(false);
        mainScreen();
        mainFrame.setVisible(true);
        return true;
    }

    private void writeConversationsToFile() {
        if (conversations.isEmpty()) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(user.getConversations(), false))) {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(user.getConversations()))) {
                for (Conversation c : conversations) {
                    out.writeObject(c);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeUsersToFile() {
        if (users.isEmpty() && user == null) {
            usersFile.delete();
        } else {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {
                for (User u : users) {
                    out.writeObject(u);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}