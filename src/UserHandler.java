import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class UserHandler implements Runnable {
    private Socket socket;
    public ArrayList<User> userArrayList = new ArrayList<User>(); //Arraylist containing all of the users in File
    public File usersFile = new File("UsersFile.txt"); //Text file containing Users
    private ArrayList<User> usersToAdd; //Users to add in new conversations
    private ArrayList<Conversation> userConversations;
    private User currentUser; // the Current User of the thread
    private ArrayList<String> messagesArr = new ArrayList<>();
    private File messages;

    public UserHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//          PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                String userInput = input.readLine();
                System.out.println(userInput);

                if (!userInput.isEmpty()) {

                    if (userInput.contains("LogIn*")) {

                        if (LogIn(userInput)) {
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

                        if (SignUp(userInput)) {
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
                        oos.writeObject(SearchUser(userInput));
                    }

                    if (userInput.contains("CreateConversation*")) {
                        if (createConversation(userInput)) {
                            System.out.println(true);
                            oos.writeBoolean(true);
                        } else {
                            System.out.println(false);
                            oos.writeBoolean(false);
                        }
                        oos.flush();
                    }

                    if (userInput.contains("deleteConversation*")) {
                        deleteConversation(userInput);
                        oos.writeBoolean(true);
                    }

                    if (userInput.contains("Message*")) {

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

                }

                oos.flush();
            }
        } catch(IOException e) {
            System.out.println("Oops: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Reads all of the users from usersFile into the userArrayList
    public void ReadUsers() {
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

    public boolean LogIn(String logIn) {
        //LogIn*MichaelCon*password123
        String[] checkUser = logIn.split("\\*");
        ReadUsers();
        for (User u : userArrayList) {
            if (u.getUsername().equals(checkUser[1]) && u.getPassword().equals(checkUser[2])) {
                currentUser = u;
                readConversations();
                ReadUsers();
                System.out.println("Correct login");
                return true;
            }
        }
        return false;
    }

    public boolean SignUp(String signUp) {
        //SignUp*MichaelCon*Username*password123
        try {

            String[] checkUser = signUp.split("\\*");
            ReadUsers();

            //Check if username is unique
            for (User u : userArrayList) {
                if (u.getUsername().equals(checkUser[2])) {
                    return false;
                }
            }

            //create a textfile for the User
            String fileName = checkUser[2] + ".txt";
            File file = new File(fileName);

            try {
                file.createNewFile();
            } catch (IOException a) {
                System.out.println("Failed creating new file for user");
            }

            User newUser = new User(checkUser[1], checkUser[2], checkUser[3], file);

            if (userArrayList.isEmpty()) {
                try {
                    usersFile.createNewFile();
                } catch (IOException e) {
                    //unable to create file
                }

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile))) {
                    out.writeObject(newUser);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            } else {
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

        } finally {
//            System.out.println("Error in Sign Up");
        }
        System.out.println("Error in Sign Up");
        return false;
    }

    //Method that searches for users based on the user input
    public ArrayList<User> SearchUser(String userSearch) {
        //SearchUser*Name
        String[] checkUser = userSearch.split("\\*");

        ArrayList<User> result = new ArrayList<>();
        ReadUsers();

        for (User u : userArrayList) {
            if (u.getUsername().toLowerCase().contains(checkUser[1].toLowerCase())) {
                result.add(u);
            }
        }
        return result;
    }

    public void addUserToConversation(String userInput) {
        ReadUsers();
        String[] input = userInput.split("\\*");
        String username = input[1];
        if (!usersToAdd.contains(currentUser)) {
            usersToAdd.add(currentUser);
        }
        for (User u : userArrayList) {
            if (u.getUsername().equals(username)) {
                usersToAdd.add(u);
            }
        }
    }

    public boolean createConversation(String userInput) {
        readConversations();
        String[] input = userInput.split("\\*");
        String nameOfConversation = input[1];
        String fileName = nameOfConversation + ".txt";
        File file = new File(fileName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            //failed
        }
        Conversation newConversation = new Conversation(nameOfConversation, usersToAdd, file);
        for (User u : usersToAdd) {
            if (u.getUsername().equals(currentUser.getUsername())) {
                if (userConversations.isEmpty()) {
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(u.getConversations()))) {
                        out.writeObject(newConversation);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
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
                ArrayList<Conversation> otherUserConversations = readOtherUserConversations(u);
                if (otherUserConversations.isEmpty()) {
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(u.getConversations()))) {
                        out.writeObject(newConversation);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
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

    private void editMessage(String userInput) {
        readConversations();
        String[] input = userInput.split("\\*");
        String conversationName = input[1];
        Conversation conversationDisplayed = userConversations.get(0);
        for (Conversation c : userConversations) {
            if (c.getName().equals(conversationName)) {
                conversationDisplayed = c;
            }
        }
        messages = conversationDisplayed.getMessages();
        String userS;
        String message;
        int i = 0;
        messagesArr.clear();
        //returns Message*Deletable*i
        try (BufferedReader br = new BufferedReader(new FileReader(messages))) {
            String line = br.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    String result = "";
                    boolean editable;
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
            //do something?
        }
    }

    private void deleteMessage(String userInput) {
        String[] input = userInput.split("\\*");
        int indexToDelete = Integer.parseInt(input[1]);
        messagesArr.remove(indexToDelete);
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

    private void editSpecificMessage(String userInput) {
        String[] input = userInput.split("\\*");
        String newMessage = input[1];
        int index = Integer.parseInt(input[2]);
        String editedMessage = currentUser.getUsername() + "*" + newMessage + "*true*" + String.valueOf(index);
        messagesArr.set(index, editedMessage);
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

    private boolean deleteAccount() {
        ReadUsers();
        boolean success = false;
        File file = currentUser.getConversations();
        try {
            if (file.exists()) {
                new PrintWriter(file).close();
                file.delete();
            }
        } catch (IOException e) {
            return false;
        }
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getUsername().equals(currentUser.getUsername())) {
                userArrayList.remove(i);
                success = true;
            }
        }
        currentUser = null;
        writeUsersToFile();
        ReadUsers();
        return success;
    }

    private void saveSettings(String userInfo) {
        String[] checkUser = userInfo.split("\\*");
        String nameField = checkUser[1];
        System.out.println(nameField);
        String passwordField = checkUser[2];
        System.out.println(passwordField);
        currentUser.setName(nameField);
        currentUser.setPassword(passwordField);

        writeUsersToFile();
        for (User u : userArrayList) {
            System.out.println(u.getName());
            System.out.println(u.getPassword());
        }
    }

    private ArrayList<Conversation> readOtherUserConversations(User otherUser) {
        ArrayList<Conversation> otherUserConversations = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(otherUser.getConversations()))) {
            Conversation c = (Conversation) in.readObject();
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

    private void writeUsersToFile() {
        if (userArrayList.isEmpty() && currentUser == null) {
            usersFile.delete();
        } else {
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

    private void deleteConversation(String userInfo) {
        //File userConversationF = user.getConversations();
        String[] checkUser = userInfo.split("\\*");
        String convNameTodelete = checkUser[1];;
        for (int i = 0; i < userConversations.size(); i++) {
            if (userConversations.get(i).getName().equals(convNameTodelete)) {
                userConversations.remove(i);
            }        }

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
