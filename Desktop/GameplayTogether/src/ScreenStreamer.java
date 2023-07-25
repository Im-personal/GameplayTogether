import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScreenStreamer {
    private String url;
    private int bitrate;
    private int audioBitrate;
    private int sampleRate;

    private boolean isWaiting = true;

    private int status = 0;

    private void setUp(String url, int bitrate, int audioBitrate, int sampleRate) {
        this.url = url;
        this.bitrate = bitrate;
        this.audioBitrate = audioBitrate;
        this.sampleRate = sampleRate;

    }

    public void start(boolean ahha)
    {
        try {
            String command = "ffmpeg -list_devices true -f dshow -i dummy";
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){

        String audio = "@device_cm_{33D9A762-90C8-11D0-BD43-00A0C911CE86}\\wave_{24E93A82-6B5E-4CD9-AA2B-1437E27014A0}";

        String command = "ffmpeg -loglevel debug -f gdigrab -framerate 30 -i desktop -f dshow -i audio=\""+audio+"\" -c:v libx264 -preset veryfast -maxrate "+bitrate+"k -bufsize "+(bitrate*2)+"k -pix_fmt yuv420p -g 50 -c:a aac -b:a "+audioBitrate+"k -ac 2 -ar "+sampleRate+" -f flv "+url+ " 2> ffmpeg.log";
        try {
            Process p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean startConnection(String hostname, int port)
    {
        new Thread(()-> {
            try (Socket socket = new Socket(hostname, port)) {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String text = "sender";
                writer.println(text);
                System.out.println("Connection data sent!");

                startWaiting(output,writer,socket.getInputStream());
                System.out.println("is waiting for new messages now");

            } catch (UnknownHostException ex) {
                System.out.println("There is no such server: " + ex.getMessage());
                status = -1;
            } catch (IOException ex) {
                System.out.println("Input-Output error: " + ex.getMessage());
                status = -1;
            }
        }).start();

        while(status==0){}

        return status>0;
    }

    public void startWaiting(OutputStream output, PrintWriter writer,InputStream input) throws IOException {

            status = 1;

            while (isWaiting)
            {
                    System.out.println("Waiting");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    //String line;

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    String line = new String(buffer, 0, bytesRead);
                    System.out.println(line);
                }

            }
        System.out.println("Listening is closed");

    }

    public void stop()
    {
        isWaiting = false;
    }

    public ScreenStreamer(String url)
    {
        setUp(url,800,128,44100);
    }

    public ScreenStreamer(String url, float scale)
    {
        setUp(url,800,128,44100);
    }

    public ScreenStreamer(String url, int bitrate)
    {
        setUp(url,bitrate,128,44100);
    }

    public ScreenStreamer(String url, int bitrate, int audioRate)
    {
        setUp(url,bitrate,audioRate,44100);
    }

    public ScreenStreamer(String url, int bitrate, int audioRate,int sampleRate)
    {
        setUp(url,bitrate,audioRate,sampleRate);
    }
}
