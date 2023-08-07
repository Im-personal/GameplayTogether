import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ComputerController {

    private ArrayList<String> keys = new ArrayList<>();

    private Robot robot;

    public ComputerController()
    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void invoke(String key)
    {
        if(key.startsWith("down:"))
        {
            String clearKey = key.replace("down:","").replace("\n","");
            int event = toKeyEvent(clearKey);
            if(event!=0)
            robot.keyPress(event);
        }

        if(key.startsWith("up:"))
        {
            String clearKey = key.replace("up:","").replace("\n","");
            int event = toKeyEvent(clearKey);
            if(event!=0)
            robot.keyRelease(event);
        }
    }

    private int toKeyEvent(String clearKey) {

        return switch (clearKey) {
            case "a" -> KeyEvent.VK_A;
            case "d" -> KeyEvent.VK_D;
            case "w" -> KeyEvent.VK_SPACE;
            default -> 0;
        };

    }

}
