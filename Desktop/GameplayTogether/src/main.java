import javax.swing.*;
import javax.websocket.DeploymentException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

public class main {


    static private JFrame inputFrame;
    static private JFrame chatFrame;
    static private ScreenStreamer stream;

    public static void main(String[] args) {
        initInputFrame();
        initChatFrame();
    }

    private static void initChatFrame() {
        chatFrame = new JFrame("Connected!");
        chatFrame.setSize(500,250);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Mute me, Mute not me, disconnect, any string data
        JButton btn_mute = new JButton("Mute me");
        JButton btn_mute_int = new JButton("Mute interlocutor");

        btn_mute.addActionListener(e -> {
            /*
            stream.imMuted=!stream.imMuted;
            if(stream.imMuted)
                btn_mute.setText("Unmute me");
            else
                btn_mute.setText("Mute me");
            //*/
        });

        btn_mute_int.addActionListener(e -> {
            /*
            stream.intMuted=!stream.intMuted;
            if(stream.intMuted)
                btn_mute_int.setText("Unmute interlocutor");
            else
                btn_mute_int.setText("Mute interlocutor");

             */
        });

        JLabel l_debugString = new JLabel("None\nNone\nNone");
        JPanel p_muteButtons = new JPanel();
        p_muteButtons.setLayout(new GridLayout(2,1));
        p_muteButtons.add(btn_mute);
        p_muteButtons.add(btn_mute_int);
        chatFrame.setLayout(new GridLayout(2,1));
        chatFrame.add(p_muteButtons);
        chatFrame.add(l_debugString);


    }

    private static void initInputFrame() {
        inputFrame = new JFrame("Gameplay together client");
        inputFrame.setSize(500,250);
        inputFrame.setLocationRelativeTo(null);

        inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inputFrame.setResizable(false);

        JButton btn_connect = new JButton("Connect");


        JTextField tf_link = new JTextField();
        JTextField tf_bitrate = new JTextField();
        JTextField tf_samplerate = new JTextField();
        JTextField tf_port = new JTextField();
        JTextField tf_audiobitrate = new JTextField();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        tf_link.setText("http//localhost");
        tf_port.setText(8080+"");
        tf_audiobitrate.setText(128+"");
        tf_bitrate.setText(3000+"");
        tf_samplerate.setText(44100+"");

        inputFrame.setLayout(new GridLayout(6,2));
        inputFrame.add(new JLabel("Server: "));
        inputFrame.add(tf_link);
        inputFrame.add(new JLabel("Port: "));
        inputFrame.add(tf_port);
        /**/
        inputFrame.add(new JLabel("Bitrate: "));
        inputFrame.add(tf_bitrate);
        inputFrame.add(new JLabel("Audio Bitrate: "));
        inputFrame.add(tf_audiobitrate);
        inputFrame.add(new JLabel("Samplerate: "));
        inputFrame.add(tf_samplerate);

        inputFrame.add(btn_connect);


        btn_connect.addActionListener(e -> {
            btn_connect.setText("Connecting...");

            String url = tf_link.getText();
            int port = Integer.parseInt(tf_port.getText());
            int bitrate = Integer.parseInt(tf_bitrate.getText());
            int audiobitrate = Integer.parseInt(tf_audiobitrate.getText());
            int samplerate = Integer.parseInt(tf_samplerate.getText());

            stream = new ScreenStreamer(url+":"+port,bitrate, audiobitrate, samplerate);
            if(stream.startConnection(url,port)){

            }
            else btn_connect.setText("Connect");


        });


        inputFrame.setVisible(true);
    }

}
