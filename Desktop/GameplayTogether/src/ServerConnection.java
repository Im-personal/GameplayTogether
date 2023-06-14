import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerConnection {
    private final String url;
    private final int port;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private volatile boolean running;

    public ServerConnection(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public boolean start() {
        try {
            socket = new Socket(url, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            running = true;
            new Thread(this::receive).start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void receive() {
        byte[] buffer = new byte[1024];
        while (running) {
            try {
                int bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                String data = new String(buffer, 0, bytesRead);

                processData(data);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public boolean send(String data) {
        try {
            out.write(data.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean connected=false;

    private void processData(String data)
    {


        switch(data)
        {
            case "conn":
                connected=true;
                break;
            default:
                System.out.print(data);
                break;
        }
    }

    public boolean stop() {
        running = false;
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
