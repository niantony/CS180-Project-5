import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatServer{
    private final ServerSocket serverSocket;


    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //this will create a NEW server handler for each new user that connects
    public void serveClient () {
        InetAddress address;
        String hostName;
        int port;
        Socket clientSocket;
        ServerHandler handler;
        Thread handlerThread;
        int clientCount = 0;

        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        hostName = address.getCanonicalHostName();
        port = this.serverSocket.getLocalPort();

        while (true) {

            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                break;
            }

            handler = new ServerHandler(clientSocket);
            handlerThread = new Thread(handler);
            handlerThread.start();

            clientCount++;

        }
    }
}
