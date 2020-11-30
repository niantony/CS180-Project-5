import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class UserHandler implements Runnable {
    private Socket socket;
    public ArrayList<User> userArrayList = new ArrayList<User>();
    public File usersFile = new File("UsersFile.txt");

    public UserHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                String userInput = input.readLine();
                System.out.println(userInput);

                if (userInput.contains("LogIn*")) {

                    if (LogIn(userInput)) {
                        output.println(true);
                    } else {
                        output.println(false);
                    }
                    userInput = "";
                }

                if (userInput.contains("SignUp*")) {

                    if (SignUp(userInput)) {
                        output.println(true);
                    } else {
                        output.println(false);
                    }
                    userInput = "";
                }

                if (userInput.contains("SearchUser*")) {
                    oos.writeObject(SearchUser(userInput));
                }

                if (userInput.contains("CreateConversation*")) {

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

            }
        } catch(IOException e) {
            System.out.println("Oops: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch(IOException e) {
                // Oh, well!
            }
        }
    }

    public void ReadUsers() {
        userArrayList.removeAll(userArrayList);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(usersFile))) {
            User u = (User) in.readObject();
            while (u != null) {
                this.userArrayList.add(u);
                u = (User) in.readObject();
            }
        } catch (EOFException | FileNotFoundException e) {
            //end of file
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean LogIn(String logIn) {
        //LogIn*MichaelCon*password123
        String[] checkUser = logIn.split("\\*");
        ReadUsers();
        for (User u : userArrayList) {
            if (u.getUsername().equals(checkUser[1]) && u.getPassword().equals(checkUser[2])) {
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
                System.out.println("Failed creating new fail for user");
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
            System.out.println("Error in Sign Up");
        }
        return false;
    }

    public ArrayList<User> SearchUser(String userSearch) {
        //SearchUser*Name
        String[] checkUser = userSearch.split("\\*");

        ArrayList<User> result = new ArrayList<>();
        ReadUsers();

        for (User user : userArrayList) {
            if (user.getUsername().toLowerCase().contains(checkUser[1].toLowerCase())) {
                result.add(user);
            }
        }

        return result;
    }
}