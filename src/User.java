import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class User {
    private PrintStream streamOut;
    private InputStream streamIn;
    private Socket client;
    public String name;
    public String username; //This will be used as the unique ID for each user
    private String password;
    public ArrayList<Conversation> conversations;  //all of the conversations this User participates in

    public User(Socket client, String name, String username, String password) throws IOException {
        this.streamOut = new PrintStream(client.getOutputStream());
        this.streamIn = client.getInputStream();
        this.client = client;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(Socket client, String name, String username, String password, ArrayList<Conversation> conversations) {
        this.name = name;
        this.username = username;
        this.password = password;

        //this.conversations = conversations;
    }

    public PrintStream getOutStream(){
        return this.streamOut;
    }

    public InputStream getInputStream(){
        return this.streamIn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

}