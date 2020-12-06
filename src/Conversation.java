import java.util.ArrayList;
import java.io.*;

/**
 * CS 180 Project 5 -- Conversation.java
 *
 * Represents a conversation and includes the name, users, and a file of the messages for the conversation.
 *
 * @author Antony Ni, G17
 * @author Ishika Vachali, Online
 * @author Michael Con, Online
 * @author Sruthi Koukuntla, LC3
 *
 * @version December 6, 2020
 */
public class Conversation implements Serializable {
    private String nameOfConversation; //name of the Chat, can be used to identify
    private ArrayList<User> conversationList; //the participants in the conversation
    private File messages;  //file of the messages for the conversation

    /**
     * Constructor for conversation
     *
     * @param name name of the conversation
     * @param users users that are participants of the conversation
     * @param messages file containing the messages for the conversation
     */
    public Conversation (String name, ArrayList<User> users, File messages) {
        this.nameOfConversation = name;
        this.conversationList = users;
        this.messages = messages;
    }

    /**
     * Returns the name of the conversation
     * @return the name of the conversation
     */
    public String getName() {
        return this.nameOfConversation;
    }

    /**
     * Returns the participants in the conversation
     * @return the participants in the conversation
     */
    public ArrayList<User> getParticipants() {
        return this.conversationList;
    }

    /**
     * Returns the file containing the messages for the conversation
     * @return the file containing the messages for the conversation
     */
    public File getMessages() {
        return messages;
    }

}
