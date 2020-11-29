import java.io.InputStream;
import java.util.Scanner;

public class MessageHandler implements Runnable{
    private InputStream server;

    public MessageHandler(InputStream server) {
        this.server = server;
    }

    public void run() {
        Scanner scan = new Scanner(server);
        String message;
        while(scan.hasNextLine()) {
            message = scan.nextLine();
            System.out.println(message);
        }
        scan.close();
    }
}
