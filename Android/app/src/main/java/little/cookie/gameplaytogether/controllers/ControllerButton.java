package little.cookie.gameplaytogether.controllers;

public class ControllerButton {

    public float x,y,w,h;

    public float r,g,b,a=0.2f;
    public boolean isVisible = true;
    public String value;

    public ControllerButton(float x, float y, float w, float h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setColor(float r, float g, float b)
    {
        this.r=r;
        this.g=g;
        this.b=b;
    }

    public void setColor(float r, float g, float b,float a)
    {
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
    }

    public boolean isTouched(float tx, float ty)
    {
        return (tx>=x&&ty>=y&&tx<=(x+w)&&ty<=(x+h));
    }

}
