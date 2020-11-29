import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class UserHandler implements Runnable {
    private Socket socket;

    public UserHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String userInput = input.readLine();

                if (userInput.contains("LogIn*")) {
                    if (LogIn(userInput)) {
                        System.out.println("Success");
                    } else {
                        System.out.println("Fail");
                    }
                }

                if (userInput.contains("SignUp*")) {
                    if (SignUp(userInput)) {
                        System.out.println("Success");
                    } else {
                        System.out.println("Fail");
                    }
                }

                if (userInput.contains("NewConversation*")) {

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

    public boolean LogIn(String logIn) {
        //LogIn*MichaelCon*password123
        String[] checkUser = logIn.split("\\*");
        ArrayList<String> users = new ArrayList<>();

        try {
            File UsersList = new File ("UsersList.txt");
            FileReader fr = new FileReader(UsersList);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                users.add(line);
            }

        } catch (IOException i) {
            System.out.println("File does not exist");
        }

        for (String userLine : users) {
            String[] arrayLine = userLine.split("\\*");
            if (checkUser[1].equals(arrayLine[0])) {
                if (checkUser[2].equals(arrayLine[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean SignUp(String signUp) {
        //SignUp*MichaelCon*password123
        try {
            File UsersList = new File ("UsersList.txt");
            FileWriter fw = new FileWriter(UsersList);
            BufferedWriter bw = new BufferedWriter(fw);

            String[] checkUser = signUp.split("\\*");

            StringBuilder sb = new StringBuilder();
            sb.append(checkUser[2]);
            sb.append("*");
            sb.append(checkUser[1]);
            bw.write(sb.toString());

            return true;
        } catch (IOException i) {
            System.out.println("Error in Sign Up");
        }

        return false;
    }



}
