import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CS 180 Project 5 -- Server.java
 * <p>
 * Server, a new thread is started for every new user
 */
public class Server {
    private int port;
    public ArrayList<User> clients;

    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is running on port 8080");
            while (true) {
                //Server socket for the Server, wait to accept a connection
                System.out.println("Waiting for client connection...");
                Socket socket = serverSocket.accept();
                System.out.println("A client connected to the server from address " +
                        socket.getLocalAddress() + " and port " + socket.getLocalPort());
                //Starts a thread for the new user, multi-threaded
                Thread t = new Thread(new UserHandler(socket));
                t.start();
            }
        }
    }

    private Server(int port) {
        this.port = port;
        this.clients = new ArrayList<User>();
    }
}