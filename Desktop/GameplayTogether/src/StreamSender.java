import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

public class StreamSender {

    public boolean imMuted=false;
    public boolean intMuted = false;

    private WebSocketContainer container;
    private Session session;
    private CountDownLatch latch;

    public StreamSender(String url, int width, int height, int bitrate, int samplerate) throws DeploymentException, IOException, URISyntaxException, InterruptedException {

        container = ContainerProvider.getWebSocketContainer();
        //URI uri = new URI(url);
        //container.connectToServer(this, uri);
       // WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Endpoint endpoint = new StreamSender();
        container.connectToServer(this, new URI(url));
        latch.await();


    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;

        session.getBasicRemote().sendText("sender");

    }

}
