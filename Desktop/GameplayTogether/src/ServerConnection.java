
import org.json.JSONObject;
import javax.websocket.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class ServerConnection {
    private static CountDownLatch latch;

    private String url;

    public ServerConnection(String url)
    {
        this.url = url;
    }


    public boolean start()
    {

        int port = 8080;
        try (Socket socket = new Socket(url, port)) {
            System.out.println("yay");
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }

        return false;
    }

}
