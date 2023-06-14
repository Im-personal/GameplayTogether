import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

public class ScreenStreamer {
    private MediaPlayer mediaPlayer;
    private String media;
    private String[] options;
    private void setUp(String url, int bitrate, int audioBitrate, int sampleRate, float scale ) {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer =  mediaPlayerFactory.mediaPlayers().newMediaPlayer();


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


    public void start(){
        mediaPlayer.media().play(media, options);
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
