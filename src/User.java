import java.util.ArrayList;

public class User {
    public String name;
    public String username;
    public String password;
    public ArrayList<User> conversations;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String name, String username, String password, ArrayList<User> conversations) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.conversations = conversations;
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
