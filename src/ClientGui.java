import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CS 180 Project 5 -- ClientGui.java
 *
 * Main GUI that interacts with user -
 * The program allows the user to create and delete conversations with one or multiple users in the server and create,
 * edit, and delete messages during a conversation. It also allows the user to create, edit, and delete their accounts.
 * All data/messages persists after the program closes through files.
 *
 * We referenced the documentation/tutorials for object output streams, object input streams, sockets, BoxLayout,
 * and GridBagLayout. We also referenced Stack Overflow for advice on appending using object output streams, clearing
 * a jpanel, and adding vertically to a panel using BoxLayout.
 * We visited the office hours for Albert Yu to discuss a problem with our Object Output Stream not returning the right
 * object.
 *
 * @author Antony Ni, G17
 * @author Ishika Vachali, Online
 * @author Michael Con, Online
 * @author Sruthi Koukuntla, LC3
 *
 * @version December 6, 2020
 */
public class ClientGui extends JComponent implements Runnable {

    private ArrayList<Conversation> conversations;  //arraylist of all of the current user's conversations
    private ArrayList<User> users;  //arraylist of all of the users on the server
    private ArrayList<User> userMatches;  //arraylist of the users that matched the current user's search (adding conv)
    private ArrayList<User> usersToAdd;  //arraylist of the users to add to a new conversation
    private ArrayList<String> messagesArr = new ArrayList<>();  //arraylist of all of the messages on the edit screen
    private Conversation conversationDisplayed;  //the conversation being displayed
    private File messages;  //the file with the messages of the conversation displayed
    private File usersFile = new File("UsersFile.txt");  //the file with all of the users in the server
    private JButton addButton;  //button to add a conversation
    private JButton settingsButton;  //button to open the settings screen
    private JButton signUpButton;  //button to sign up as a new user
    private JButton loginButton;  //button to log in
    private JButton signUpPageButton;  //button on sign up page to submit information
    private JButton signupToLogin;  //button tat takes the user from the sign up page to the login page
    private JTextField usernameField;  //text field on login page for username
    private JPasswordField passwordField;  //text field on login page for password
    private JTextField createNameField;  //text field on sign up page to create name
    private JTextField createUsernameField;  //text field on sign up page to create username
    private JPasswordField createPasswordField;  //text field on sign up page to create password
    private JFrame loginFrame;  //frame for login page
    private JFrame signUpFrame;  //frame for signup page
    private JFrame mainFrame;  //frame for main page containing all of the conversations
    private JFrame messageFrame;  //frame for messages page for the displayed conversation
    private JFrame editMessageFrame;  //frame for editing page for the displayed conversation
    private JFrame addConversationFrame;  //frame for searching for users to add to a new conversation
    private JFrame addConversationFields;  //frame for submitting fields to create a new conversation
    private JTextField textField;  //text field within messages frame where users can add new messages
    private JButton sendButton;  //button to send message
    private JButton backButton;  //button to return to main screen from messages page
    private JButton editMessBackButton;  //button to return to messages page from edit page
    private JButton editMessagesButton;  //button to go to edit messages page
    private JButton deleteMessageButton;  //button to delete specific message
    private JButton editSpecificMsgButton;  //button to edit specific message
    private JPanel messagePanel;  //panel for messages for displayed conversation
    private JPanel usersPanel;  //panel that displays matched users during search
    private JTextField searchUsers;  //text field to search for other users while adding a new conversation
    private JButton searchButton;  //button to search for other users while adding a new conversation
    private JButton submitFields;  //button to submit conversation fields when creating new conversation
    private JButton addOtherUsers;  //button to add more users to a new conversation
    private JTextField conversationNameField;  //text field for the name of the new conversation
    private User user;  //current user
    private boolean successfulLogin;  //boolean containing whether the login was successful
    private boolean successfulAdditionToFile;  //boolean containing whether new conversation was added successfully
    private Container messageContent;  //container for frame containing messages for the conversation displayed
    private Timer timer;  //timer for the messages panel
    private Timer timerMain;  //timer for the main screen
    private JPanel conversationPanel;  //panel displaying the user's conversations on main screen

