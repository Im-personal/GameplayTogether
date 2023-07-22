package little.cookie.gameplaytogether.controllers;

import java.util.ArrayList;

public abstract class GameController {

    public final float width;
    public final float height;

    public ArrayList<ControllerScreen> controllerScreens = new ArrayList<>();

    public ControllerScreen actualScreen = null;

    public final float[] tx = new float[10];
    public final float[] ty = new float[10];
    public final float[] ux = new float[10];
    public final float[] uy = new float[10];
    public final boolean[] isDown = new boolean[10];

    public GameController(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public abstract String down(float x, float y, int n);
    public abstract String move(float x, float y, int n);
    public abstract String up(float x, float y, int n);

    public String onTouch(float x, float y, int n)
    {
        tx[n]=x;
        ty[n]=y;

        isDown[n]=true;

        return down(x,y,n);
    }

    public String onMove(float x, float y, int n)
    {
        isDown[n]=true;

        return move(x,y,n);
    }

    public String onRelease(float x, float y, int n)
    {
        ux[n] = x;
        uy[n] = y;

        isDown[n]=false;

        return up(x,y,n);
    }



}
