import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;

    public static void main(String[] args) throws IOException {
        Client user = new Client("192.168.100.210", 8080);
        user.run();
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws IOException {
        Socket user = new Socket(host, port);

        System.out.println("User has connected to the server");

        PrintStream outputStream = new PrintStream(user.getOutputStream());

        // Asks for Name and sends to server
        Scanner scan = new Scanner(System.in);
        System.out.println("Name:");
        String name = scan.nextLine();
        outputStream.println(name);

        // Asks for Username and sends to server
        System.out.println("Username:");
        String username = scan.nextLine();
        outputStream.println(username);

        // Asks for Password and sends to server
        System.out.println("Password:");
        String password = scan.nextLine();
        outputStream.println(password);

        // Thread for handling messages
        Thread t1 = new Thread(new MessageHandler(user.getInputStream()));
        t1.start();

        while (scan.hasNextLine()) {
            outputStream.println(scan.nextLine());
        }

        outputStream.close();
        scan.close();
        user.close();
    }

}