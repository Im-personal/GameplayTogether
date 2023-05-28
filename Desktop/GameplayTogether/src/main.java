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
    static private StreamSender stream;

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

            stream.imMuted=!stream.imMuted;
            if(stream.imMuted)
                btn_mute.setText("Unmute me");
            else
                btn_mute.setText("Mute me");

        });

        btn_mute_int.addActionListener(e -> {

            stream.intMuted=!stream.intMuted;
            if(stream.intMuted)
                btn_mute_int.setText("Unmute interlocutor");
            else
                btn_mute_int.setText("Mute interlocutor");
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
        JTextField tf_width = new JTextField();
        JTextField tf_height = new JTextField();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        tf_width.setText(width+"");
        tf_height.setText(height+"");
        tf_bitrate.setText(8000000+"");
        tf_samplerate.setText(44+"");

        inputFrame.setLayout(new GridLayout(6,2));
        inputFrame.add(new JLabel("Server: "));
        inputFrame.add(tf_link);
        inputFrame.add(new JLabel("Width: "));
        inputFrame.add(tf_width);
        inputFrame.add(new JLabel("Height: "));
        inputFrame.add(tf_height);
        inputFrame.add(new JLabel("Bitrate: "));
        inputFrame.add(tf_bitrate);
        inputFrame.add(new JLabel("Samplerate: "));
        inputFrame.add(tf_samplerate);

        inputFrame.add(btn_connect);


        btn_connect.addActionListener(e -> {
            btn_connect.setText("Connecting...");

            try {
                stream = new StreamSender(tf_link.getText(),Integer.parseInt(tf_width.getText()),
                        Integer.parseInt(tf_height.getText()),Integer.parseInt(tf_bitrate.getText()),
                        Integer.parseInt(tf_samplerate.getText()));
            } catch (DeploymentException | IOException | URISyntaxException | InterruptedException deploymentException) {
                deploymentException.printStackTrace();
            }

            //inputFrame.setVisible(false);
            //chatFrame.setVisible(true);
        });


        inputFrame.setVisible(true);
    }

}
