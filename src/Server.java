import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private int port;
    public ArrayList<User> clients;

    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to server");
                Thread t = new Thread(new UserHandler(socket));
//                UserHandler userHandler = new UserHandler(socket);
                t.start();
            }
        }
    }

    private Server(int port) {
        this.port = port;
        this.clients = new ArrayList<User>();
    }

}