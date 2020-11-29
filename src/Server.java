import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private int port;
    public ArrayList<User> clients;

    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket socket = serverSocket.accept();
                UserHandler userHandler = new UserHandler(socket);
                userHandler.run();
            }
        }
    }

    private Server(int port) {
        this.port = port;
        this.clients = new ArrayList<User>();
    }

//    private void run() throws IOException {
//        ServerSocket server = new ServerSocket(port);
//        System.out.println("Server is running on port " + this.port);
//
//        while(true) {
//            // Accepts a new connection (client) to the server.
//            Socket client = server.accept();
//
//            String name = new Scanner(client.getInputStream()).nextLine();
//            System.out.println("Client's name: " + name);
//            String username = new Scanner(client.getInputStream()).nextLine();
//            System.out.println("Client's username: " + username);
//            String password = new Scanner(client.getInputStream()).nextLine();
//            System.out.println("Client's password: " + password);
//
//            User newUser = new User(client, name, username, password);
//            clients.add(newUser);
//
//            newUser.getOutStream().println("Welcome " + name + "!");
//            newUser.getOutStream().println("Messages: ");
//
//            Thread clientThread = new Thread(new UserHandler(this, newUser));
//            clientThread.start();
//
//
//        }
//    }


}