    //settings
    private JFrame settingsFrame;  //frame for the settings page
    private JButton homeButton;  //button to return to main screen
    private JButton saveButton;  //button to save edits to account
    private JButton logoutButton;  //button to logout of account
    private JButton deleteAccountButton;  //button to delete account
    private JTextField nameField;  //text field to enter a new name for account
    private JLabel nameLabel;  //label before name text field
    private JLabel usernameLabel;  //label before username label
    private JLabel passwordLabel;  //label before password text field
    
    JButton deleteConvButton;  //button to delete conversations
    public static Socket socket;  //socket connected to server
    public static PrintWriter outputToServer;  //print writer to send messages to server
    public static ObjectInputStream obj;  //object input stream to read in messages from server

    /**
     * Main action listener for login screen, signup screen, main screen, messages screen, and adding conversation
     * screens
     */
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

                    timerMain = new Timer(100, updateMain);
                    timerMain.setDelay(1900);
                    timerMain.start();

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
                settings();
                mainFrame.setVisible(false);
            } else if (e.getSource() == sendButton) {
                if (!textField.getText().isEmpty()) {
                    String message = textField.getText();
                    textField.setText(null);
                    addMessage(message);
                    displayMessages();
                }
            } else if (e.getSource() == backButton) {
                messageFrame.setVisible(false);
                mainPanel();
                mainFrame.setVisible(true);
                messageFrame.dispose();
            } else if (e.getSource() == editMessagesButton) {
                messageFrame.setVisible(false);
                editMessages();
                messageFrame.dispose();
            } else if (e.getSource() == editMessBackButton) {
                editMessageFrame.setVisible(false);
                displayConversation();
            } else {
                int index = Integer.parseInt(e.getActionCommand());
                conversationDisplayed = conversations.get(index);
                displayConversation();

                timer = new Timer(100, update);
                timer.setDelay(1900);
                timer.start();
            }
        }
    };

    /**
     * Action listener for settings page
     */
    ActionListener settingsAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == deleteAccountButton) {
                settingsFrame.setVisible(false);
                deleteAccount();
            } else if (e.getSource() == homeButton) {
                settingsFrame.setVisible(false);
                //mainScreen();
                mainFrame.setVisible(true);
            } else if (e.getSource() == saveButton) {
                saveSettings();
            } else if (e.getSource() == logoutButton) {
                settingsFrame.setVisible(false);
                JOptionPane.showMessageDialog(null, "Logging out...", "Log Out",
                        JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        }
    };

    /**
     * Action listener for deleting specific messages
     */
    ActionListener deleteMessageAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = Integer.parseInt(e.getActionCommand());
            deleteMessage(index);
            editMessages();
        }
    };

    /**
     * Action listener for deleting conversations
     */
    ActionListener deleteConversation = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = Integer.parseInt(e.getActionCommand());
            conversationDisplayed = conversations.get(index);
            deleteConversation();
        }
    };

    /**
     * Action listener for editing specific messages
     */
    ActionListener editSpecificMessageAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = Integer.parseInt(e.getActionCommand());
            editSpecificMessage(index);
            editMessages();
        }
    };

    /**
     * Action listener to add users to new conversations
     */
    ActionListener addUserToConversation = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            User userToBeAdded = userMatches.get(Integer.parseInt(e.getActionCommand()));
            addConversationFrame.setVisible(false);
            fillConversationFields(userToBeAdded);
        }
    };

    /**
     * Action listener to update messages frame for the conversation displayed
     */
    ActionListener update = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayMessages();

        }
    };

    /**
     * Action listener to update main screen displaying all of the user's conversations
     */
    ActionListener updateMain = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel();
        }
    };

    /**
     * Constructor for ClientGui
     */
    public ClientGui() {
        conversations = new ArrayList<>();
        users = new ArrayList<>();
    }

    /**
     * Main method
     * @param args main method arguments
     */
    public static void main(String[] args) {
        try {
            //Create a socket for the user and sets the host to localhost and port 8080
            socket = new Socket("localhost", 8080);
            outputToServer = new PrintWriter(socket.getOutputStream(), true);
            obj = new ObjectInputStream(socket.getInputStream());
            System.out.println("You connected to the server from address " +
                    socket.getLocalAddress() + " and port " + socket.getLocalPort());
        } catch (IOException i) {
            JOptionPane.showMessageDialog(null, "Error connecting to Server",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(new ClientGui());
    }

    /**
     * Method that starts gui and displays the login screen
     */
    public void run() {
        //Main Login Screen
        loginFrame = new JFrame("Login");
        Container loginContent = loginFrame.getContentPane();  //container for login screen
        loginContent.setLayout(null);

        JLabel userName = new JLabel("Username");  //label for username text field
        userName.setFont(new Font("Arial", Font.PLAIN, 20));
        userName.setSize(100, 30);
        userName.setLocation(300, 220);
        loginContent.add(userName);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        usernameField.setSize(190, 20);
        usernameField.setLocation(400, 227);
        loginContent.add(usernameField);

        JLabel passWord = new JLabel("Password ");  //label for password text field
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

        loginFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        loginFrame.pack();

        JLabel label1 = new JLabel();  //welcome label on login screen
        label1.setText("Welcome!");
        label1.setBounds(395, 75, 200, 50);
        label1.setFont(new Font("Verdana", 1, 20));

        JLabel label2 = new JLabel();  //label with directions on login screen
        label2.setText("login in below or create an account");
        label2.setBounds(295, 115, 350, 50);
        label2.setFont(new Font("Verdana", 1, 15));

        loginFrame.setSize(900, 600);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.add(label1);
        loginFrame.add(label2);
        loginFrame.setVisible(true);
    }

    /**
     * Method to log the user in
     */
    private void logIn() {
        String username = usernameField.getText();  //value entered in the username field
        String password = passwordField.getText();  //value entered in password field
        successfulLogin = false;
        
        if (username.isEmpty() || password.isEmpty()) {
            successfulLogin = false;
            JOptionPane.showMessageDialog(null, "Please enter all of the fields",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        readUsers();
        StringBuilder sb = new StringBuilder();
        sb.append("LogIn*");
        sb.append(username + "*");
        sb.append(password);
        
        outputToServer.println(sb.toString());
        try {
            successfulLogin = obj.readBoolean();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error with login. Try again.",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
        }
        
        if (!successfulLogin) {
            JOptionPane.showMessageDialog(null, "Wrong username or password",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            successfulLogin = false;
        } else {
            successfulLogin = true;
            conversations = new ArrayList<>();
            users = new ArrayList<>();
            readUsers();
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    user = u;
                }
            }
            readConversationsFromFile();
        }
    }

    /**
     * Method to display sign up screen
     */
    private void displaySignUp() {
        signUpFrame = new JFrame("Sign Up");
        Container signUpContent = signUpFrame.getContentPane();  //container for sign up screen
        
        signUpContent.setLayout(null);
        JLabel fullNameLabel = new JLabel("Full Name ");  //label for full name text field
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
        
        JLabel userName = new JLabel("Username");  //label for username text field
        userName.setFont(new Font("Arial", Font.PLAIN, 20));
        userName.setSize(100, 20);
        userName.setLocation(300, 200);
        signUpContent.add(userName, BorderLayout.CENTER);
        
        createUsernameField = new JTextField();
        createUsernameField.addActionListener(actionListener);
        createUsernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        createUsernameField.setSize(190, 20);
        createUsernameField.setLocation(400, 200);
        signUpContent.add(createUsernameField, BorderLayout.CENTER);
        
        JLabel passWord = new JLabel("Password ");  //label for password text field
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
        
        signupToLogin = new JButton("Login");
        signupToLogin.setFont(new Font("Arial", Font.PLAIN, 15));
        signupToLogin.setSize(100, 20);
        signupToLogin.setLocation(360, 350);
        signupToLogin.addActionListener(actionListener);
        signUpContent.add(signupToLogin);
        
        signUpFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        signUpFrame.pack();
        
        signUpFrame.setSize(900, 600);
        signUpFrame.setLocationRelativeTo(null);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setVisible(true);
    }

    /**
     * Method to sign up as a new user
     */
    private void signUp() {
        readUsers();
        String username = createUsernameField.getText();  //username entered in text field
        String password = createPasswordField.getText();  //password entered in text field
        String name = createNameField.getText();  //full name entered in text field
        if (username.contains("*") || password.contains("*") || name.contains("*")) {
            JOptionPane.showMessageDialog(null, "Invalid information. No * allowed.",
                    "Signup Error", JOptionPane.ERROR_MESSAGE);
            signUpFrame.setVisible(false);
            displaySignUp();
            return;
        }

        //Displays error and returns to sign up screen if not all fields were entered
        if (username == null || ("".equals(username))
                || ("".equals(password)) || password == null
                || ("".equals(name)) || name == null) {
            JOptionPane.showMessageDialog(null, "Please enter all details.",
                    "Signup Error", JOptionPane.ERROR_MESSAGE);
            signUpFrame.setVisible(false);
            displaySignUp();
            return;
        } else {
            StringBuilder sb = new StringBuilder();  //message to sent to server to sign up
            sb.append("SignUp*");
            sb.append(name + "*");
            sb.append(username + "*");
            sb.append(password);
            
            outputToServer.println(sb.toString());
            readUsers();

            //Displays error if unable to sign up
            try {
                if (obj.readBoolean()) {
                    signUpFrame.setVisible(false);
                    loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username",
                            "Signup Error", JOptionPane.ERROR_MESSAGE);
                    signUpFrame.setVisible(false);
                    displaySignUp();
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Sign up failed. Try again.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Displays main screen
     */
    private void mainScreen() {
        mainFrame = new JFrame("Messages");
        Container content = mainFrame.getContentPane();  //container for main screen
        
        content.setLayout(new BorderLayout());

        conversationPanel = new JPanel(new GridBagLayout());

        mainPanel();

        JScrollPane scrollPane = new JScrollPane(conversationPanel);  //scroll pane with all user's conversations

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        content.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());  //panel with settings and add conversation buttons
        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(actionListener);
        bottomPanel.add(settingsButton, BorderLayout.WEST);
        addButton = new JButton("New Conversation");
        addButton.addActionListener(actionListener);
        bottomPanel.add(addButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomPanel.setBackground(Color.white);
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        mainFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        mainFrame.pack();
        
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    /**
     * Method to display conversations in panel of main screen
     */
    private void mainPanel() {
        //Clears panel
        conversationPanel.removeAll();
        conversationPanel.revalidate();
        conversationPanel.repaint();

        readConversationsFromFile();

        conversationPanel.setLayout(new GridBagLayout());
        conversationPanel.setBackground(Color.decode("#B9E0DE")); // set background color

        GridBagConstraints constraints = new GridBagConstraints();  //constraints for layout
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        deleteConvButton = new JButton("Delete");

        for (int i = 0; i < conversations.size(); i++) {
            JButton button = new JButton(conversations.get(i).getName());  //button for conversation
            button.setActionCommand(String.valueOf(i));
            button.addActionListener(actionListener);
            constraints.gridwidth = 1;
            constraints.gridx = 0;
            constraints.ipadx = 300;
            conversationPanel.add(button, constraints);
            deleteConvButton = new JButton("Delete");
            deleteConvButton.setActionCommand(String.valueOf(i));
            deleteConvButton.addActionListener(deleteConversation);
            constraints.gridwidth = 1;
            constraints.gridx = 2;
            constraints.ipadx = 0;
            conversationPanel.add(deleteConvButton, constraints);
        }

    }

    /**
     * Displays settings screen
     */
    private void settings() {
        //Tells server we are in settings
        outputToServer.println("Settings*");
        try {
            user = (User) obj.readObject();
        } catch (IOException i) {
            JOptionPane.showMessageDialog(null, "Connection failed while searching for a user",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException c) {
            JOptionPane.showMessageDialog(null, "Connection failed while searching for a user",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        settingsFrame = new JFrame("Settings");
        Container content = settingsFrame.getContentPane();  //container for settings page
        content.setLayout(new BorderLayout());

        // Contains all the information fields to view and edit.
        // Includes the user's "Name", "Username", and "Password"
        JPanel infoPanel = new JPanel();  //panel with all settings information
        infoPanel.setBackground(Color.decode("#B9E0DE"));
        nameLabel = new JLabel("Name: ");
        nameField = new JTextField(user.getName(), 10);
        usernameLabel = new JLabel("Username: ");
        JLabel usernameField = new JLabel(user.getUsername(), 10);  //label with user's username
        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField("", 10);
        
        infoPanel.add(usernameLabel);
        infoPanel.add(usernameField);
        infoPanel.add(nameLabel);
        infoPanel.add(nameField);
        infoPanel.add(passwordLabel);
        infoPanel.add(passwordField);
        content.add(infoPanel, BorderLayout.CENTER);

        // Shows the "Home", "Save", and "Delete" buttons at the bottom of the GUI
        JPanel buttonsPanel = new JPanel();  //panel for all of the settings buttons
        homeButton = new JButton("Home");
        homeButton.addActionListener(settingsAL);
        saveButton = new JButton("Save Settings");
        saveButton.addActionListener(settingsAL);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(settingsAL);
        deleteAccountButton = new JButton("Delete");
        deleteAccountButton.addActionListener(settingsAL);
        buttonsPanel.add(homeButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(logoutButton);
        buttonsPanel.add(deleteAccountButton);
        content.add(buttonsPanel, BorderLayout.SOUTH);
        
        settingsFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        settingsFrame.pack();
        
        settingsFrame.setSize(600, 400);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsFrame.setVisible(true);
    }

    /**
     * Method to delete current user's account
     */
    private void deleteAccount() {
        //Send the command 'DeleteAccount*' to the server
        outputToServer.println("DeleteAccount*");
        boolean success = false;  //boolean for whether deletion was successful

        //Displays error if unsuccessful deletion
        try {
            success = obj.readBoolean();
            if (!success) {
                JOptionPane.showMessageDialog(null, "Error in deleting Account", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Successfully Deleted Account",
                        "Deletion Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error in deleting Account", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        readUsers();
        user = null;
        System.exit(0);  //closes program
    }

    /**
     * Method to edit account called after pressing save settings
     */
    private void saveSettings() {
        String fullName = nameField.getText();  //new full name entered in text field
        String newPassword = passwordField.getText();  //new password entered in text field

        //Displays error if not all fields are entered
        if (fullName == null || fullName.equals("") || newPassword == null || newPassword.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter all the fields", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();  //Message to be sent to server to edit account
        sb.append("SaveSettings*");
        sb.append(fullName + "*");
        sb.append(newPassword);
        
        outputToServer.println(sb.toString());
        
        JOptionPane.showMessageDialog(null, "New information saved to account.", "SavedSettings",
                JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Error reading conversations from file. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Error reading users from file. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the conversation that was clicked (frame only)
     */
    private void displayConversation() {
        messageFrame = new JFrame(conversationDisplayed.getName());
        messageContent = messageFrame.getContentPane();
        messageContent.setLayout(new BorderLayout());
        messagePanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(messagePanel);  //scroll pane for messages

        messageContent.add(scrollPane, BorderLayout.CENTER);
        JPanel textFieldPanel = new JPanel();  //panel for text field and buttons for messages
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
        messageContent.add(textFieldPanel, BorderLayout.SOUTH);
        
        messageFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        messageFrame.pack();
        
        messageFrame.setSize(800, 400);
        messageFrame.setLocationRelativeTo(null);
        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        displayMessages();
        messageFrame.setVisible(true);
    }

    /**
     * Display messages for the conversation displayed
     */
    private void displayMessages() {
        //Clears message panel contents
        messagePanel.removeAll();
        messagePanel.revalidate();
        messagePanel.repaint();

        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messages = conversationDisplayed.getMessages();
        
        messagePanel.setBackground(Color.decode("#B9E0DE")); // set background color
        //messageFrame.pack();
        
        String user;  //user sending the message
        String message;  //specific message
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
            JOptionPane.showMessageDialog(null, "Error displaying messages. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editMessages() {
        editMessageFrame = new JFrame(conversationDisplayed.getName());
        
        Container content = editMessageFrame.getContentPane();
        content.setLayout(new BorderLayout());
        
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(Color.decode("#B9E0DE"));
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
            JOptionPane.showMessageDialog(null, "Error editing messages. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error displaying messages. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        for (String line : messagesArr) {
            String[] output = line.split("\\*");
            constraints.gridwidth = 2;
            constraints.gridx = 0;
            constraints.ipadx = 300;
            String formattedMessage = output[0] + ": " + output[1];
            messagePanel.add(new JLabel(formattedMessage), constraints);
            messagePanel.setBackground(Color.decode("#B9E0DE"));
            boolean editable = Boolean.parseBoolean(output[2]);
            String index = output[3];
            if (editable) {
                deleteMessageButton = new JButton("Delete");
                deleteMessageButton.setActionCommand(index);
                deleteMessageButton.addActionListener(deleteMessageAL);
                editSpecificMsgButton = new JButton("Edit");
                editSpecificMsgButton.setActionCommand(index);
                editSpecificMsgButton.addActionListener(editSpecificMessageAL);
                JPanel editButtons = new JPanel();
                editButtons.setBackground(Color.decode("#B9E0DE"));
                editButtons.add(deleteMessageButton);
                editButtons.add(editSpecificMsgButton);
                constraints.gridwidth = 1;
                constraints.gridx = 2;
                constraints.ipadx = 0;
                messagePanel.add(editButtons, constraints);
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
        
        content.add(scrollPane, BorderLayout.CENTER);
        JPanel textFieldPanel = new JPanel();
        
        editMessBackButton = new JButton("Back");
        editMessBackButton.addActionListener(actionListener);
        
        textFieldPanel.add(editMessBackButton);
        content.add(textFieldPanel, BorderLayout.SOUTH);
        
        editMessageFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        editMessageFrame.pack();
        
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
    
    private void editSpecificMessage(int index) {
        editMessageFrame.setVisible(false);
        String editedMessage = JOptionPane.showInputDialog("Enter the new edited message:");
        if (editedMessage == null || editedMessage.equals("")) {
            JOptionPane.showMessageDialog(null, "Invalid message", "Error Editing Message",
                    JOptionPane.ERROR_MESSAGE);
            editMessages();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("EditSpecificMessage*");
        sb.append(editedMessage + "*");
        sb.append(String.valueOf(index) + "*");
        outputToServer.println(sb);
        try {
            int size = obj.readInt();
            messagesArr.clear();
            for (int i = 0; i < size; i++) {
                messagesArr.add((String) obj.readObject());
            }
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Failed to edit message. Try again.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Failed to edit conversation. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        readConversationsFromFile();
    }

    /**
     * Delete conversation for the current user
     */
    private void deleteConversation() {
        String convNameTodelete = conversationDisplayed.getName();
        
        StringBuilder sb = new StringBuilder();
        sb.append("DeleteConversation*");
        sb.append(convNameTodelete);
        
        outputToServer.println(sb.toString());
        
        boolean success = false;
        try {
            success = obj.readBoolean();
            JOptionPane.showMessageDialog(null, "Successfully Deleted Conversation",
                    "Error", JOptionPane.ERROR_MESSAGE);
            if (!success) {
                JOptionPane.showMessageDialog(null, "Error in deleting Conversation",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error in deleting Conversation",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(null, "Failed to add message. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        messagePanel.add(new JLabel(user.getUsername() + ": " + message));
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
        
        addConversationFrame.getContentPane().setBackground(Color.decode("#B9E0DE")); // set background color
        addConversationFrame.pack();
        
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
            JOptionPane.showMessageDialog(null, "Error while searching for user. Try Again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException c) {
            JOptionPane.showMessageDialog(null, "Error in deleting Conversation",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
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
        outputToServer.println("AddUserToConversation*" + otherUser.getUsername());
        try {
            usersToAdd = new ArrayList<>();
            int size = obj.readInt();
            for (int i = 0; i < size; i++) {
                usersToAdd.add((User) obj.readObject());
            }
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Failed to add conversation. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
        String nameOfConversation = conversationNameField.getText();  //name of new conversation entered in text field
        if (nameOfConversation.contains("*")) {
            JOptionPane.showMessageDialog(null, "Invalid Conversation Name (No * allowed). Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String conversationFileName = nameOfConversation + ".txt";  //potential name of conversation messages file
        if (new File(conversationFileName).exists()) {
            JOptionPane.showMessageDialog(null, "Invalid Conversation Name. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for (Conversation c : conversations) {
            if (nameOfConversation.equals(c.getName())) {
                JOptionPane.showMessageDialog(null, "Invalid Conversation Name. Try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        outputToServer.println("CreateConversation*" + nameOfConversation);
        boolean successfulAddition = false;
        try {
            successfulAddition = obj.readBoolean();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to add conversation. Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!successfulAddition) {
            JOptionPane.showMessageDialog(null, "Invalid conversation name", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        readConversationsFromFile();
        addConversationFields.setVisible(false);
        mainFrame.setVisible(false);
        mainScreen();
        mainFrame.setVisible(true);
        return true;
    }
}
