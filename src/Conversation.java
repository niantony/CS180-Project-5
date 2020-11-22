import java.util.ArrayList;

public class Conversation {
    public String nameOfConversation; //Name of the Chat, can be used to identify
    public ArrayList<User> conversationList; //the particpants in the conversation

    public Conversation (String name, ArrayList<User> users) {
        this.nameOfConversation = name;
        this.conversationList = users;
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

}
