import java.io.*;

/**
 * CS 180 Project 5 -- User.java
 *
 * Represents a user of the program with a name, username, password, and file of conversations.
 *
 * @author Antony Ni, G17
 * @author Ishika Vachali, Online
 * @author Michael Con, Online
 * @author Sruthi Koukuntla, LC3
 *
 * @version December 6, 2020
 */
public class User implements Serializable {
    private String name; //Name of the user, does not need to be Unique
    private String username; //Username, this will be used as the unique ID for each user
    private String password; //Password of the User
    private File conversations;  //all of the conversations this User participates in

    /**
     * Constructor for the user
     *
     * @param name full name of the user
     * @param username username of the user
     * @param password password of the user
     * @param conversations file containing all of the user's conversations
     */
    public User(String name, String username, String password, File conversations) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.conversations = conversations;
    }

    /**
     * Returns the name of the user
     * @return name of the user
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the user to a new name
     * @param newName new name for the user
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns the username of the user
     * @return username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username of the user to a new username
     * @param newUsername new username for the user
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Returns the password of the user
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password of the user to a new password
     * @param newPassword new password for the user
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Returns the file containing the user's conversations
     * @return the file containing the user's conversations
     */
    public File getConversations() {
        return conversations;
    }

}
