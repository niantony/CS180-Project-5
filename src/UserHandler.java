import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class UserHandler implements Runnable {
    private Socket socket;
    public ArrayList<User> userArrayList = new ArrayList<User>();
    public File usersFile = new File("UsersFile.txt");
    private ArrayList<User> usersToAdd;
    private ArrayList<Conversation> userConversations;
    private User currentUser;

    public UserHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                String userInput = input.readLine();
                System.out.println(userInput);

                if (userInput.contains("LogIn*")) {

                    if (LogIn(userInput)) {
                        readConversations();
//                        output.println(true);
                        oos.writeBoolean(true);
                        oos.flush();
//                        oos.reset();
//                        System.out.println(currentUser.getName());
//                        oos.writeObject(currentUser);
//                        oos.flush();
//                        System.out.println(currentUser.getName());
                    } else {
//                        output.println(false);
                        oos.writeBoolean(false);
                        oos.flush();
                        System.out.println("User not found");
                    }
                    userInput = "";
                }

                if (userInput.contains("SignUp*")) {

                    if (SignUp(userInput)) {
//                        output.println(true);
                        oos.writeBoolean(true);
                    } else {
//                        output.println(false);
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
                    for (User u : usersToAdd) {
                        System.out.println("Test: " + u.getName());
                    }
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
//                        output.println(true);
                        oos.writeBoolean(true);
                    } else {
                        System.out.println(false);
//                        output.println(false);
                        oos.writeBoolean(false);
                    }
                    oos.flush();
//                    oos.writeObject(userConversations);
                }

                if (userInput.contains("DeleteConversation*")) {

                }

                if (userInput.contains("Message*")) {

                }

                if (userInput.contains("EditMessage*")) {

                }

                if (userInput.contains("DeleteMessage*")) {

                }

                if (userInput.contains("EditProfile*")) {

                }

                if (userInput.contains("DeleteProfile*")) {

                }
                oos.flush();
//                output.flush();
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
//        if (file.exists()) {
//            return false;
//        }
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
}