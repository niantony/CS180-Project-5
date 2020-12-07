import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CS 180 Project 5 -- Server.java
 *
 * Server - a new thread (instance of UserHandler) is started for every new user that connects to the server
 *
 * We visited the office hours for Albert Yu, who helped us make the program multi-threaded.
 *
 * @author Antony Ni, G17
 * @author Ishika Vachali, Online
 * @author Michael Con, Online
 * @author Sruthi Koukuntla, LC3
 *
 * @version December 6, 2020
 */
public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
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
}