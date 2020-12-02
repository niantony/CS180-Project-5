import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class User implements Serializable {
    private PrintStream streamOut;
    private InputStream streamIn;
    private Socket client;
    private String name;
    private String username; //This will be used as the unique ID for each user
    private String password;
    private File conversations;  //all of the conversations this User participates in

    public User(Socket client, String name, String username, String password, File conversations) throws IOException {
        this.streamOut = new PrintStream(client.getOutputStream());
        this.streamIn = client.getInputStream();
        this.client = client;
        this.name = name;
        this.username = username;
        this.password = password;
        this.conversations = conversations;
    }
    public User(String name, String username, String password, File conversations) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.conversations = conversations;
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

    public File getConversations() {
        return conversations;
    }

    public Socket getClient() {
        return this.client;
    }
}
