package little.cookie.gameplaytogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameplayActivity extends AppCompatActivity {

    static ArrayList<String> send = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        new Thread(()->startConnection(MainActivity.host,8080)).start();
    }


    public boolean startConnection(String hostname, int port)
    {
        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text = "receiver";
            writer.println(text);
            Log.d("dataforme","Connection data sent!");

            startWaiting(output,writer);
            Log.d("dataforme","is waiting for new messages now");

        } catch (UnknownHostException ex) {
            Log.d("dataforme","There is no such server: " + ex.getMessage());
            return false;
        } catch (IOException ex) {
            Log.d("dataforme","Input-Output error: " + ex.getMessage());
            return false;
        }

        return true;
    }

    private void startWaiting(OutputStream output, PrintWriter writer) {

        while (true) {

            if (send.size() > 0) {
                String data = send.get(0);
                send.remove(0);

                writer.println(data);
            }

        }

    }

}