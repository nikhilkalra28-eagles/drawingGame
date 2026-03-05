import java.awt.*;

public class mario {

    int xpos;
    int ypos;
    int speed;
    double dx;
    double dy;
    int width;
    int height;
    Rectangle hitbox;
    boolean isAlive = true;
    boolean up = false;
    boolean down = false;
    boolean right = false;
    boolean left = false;

    public mario(int xposInput, int yposInput, double dxInput, double dyInput, int widthInput, int heightInput) {
        xpos = xposInput;
        ypos = yposInput;
        dx = dxInput;
        dy = dyInput;
        width = widthInput;
        height = heightInput;

        hitbox = new Rectangle(xpos, ypos, width, height);
        // convention for making a rectangle


    }

    public void move() { //exam
//  mario keys
        if (up) {
            ypos = ypos - (int) dy;
        }
        if (down) {
            ypos = ypos + (int) dy;
        }
        if (right) {
            xpos = xpos + (int) dx;
        }
        if (left) {
            xpos = xpos - (int) dx;
        }

        hitbox = new Rectangle(xpos, ypos, width, height);

    }
}
