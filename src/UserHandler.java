import java.util.ArrayList;
import java.util.Scanner;

public class UserHandler implements Runnable {
    private Server server;
    private User user;

    UserHandler(Server server, User user) {
        this.server = server;
        this.user = user;
    }
    public void run() {
        String clientMessage;

        Scanner userInput = new Scanner(this.user.getInputStream());
        while (userInput.hasNextLine()) {
            clientMessage = userInput.nextLine();
            this.user.getOutStream().println(user.getName() + ": " + clientMessage);
        }
    }
}
