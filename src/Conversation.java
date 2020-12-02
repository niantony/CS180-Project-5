import java.util.ArrayList;
import java.io.*;

public class Conversation implements Serializable {
    private String nameOfConversation; //Name of the Chat, can be used to identify
    private ArrayList<User> conversationList; //the participants in the conversation
    private File messages;

    public Conversation (String name, ArrayList<User> users, File messages) {
        this.nameOfConversation = name;
        this.conversationList = users;
        this.messages = messages;
    }

    //return the name of the conversation
    public String getName () {
        return this.nameOfConversation;
    }

    //add a new participant to the conversation
    public void addParticipant (User newUser) {
        this.conversationList.add(newUser);

    }

    //remove a user from the conversation
    public void removeParticipant (User user) {
        this.conversationList.remove(user);
    }

    //return int of number of users in the conversation
    public int numberOfParticipants () {
        return this.conversationList.size();
    }

    public File getMessages() {
        return messages;
    }

    public void setMessages(File file) {
        this.messages.delete();
        this.messages = file;
    }

}
