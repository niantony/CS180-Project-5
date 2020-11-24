import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerHandler implements Runnable{

    private final Socket clientSocket;

    public ServerHandler(Socket clientSocket) {
        if (clientSocket.equals(null)) {
            throw new NullPointerException();
        }
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                wait(100);
                var outputStream = this.clientSocket.getOutputStream();
                var writer = new BufferedWriter(new OutputStreamWriter(outputStream));




            } catch (IOException i) {
                i.printStackTrace();
            } catch (InterruptedException a) {
                a.printStackTrace();
            }
        }

    }

    public void writeToFile() {


    }
}
