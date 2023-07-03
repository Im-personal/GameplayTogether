import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScreenStreamer {
    private MediaPlayer mediaPlayer;
    private String media;
    private String[] options;
    private String url;
    private void setUp(String url, int bitrate, int audioBitrate, int sampleRate, float scale ) {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer =  mediaPlayerFactory.mediaPlayers().newMediaPlayer();

        this.url = url;

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.exit(0);
            }
        });
        media = "screen://";
        options = new String[]{
                ":sout=#transcode{vcodec=h264,vb=" + bitrate + ",scale=" + scale + ",acodec=mp4a,ab=" + audioBitrate + ",channels=2,samplerate=" + sampleRate + "}:http{mux=ffmpeg{mux=flv},dst="+url+"/stream}",
                ":no-sout-rtp-sap",
                ":no-sout-standard-sap",
                ":sout-keep"
        };

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

        String command = "ffmpeg -f gdigrab -framerate 30 -i desktop -f dshow -i audio=\""+audio+"\" -c:v libx264 -preset veryfast -maxrate 3000k -bufsize 6000k -pix_fmt yuv420p -g 50 -c:a aac -b:a 160k -ac 2 -ar 44100 -f flv "+url;
        try {
            Process p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop()
    {
        mediaPlayer.release();
    }

    public ScreenStreamer(String url)
    {
        setUp(url,800,128,44100,1);
    }

    public ScreenStreamer(String url, float scale)
    {
        setUp(url,800,128,44100,scale);
    }

    public ScreenStreamer(String url, int bitrate)
    {
        setUp(url,bitrate,128,44100,1);
    }

    public ScreenStreamer(String url, int bitrate, int audioRate)
    {
        setUp(url,bitrate,audioRate,44100,1);
    }

    public ScreenStreamer(String url, int bitrate, int audioRate,int sampleRate)
    {
        setUp(url,bitrate,audioRate,sampleRate,1);
    }
}
