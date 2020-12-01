import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CS 180 Project 5 -- MainGui.java
 * <p>
 * Main GUI that interacts with user
 */
public class MainGui extends JComponent implements Runnable {
    private ArrayList<Conversation> conversations;
    private ArrayList<User> users;
    private ArrayList<User> userMatches;
    private ArrayList<User> usersToAdd;
    private ArrayList<String> messagesArr = new ArrayList<>();
    private Conversation conversationDisplayed;
    private File messages;
    private File usersFile = new File("UsersFile.txt");
    private JButton addButton;
    private JButton settingsButton;
    private JButton signUpButton;
    private JButton loginButton;
    private JButton signUpPageButton;
    private JButton signupToLogin;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField createNameField;
    private JTextField createUsernameField;
    private JPasswordField createPasswordField;
    private JFrame loginFrame;
    private JFrame signUpFrame;
    private JFrame mainFrame;
    private JFrame messageFrame;
    private JFrame editMessageFrame;
    private JFrame addConversationFrame;
    private JFrame addConversationFields;
    private JTextField textField;
    private JButton sendButton;
    private JButton backButton;
    private JButton editMessBackButton;
    private JButton editMessagesButton;
    private JButton deleteMessageButton;
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
//    public static BufferedReader bfr;
    public static PrintWriter outputToServer;
    public static ObjectInputStream obj;

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
            } else if (e.getSource() == signupToLogin) {
                loginFrame.setVisible(true);
                signUpFrame.setVisible(false);
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
                //settings gui
            } else if (e.getSource() == sendButton) {
                String message = textField.getText();
                textField.setText(null);
                addMessage(message);
            } else if (e.getSource() == backButton) {
                messageFrame.setVisible(false);
                mainFrame.setVisible(true);
            } else if (e.getSource() == editMessagesButton) {
                messageFrame.setVisible(false);
                editMessages();
            } else if (e.getSource() == editMessBackButton) {
                editMessageFrame.setVisible(false);
                displayMessages();
            } else {
                int index = Integer.parseInt(e.getActionCommand());
                conversationDisplayed = conversations.get(index);
                displayMessages();
            }
        }
    };

//    ActionListener deleteConversation = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            int index = Integer.parseInt(e.getActionCommand());
//            conversationDisplayed = conversations.get(index);
//            deleteConversation();
//            mainScreen();
//        }
//    };

    ActionListener deleteMessageAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = Integer.parseInt(e.getActionCommand());
            deleteMessage(index);
            editMessages();
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

    public MainGui() {
        conversations = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 8080);
