package little.cookie.gameplaytogether.controllers;

public class ControllerFloatingGamepad extends ControllerButton{
    float R;

    public ControllerFloatingGamepad(float x, float y, float w, float h, float r) {
        super(x, y, w, h);
        this.R = r;
    }
}
