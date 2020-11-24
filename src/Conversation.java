import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.Serializable;

public class Conversation implements Serializable {
    public String nameOfConversation; //Name of the Chat, can be used to identify
    public ArrayList<User> conversationList; //the participants in the conversation
    public String fileName;

    public Conversation (String name, ArrayList<User> users) throws IOException {
        this.nameOfConversation = name;
        this.conversationList = users;

        this.fileName = nameOfConversation + "MessagesTextFile";

        File messages = new File(fileName);

        if (messages.createNewFile()) {
            System.out.println("File Created Successfully");
        } else {
            System.out.println("File already Exists");
        }

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

    public ArrayList<User> getUsers() {
        return conversationList;
    }

}