//            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputToServer = new PrintWriter(socket.getOutputStream(), true);
            obj = new ObjectInputStream(socket.getInputStream());
        } catch (IOException i) {
            JOptionPane.showMessageDialog(null, "Error connecting to Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(new MainGui());
    }

    public void run() {
        /**
         * Main Login Screen
         */

        loginFrame = new JFrame("Login");
        Container loginContent = loginFrame.getContentPane();
        loginContent.setLayout(null);

        JLabel userName = new JLabel("Username");
        userName.setFont(new Font("Arial", Font.PLAIN, 20));
        userName.setSize(100, 30);
        userName.setLocation(300, 195);
        loginContent.add(userName);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        usernameField.setSize(190, 20);
        usernameField.setLocation(400, 200);
        loginContent.add(usernameField);

        JLabel passWord = new JLabel("Password ");
        passWord.setFont(new Font("Arial", Font.PLAIN, 20));
        passWord.setSize(100, 30);
        passWord.setLocation(300, 265);
        loginContent.add(passWord);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setSize(190, 20);
        passwordField.setLocation(400, 270); //285
        loginContent.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loginButton.setSize(100, 20);
        loginButton.setLocation(360, 320);
        loginButton.addActionListener(actionListener);
        loginContent.add(loginButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 15));
        signUpButton.setSize(100, 20);
        signUpButton.setLocation(360, 360);
        signUpButton.addActionListener(actionListener);
        loginContent.add(signUpButton);

        loginFrame.setSize(900, 600);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
    }

    private void logIn() {
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
//            success = Boolean.parseBoolean(bfr.readLine());
            success = obj.readBoolean();
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

        signUpContent.setLayout(null);
        JLabel fullNameLabel = new JLabel("Full Name ");
        fullNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        fullNameLabel.setSize(100, 20);
        fullNameLabel.setLocation(300, 170);
        signUpContent.add(fullNameLabel);

        createNameField = new JTextField();
        createNameField.addActionListener(actionListener);
        createNameField.setFont(new Font("Arial", Font.PLAIN, 15));
        createNameField.setSize(190, 20);
        createNameField.setLocation(400, 170);
        signUpContent.add(createNameField, BorderLayout.CENTER);

        JLabel userName = new JLabel("Username");
        userName.setFont(new Font("Arial", Font.PLAIN, 20));
        userName.setSize(100, 20);
        userName.setLocation(300, 200); //350
        signUpContent.add(userName, BorderLayout.CENTER);

        createUsernameField = new JTextField();
        createUsernameField.addActionListener(actionListener);
        createUsernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        createUsernameField.setSize(190, 20);
        createUsernameField.setLocation(400, 200);
        signUpContent.add(createUsernameField, BorderLayout.CENTER);

        JLabel passWord = new JLabel("Password ");
        passWord.setFont(new Font("Arial", Font.PLAIN, 20));
        passWord.setSize(100, 20);
        passWord.setLocation(300, 230);
        signUpContent.add(passWord);

        createPasswordField = new JPasswordField();
        createPasswordField.addActionListener(actionListener);
        createPasswordField.setFont(new Font("Arial", Font.PLAIN, 15));
        createPasswordField.setSize(190, 20);
        createPasswordField.setLocation(400, 230);
        signUpContent.add(createPasswordField);

        signUpPageButton = new JButton("Sign Up");
        signUpPageButton.setFont(new Font("Arial", Font.PLAIN, 15));
        signUpPageButton.setSize(100, 20);
        signUpPageButton.setLocation(360, 320);
        signUpPageButton.addActionListener(actionListener);
        signUpContent.add(signUpPageButton);

        signupToLogin = new JButton("Login"); //Sruthi
        signupToLogin.setFont(new Font("Arial", Font.PLAIN, 15));
        signupToLogin.setSize(100, 20);
        signupToLogin.setLocation(360, 350);
        signupToLogin.addActionListener(actionListener);
        signUpContent.add(signupToLogin);

        signUpFrame.setSize(900, 600);
        signUpFrame.setLocationRelativeTo(null);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setVisible(true);
    }

    private void signUp() {
        readUsers();
        String username = createUsernameField.getText();
        String password = createPasswordField.getText();
        String name = createNameField.getText();
        if (username == null || ("".equals(username))
                || ("".equals(password)) || password == null
                || ("".equals(name)) || name == null) {
            JOptionPane.showMessageDialog(null, "Please enter all details.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            signUpFrame.setVisible(false);
            displaySignUp();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("SignUp*");
            sb.append(name + "*");
            sb.append(username + "*");
            sb.append(password);

            outputToServer.println(sb.toString());
            readUsers();

            try {
                //changed
                if (obj.readBoolean()) {
                    signUpFrame.setVisible(false);
                    loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username", "Signup Error", JOptionPane.ERROR_MESSAGE);
                    signUpFrame.setVisible(false);
                    displaySignUp();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
//        JPanel textFieldPanel = new JPanel(new BorderLayout());
        JPanel textFieldPanel = new JPanel();
        textField = new JTextField(30);
        textField.addActionListener(actionListener);
        textFieldPanel.add(textField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.addActionListener(actionListener);
        backButton = new JButton("Back");
        backButton.addActionListener(actionListener);
        editMessagesButton = new JButton("Edit");
        editMessagesButton.addActionListener(actionListener);
        textFieldPanel.add(backButton);
        textFieldPanel.add(editMessagesButton);
        textFieldPanel.add(textField);
        textFieldPanel.add(sendButton);
        content.add(textFieldPanel, BorderLayout.SOUTH);
        messageFrame.setSize(600, 400);
        messageFrame.setLocationRelativeTo(null);
        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageFrame.setVisible(true);
    }

    private void editMessages() {
        editMessageFrame = new JFrame(conversationDisplayed.getName());

        Container content = editMessageFrame.getContentPane();
        content.setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;

        outputToServer.println("EditMessage*" + conversationDisplayed.getName());
        try {
            int size = obj.readInt();
            messagesArr.clear();
            for (int i = 0; i < size; i++) {
                messagesArr.add((String) obj.readObject());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (String line : messagesArr) {
            String[] output = line.split("\\*");
            constraints.gridwidth = 2;
            constraints.gridx = 0;
            constraints.ipadx = 300;
            String formattedMessage = output[0] + ": " + output[1];
            messagePanel.add(new JLabel(formattedMessage), constraints);
            boolean deletable = Boolean.parseBoolean(output[2]);
            String index = output[3];
            if (deletable) {
                deleteMessageButton = new JButton("Delete");
                deleteMessageButton.setActionCommand(index);
                deleteMessageButton.addActionListener(deleteMessageAL);
                constraints.gridwidth = 1;
                constraints.gridx = 2;
                constraints.ipadx = 0;
                messagePanel.add(deleteMessageButton, constraints);
            } else {
                JLabel noDeleteMessage = new JLabel("Cannot Delete");
                constraints.gridwidth = 1;
                constraints.gridx = 2;
                constraints.ipadx = 0;
                messagePanel.add(noDeleteMessage, constraints);
            }
        }
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        content.add(scrollPane, BorderLayout.NORTH);

        //messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        //JScrollPane scrollPane = new JScrollPane(messagePanel);
        content.add(scrollPane, BorderLayout.CENTER);
        //JPanel textFieldPanel = new JPanel(new BorderLayout());
        JPanel textFieldPanel = new JPanel();

        editMessBackButton = new JButton("Back");
        editMessBackButton.addActionListener(actionListener);

        //JPanel homeEditPanel = new JPanel(new BorderLayout());
        textFieldPanel.add(editMessBackButton);
        //textFieldPanel.add(editMessagesButton);
        //textFieldPanel.add(textField);
        //textFieldPanel.add(sendButton);
        content.add(textFieldPanel, BorderLayout.SOUTH);
        //content.add(homeEditPanel, BorderLayout.WEST);

        editMessageFrame.setSize(600, 400);
        editMessageFrame.setLocationRelativeTo(null);
        editMessageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editMessageFrame.setVisible(true);
    }

    private void deleteMessage(int index) {
        editMessageFrame.setVisible(false);
        outputToServer.println("DeleteMessage*" + String.valueOf(index));
        messagesArr.remove(index);
        readConversationsFromFile();
    }

    /**
     * Add message to conversation
     *
     * @param message message to be added
     */
    private void addMessage(String message) {
        String formattedMessage = "\n" + user.getUsername() + "*" + message;
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(messages, true))) {
            pw.print(formattedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messagePanel.add(new JLabel(user.getUsername() + ": " + message));
        messageFrame.setVisible(true);
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
            usersToAdd = new ArrayList<>();
            int size = obj.readInt();
            for (int i = 0; i < size; i++) {
                usersToAdd.add((User) obj.readObject());
            }
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        String names = "";
        for (User u : usersToAdd) {
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
//            successfulAddition = Boolean.parseBoolean(bfr.readLine());
            successfulAddition = obj.readBoolean();
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

//    public class AppendingObjectOutputStream extends ObjectOutputStream {
//
//        public AppendingObjectOutputStream(OutputStream out) throws IOException {
//            super(out);
//        }
//
//        @Override
//        protected void writeStreamHeader() throws IOException {
//            reset();
//        }
//
//    }
}