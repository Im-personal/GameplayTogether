package little.cookie.gameplaytogether;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerDisplay extends View {

    private Class<?> gamepadClass;
    private Constructor<?> constructor;
    private Method onTouch;
    private Method onMove;
    private Method onRelease;
    private Object controller;

    public ControllerDisplay(Context context, AttributeSet attrs) {
        super(context,attrs);
        gamepadClass = MainActivity.gamepadClass;

        initOnClick();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            constructor = gamepadClass.getConstructor(float.class,float.class);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        Activity activity = (Activity) this.getContext();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        controller = constructor.newInstance(width, height);

        onTouch = gamepadClass.getMethod("onTouch", float.class, float.class, int.class);
        onMove = gamepadClass.getMethod("onMove", float.class, float.class, int.class);
        onRelease = gamepadClass.getMethod("onRelease", float.class, float.class, int.class);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private void initOnClick() {
        setOnTouchListener((v, event) -> {

            float x = event.getX();
            float y = event.getY();
            int n = event.getPointerCount();
            int action = event.getActionMasked();
            String key;
            try {
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    key = (String)onTouch.invoke(controller,x,y,n);
                    if(key!=null) {
                        Log.d("key", "down: " + key);
                        GameplayActivity.send.add("down:"+key);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    key = (String)onMove.invoke(controller,x,y,n);
                    if(key!=null) {
                        Log.d("key", "move: " + key);
                        GameplayActivity.send.add("move"+key);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    key = (String)onRelease.invoke(controller,x,y,n);
                    if(key!=null) {
                        Log.d("key", "up: " + key);
                        GameplayActivity.send.add("up:"+key);
                    }
                    v.performClick();
                    break;
            }

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            return true;
        });

    }


}
