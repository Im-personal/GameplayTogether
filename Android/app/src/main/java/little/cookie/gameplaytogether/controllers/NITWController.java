package little.cookie.gameplaytogether.controllers;

import little.cookie.gameplaytogether.controllers.ControllerButton;
import little.cookie.gameplaytogether.controllers.ControllerScreen;
import little.cookie.gameplaytogether.controllers.GameController;

public class NITWController extends GameController {

    private ControllerScreen cs1;

    public NITWController(float width, float height) {
        super(width, height);

        initControllers();
    }

    private void initControllers() {
        cs1 = new ControllerScreen();
        ControllerButton cb_left = new ControllerButton(0.0f,0.0f,0.5f,1f);
        cb_left.value = "a";
        ControllerButton cb_right = new ControllerButton(0.5f,0.0f,0.5f,1f);
        cb_right.value = "d";
        cs1.controllerButtons.add(cb_left);
        cs1.controllerButtons.add(cb_right);
        controllerScreens.add(cs1);
        actualScreen=cs1;
    }

    boolean isJump = false;
    @Override
    public String down(float x, float y, int n) {

        float mtx = x/width;
        float mty = y/height;

        if(actualScreen==cs1)
        {
            float tx2 = tx[0]/width;

            if(n==2)
            {
                if((mtx>0.5f&&tx2<0.5f)||(mtx<0.5f&&tx2>0.5f)) {
                    isJump=true;
                    return "w";
                }
            }
        }

        for(ControllerButton b: actualScreen.controllerButtons)
        {
            if(b.isTouched(mtx,mty))
                return b.value;
        }

        return null;
    }

    @Override
    public String move(float x, float y, int n) {

        return null;
    }

    @Override
    public String up(float x, float y, int n) {

        if(isJump)
        {
            isJump=false;
            return "w";
        }


        float tx = x/width;
        float ty = y/height;

        for(ControllerButton b: actualScreen.controllerButtons)
        {
            if(b.isTouched(tx,ty))
                return b.value;
        }


        return null;
    }
}
