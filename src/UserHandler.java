import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CS 180 Project 5 -- UserHandler.java
 *
 * Handles requests from the user, such as logging in, signing up, adding conversation, editing and deleting messages,
 * and more on the server side.
 *
 * We referenced the documentation/tutorials for object output streams, object input streams, and sockets.
 *
 * @author Antony Ni, G17
 * @author Ishika Vachali, Online
 * @author Michael Con, Online
 * @author Sruthi Koukuntla, LC3
 *
 * @version December 6, 2020
 */
public class UserHandler implements Runnable {

    private Socket socket;  //socket that connects user to server
    public ArrayList<User> userArrayList = new ArrayList<User>();  //arraylist of all of the users in the server

    public File usersFile = new File("UsersFile.txt"); //usersFile contains all of the users in the system
    private ArrayList<User> usersToAdd; //arraylist of users to add to a new conversation
    private ArrayList<Conversation> userConversations; //all the conversations of the user
    private User currentUser; // the current user of the thread
    private ArrayList<String> messagesArr = new ArrayList<>();  //arraylist of conversation messages from to edit/delete
    private File messages; //TextFile containing the messages for the conversation

    /**
     * Constructor for UserHandler
     * @param socket that connects current user to server
     */
    public UserHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Receives input from the server and calls methods accordingly
     */
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));  //reads user
                                                                                                        // input
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  //sends output to user

            while (true) { //Endless loop, keeps the thread for the user open
                String userInput = input.readLine(); //Input by the User to the server

                if (userInput != null) {
                    System.out.println(userInput);

                    /**
                        The user sends commands to the server in the form of
                        COMMAND*Input
                        The COMMAND signifies the action that the user wants to do
                        The server responds accordingly
                    */
                    if (userInput.contains("LogIn*")) {

                        if (logIn(userInput)) {
                            readConversations();
                            oos.writeBoolean(true);
                            oos.flush();
                        } else {
                            oos.writeBoolean(false);
                            oos.flush();
                            System.out.println("User not found");
                        }
                        userInput = "";
                    }

                    if (userInput.contains("SignUp*")) {

                        if (signUp(userInput)) {
                            oos.writeBoolean(true);
                        } else {
                            oos.writeBoolean(false);
                        }
                        oos.flush();
                        userInput = "";
                    }

                    if (userInput.contains("AddConversation*")) {
                        usersToAdd = new ArrayList<>();
                    }

                    if (userInput.contains("AddUserToConversation*")) {
                        addUserToConversation(userInput);
                        oos.writeInt(usersToAdd.size());
                        oos.flush();
                        for (User u : usersToAdd) {
                            oos.writeObject(u);
                            oos.flush();
                        }
                    }

                    if (userInput.contains("SearchUser*")) {
                        oos.writeObject(searchUser(userInput));
                    }

                    if (userInput.contains("CreateConversation*")) {
                        if (createConversation(userInput)) {
                            oos.writeBoolean(true);
                        } else {
                            oos.writeBoolean(false);
                        }
                        oos.flush();
                    }

                    if (userInput.contains("DeleteConversation*")) {
                        deleteConversation(userInput);
                        oos.writeBoolean(true);
                        oos.flush();
                    }

                    if (userInput.contains("EditMessage*")) {
                        editMessage(userInput);
                        oos.writeInt(messagesArr.size());
                        for (String line : messagesArr) {
                            oos.writeObject(line);
                        }
                    }

                    if (userInput.contains("EditSpecificMessage*")) {
                        editSpecificMessage(userInput);
                        oos.writeInt(messagesArr.size());
                        for (String line : messagesArr) {
                            oos.writeObject(line);
                        }
                    }

                    if (userInput.contains("DeleteMessage*")) {
                        deleteMessage(userInput);
                    }

                    if (userInput.contains("Settings*")) {
                        oos.writeObject(currentUser);
                        oos.flush();
                    }

                    if (userInput.contains("SaveSettings*")) {
                        saveSettings(userInput);
                        oos.writeBoolean(true);
                    }

                    if (userInput.contains("DeleteAccount*")) {
                        boolean successfulDelete = deleteAccount();
                        oos.writeBoolean(successfulDelete);
                    }
                    oos.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Oops: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads all of the users from the usersFile into the usersArrayList
     */
    public void readUsers() {
        userArrayList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(usersFile))) {
            User u = (User) in.readObject();
            while (u != null) {
                userArrayList.add(u);
                u = (User) in.readObject();
            }
        } catch (EOFException | FileNotFoundException e) {
            //end of file
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all the conversations for the current user into the userConversations arraylist
     */
    private void readConversations() {
        userConversations = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(currentUser.getConversations()))) {
            Conversation c = (Conversation) in.readObject();
            while (c != null) {
                userConversations.add(c);
                c = (Conversation) in.readObject();
            }
        } catch (EOFException | NullPointerException e) {
            //end of file
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see whether login is valid and sets current user to the user with the same username and password
     * @param logIn input from client
     * @return boolean, true if successful login, false if unsuccessful login
     */
    public boolean logIn(String logIn) {
        //LogIn*Username*Password
        String[] checkUser = logIn.split("\\*");  //array with user input
        readUsers();
        for (User u : userArrayList) {
            if (u.getUsername().equals(checkUser[1]) && u.getPassword().equals(checkUser[2])) {
                currentUser = u;  //sets current user equal to user with correct login info
                readConversations();
                readUsers();
                System.out.println("Correct login");
                return true;
            }
        }
        return false;
    }

    /**
     * Signs up a new user using information from client
     * @param signUp input from client
     * @return boolean, true if successful sign up, false if unsuccessful sign up
     */
    public boolean signUp(String signUp) {
        //SignUp*Name*Username*Password
        try {

            String[] checkUser = signUp.split("\\*");  //user input
            readUsers();

            //Check if username is unique
            for (User u : userArrayList) {
                if (u.getUsername().equals(checkUser[2])) {
                    return false;
                }
            }

            //Create a textfile for the User
            String fileName = checkUser[2] + ".txt";  //name of textfile of conversations for the new user
            File file = new File(fileName);  //textfile of conversations for the new user

            try {
                file.createNewFile();
            } catch (IOException a) {
                System.out.println("Failed creating new file for user");
                return false;
            }

            User newUser = new User(checkUser[1], checkUser[2], checkUser[3], file);  //new user with entered info

            if (userArrayList.isEmpty()) {
                try {
                    usersFile.createNewFile();
                } catch (IOException e) {
                    System.out.println("Failed to create new file");
                    return false;
                }

                //Write new user to users file
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {
                    out.writeObject(newUser);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            } else {
                //Write all users + new user to users file
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {

                    for (User u : userArrayList) {
                        out.writeObject(u);
                        out.flush();
                    }
                    out.writeObject(newUser);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            return false;
        }
        System.out.println("Error in Sign Up");
        return false;
    }

    /**
     * Searches for matching users based on user input
     * @param userSearch input from client
     * @return arraylist of users that matched with input
     */
    public ArrayList<User> searchUser(String userSearch) {
        //SearchUser*Username
        String[] checkUser = userSearch.split("\\*");  //user input

        ArrayList<User> result = new ArrayList<>();  //arraylist of matching users
        readUsers();

        //Checks whether username/name of users in usersArrayList match the search input
        for (User u : userArrayList) {
            if (u.getUsername().toLowerCase().contains(checkUser[1].toLowerCase())) {
                result.add(u);
            } else if (u.getName().toLowerCase().contains(checkUser[1].toLowerCase())) {
                result.add(u);
            }
        }
        return result;
    }

    /**
     * Adds a user to a conversation
     * @param userInput input from client
     */
    public void addUserToConversation(String userInput) {
        readUsers();
        String[] input = userInput.split("\\*");  //input from client
        String username = input[1];  //username of user being added

        //if usersToAdd array doesn't contain current user, add current user
        if (!usersToAdd.contains(currentUser)) {
            usersToAdd.add(currentUser);
        }
        //add user with same username to arraylist
        for (User u : userArrayList) {
            if (u.getUsername().equals(username)) {
                usersToAdd.add(u);
            }
        }
    }

    /**
     * Creates a new conversation
     * @param userInput input from client
     * @return boolean, true is successfully created conversation, false if failed
     */
    public boolean createConversation(String userInput) {
        readConversations();
        String[] input = userInput.split("\\*");  //user input
        String nameOfConversation = input[1];  //name of the conversation to create
        String fileName = nameOfConversation + ".txt";  //filename for messages file for new conversation
        File file = new File(fileName);  //file of messages for new conversation
        try {
            file.createNewFile();
        } catch (IOException e) {
            return false;
        }
        Conversation newConversation = new Conversation(nameOfConversation, usersToAdd, file);  //new conversation
        for (User u : usersToAdd) {
            //if current user is being added, use userConversations
            if (u.getUsername().equals(currentUser.getUsername())) {
                //if empty, only write new conversation
                if (userConversations.isEmpty()) {
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(u.getConversations()))) {
                        out.writeObject(newConversation);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //if not empty write all conversations and new conversation
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(u.getConversations()))) {
                        for (Conversation c : userConversations) {
                            out.writeObject(c);
                            out.flush();
                        }
                        out.writeObject(newConversation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //if not current user, read their conversations file into an arraylist
                ArrayList<Conversation> otherUserConversations = readOtherUserConversations(u);  //other user's
                                                                                                 //conversation file
                //if empty, only write new conversation
                if (otherUserConversations.isEmpty()) {
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(u.getConversations()))) {
                        out.writeObject(newConversation);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //if not empty write all conversations and new conversation
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(u.getConversations()))) {
                        for (Conversation c : otherUserConversations) {
                            out.writeObject(c);
                            out.flush();
                        }
                        out.writeObject(newConversation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    /**
     * Runs through messages and returns whether the message is editable
     * @param userInput input from client
     */
    private void editMessage(String userInput) {
        readConversations();
        String[] input = userInput.split("\\*");  //user input
        String conversationName = input[1];  //name of conversation being edited
        Conversation conversationDisplayed = userConversations.get(0);
        //find the conversation displayed by finding the matching conversation name
        for (Conversation c : userConversations) {
            if (c.getName().equals(conversationName)) {
                conversationDisplayed = c;
            }
        }
        messages = conversationDisplayed.getMessages();  //message file within conversation displayed
        String userS;  //user's username for a specific message in loop
        String message;  //specific message being looked at in loop
        int i = 0;
        messagesArr.clear();
        //returns Message*Editable*i
        try (BufferedReader br = new BufferedReader(new FileReader(messages))) {
            String line = br.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    String result = "";  //resulting line to add to messagesArr
                    boolean editable;  //whether the line is editable (by current user)
                    String[] userAndMessage = line.split("\\*");
                    userS = userAndMessage[0];
                    message = userAndMessage[1];
                    result += userS + "*" + message + "*";
                    if (userS.equals(currentUser.getUsername())) {
                        editable = true;
                    } else {
                        editable = false;
                    }
                    result += String.valueOf(editable) + "*" + String.valueOf(i);
                    messagesArr.add(result);
                    i++;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error in finding editable messages");
            return;
        }
    }

    /**
     * Deletes a specific message
     * @param userInput input from client
     */
    private void deleteMessage(String userInput) {
        String[] input = userInput.split("\\*");  //user's input
        int indexToDelete = Integer.parseInt(input[1]);  //index in messagesArr of message to delete
        messagesArr.remove(indexToDelete);  //removes message to delete from array

        //Rewrites messages file using messagesArr
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(messages))) {
            String formattedMessage;
            for (String msg : messagesArr) {
                String[] message = msg.split("\\*");
                formattedMessage = "\n" + message[0] + "*" + message[1];
                pw.print(formattedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits a specific message
     * @param userInput input from client
     */
    private void editSpecificMessage(String userInput) {
        String[] input = userInput.split("\\*");  //user's input
        String newMessage = input[1];  //new message to replace old message with
        int index = Integer.parseInt(input[2]);  //index of message to replace in messagesArr

        //new formatted message to replace old message in messagesArr
        String editedMessage = currentUser.getUsername() + "*" + newMessage + "*true*" + String.valueOf(index);
        messagesArr.set(index, editedMessage);

        //Rewrites messages file using messagesArr
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(messages))) {
            String formattedMessage;
            for (String msg : messagesArr) {
                String[] message = msg.split("\\*");
                formattedMessage = "\n" + message[0] + "*" + message[1];
                pw.print(formattedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes user's account
     * @return boolean, true if deleted account successfully, false if failed
     */
    private boolean deleteAccount() {
        readUsers();
        boolean success = false;  //boolean for whether account was deleted successfully
        File file = currentUser.getConversations();  //file of user's conversations
        try {
            //clears file contents
            if (file.exists()) {
                new PrintWriter(file).close();
                file.delete();
            }
        } catch (IOException e) {
            return false;
        }
        //Removes user from userArrayList
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getUsername().equals(currentUser.getUsername())) {
                userArrayList.remove(i);
                success = true;
            }
        }
        currentUser = null;
        writeUsersToFile();  //write updated userArrayList to usersFile
        readUsers();
        return success;
    }

    /**
     * Edits account with new information
     * @param userInfo input from client
     */
    private void saveSettings(String userInfo) {
        String[] checkUser = userInfo.split("\\*");  //user input
        String nameField = checkUser[1];  //new name
        String passwordField = checkUser[2];  //new password

        //Changes user with matching username with new name and password
        for (User u : userArrayList) {
            if (u.getUsername().equals(currentUser.getUsername())) {
                u.setName(nameField);
                u.setPassword(passwordField);
            }
        }
        currentUser.setName(nameField);
        currentUser.setPassword(passwordField);
        writeUsersToFile();  //updates userFile with updated userArrayList
    }

    /**
     * Reads another user's conversations file into an arraylist of conversations
     * @param otherUser other user to read conversations from
     * @return arraylist of the other user's conversations
     */
    private ArrayList<Conversation> readOtherUserConversations(User otherUser) {
        ArrayList<Conversation> otherUserConversations = new ArrayList<>();  //arraylist of other user's conversations
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(otherUser.getConversations()))) {
            Conversation c = (Conversation) in.readObject();  //conversation being read
            //adds to arraylist if not null value
            while (c != null) {
                otherUserConversations.add(c);
                c = (Conversation) in.readObject();
            }
        } catch (EOFException | NullPointerException e) {
            //end of file
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return otherUserConversations;
    }

    /**
     * Writes userArrayList to usersFile
     */
    private void writeUsersToFile() {
        //Delete file if empty
        if (userArrayList.isEmpty() && currentUser == null) {
            usersFile.delete();
        } else {
            //Rewrite usersFile with userArrayList
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {
                for (User u : userArrayList) {
                    out.writeObject(u);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes conversation for current user only
     * @param userInfo input from client
     */
    private void deleteConversation(String userInfo) {
        String[] checkUser = userInfo.split("\\*");  //user's input
        String convNameToDelete = checkUser[1];  //name of conversation to delete

        readConversations();
        //Remove conversation from userConversations
        for (int i = 0; i < userConversations.size(); i++) {
            if (userConversations.get(i).getName().equals(convNameToDelete)) {
                userConversations.remove(i);
            }
        }

        //Rewrite user's conversation file with updated userConversations
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(currentUser.getConversations()))) {
            for (Conversation c : userConversations) {
                out.writeObject(c);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
