//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// Graphics Libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
public class BasicGameApp implements Runnable,MouseMotionListener, MouseListener, KeyListener {

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;


    //Variable Definition Section
    Image background;
    Image marioImg;
    Image chest;
    mario marioPlayer;

    int mouseX = 0;
    int mouseY = 0;
    // Rect location
    int rectX = 300;
    int rectY = 200;
    int rectW = 400;
    int rectH = 250;
    Rectangle rectDetect = new Rectangle(rectX - 15, rectY - 15, rectW + 30, rectH + 30);
    int startX = -1;
    int startY = -1;
    int startRadius = 10;
    boolean hasStart = false;
    // triangle detection
    Rectangle [] hitBoxes;

    // location of triangle
    int[] x = {300, 500, 700};
    int[] y = {550, 250, 550};
    int level = 1; // shows level
    ArrayList<Point> trail = new ArrayList<>();
    boolean drawing = false;
    boolean shapeDone = false;
    // corners of triangle and square
    boolean hitA = false;
    boolean hitB = false;
    boolean hitC = false;
    boolean hitD = false;
    boolean youWin = false;
    Rectangle chestBox;


    //You can set their initial values too

    // Like Mario mario = new Mario(); //



    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor
        setUpGraphics();
        // triangle detection
        hitBoxes = new Rectangle[] {
                new Rectangle(0, 50, 1000, 170),
                new Rectangle (3,573,1000,100),
                new Rectangle (5,200,400,150),
                new Rectangle(0,360,270,200),
                new Rectangle(278,367,50,50),
                new Rectangle(279,432,47,50),
                new Rectangle(340,340,50,50),
                new Rectangle(315, 380, 50,50),
                new Rectangle(390, 215, 50,100),
                new Rectangle(730,0,200,1000),
                new Rectangle(600,65,200,300),
                new Rectangle(655,394,100,50),
                new Rectangle(690,455,100,50),
                new Rectangle(556,259,100,50),
                new Rectangle(450,380,100,150),
                new Rectangle(510,415,80,125),
                new Rectangle(410,415,80,105),
                new Rectangle(460,336,80,105)

        };



        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);

        //variable and objects
        background = Toolkit.getDefaultToolkit().getImage("grey.jpg");
        marioImg = Toolkit.getDefaultToolkit().getImage("mariooo.png");
        chest = Toolkit.getDefaultToolkit().getImage("chest.jpg");
        marioPlayer = new mario(10, 100, 5, 5, 150, 150);
        chestBox = new Rectangle(500, 300, 120, 120);
        //create (construct) the objects needed for the game


    }
    // end BasicGameApp constructor

    public void moveThings() {
        // mario can move
        if (level == 3) {
            marioPlayer.move();

            if (marioPlayer.hitbox.intersects(chestBox)) {
                youWin = true;
            }
        }

        // rectangle detection
        if (level == 2) {
            if (!rectDetect.contains(mouseX, mouseY)) {
                drawing = false;
                trail.clear();
                return;
            }
        }


        // level 2 rectangle corner hitting and advancing
        if (level == 2) {

            // corners of rectangle
            int ax = rectX, ay = rectY;
            int bx = rectX + rectY, by = rectY;
            int cx = rectX + rectW, cy = rectY + rectH;
            int dx = rectX, dy = rectY + rectH;

            // distance from cursor to corners
            double dA = Math.hypot(mouseX - ax, mouseY - ay);
            double dB = Math.hypot(mouseX - bx, mouseY - by);
            double dC = Math.hypot(mouseX - cx, mouseY - cy);
            double dD = Math.hypot(mouseX - dx, mouseY - dy);

            // if all these are hit
            if (dA < 40) hitA = true;
            if (dB < 40) hitB = true;
            if (dC < 40) hitC = true;
            if (dD < 40) hitD = true;

            // if the corners are hit, level up
            if (hitA && hitB && hitC && hitD) {
                shapeDone = true;
            }

        }

        // level 1 advancing
        if (!drawing) return;
        boolean nearStart = hasStart && mouseX >= startX - startRadius && mouseX <= startX + startRadius &&  mouseY >= startY - startRadius && mouseY <= startY + startRadius;
        if (level == 1) {
            for (int i = 0; i < hitBoxes.length; i++){
                if (hitBoxes[i].contains(mouseX, mouseY)) {
                    drawing = false;
                    trail.clear();
                    level = 1;
                    return;
                }
            }
        }



        // corners of triangle
        int ax = x[0], ay = y[0];
        int bx = x[1], by = y[1];
        int cx = x[2], cy = y[2];

        // distance from mouse to each corner of triangle
       double dA = Math.hypot(mouseX - ax, mouseY - ay);
       double dB = Math.hypot(mouseX - bx, mouseY - by);
       double dC = Math.hypot(mouseX - cx, mouseY - cy);


        // game over
        if (mouseX < 100 || mouseX > 900 || mouseY < 50 || mouseY > 650)  {
            trail.clear();
            drawing = false;
            return;
        }
        // drawing when cursor hits
        if (drawing) {
            trail.add(new Point(mouseX, mouseY));
        }
        // level up
        if (dA < 40) hitA = true;
        if (dB < 40) hitB = true;
        if (dC < 40) hitC = true;

        // advancing once corners are hit
        if (hitA && hitB && hitC) {
            shapeDone = true;
        }

        // level up and restarts corners
        if (shapeDone && nearStart) {
            level++;
            drawing = true;
            trail.clear();
            hasStart = true;
            shapeDone = false;
            startX = mouseX;
            startY = mouseY;
            hitA = false;
            hitB = false;
            hitC = false;
            return;



        }
    }


    //Paints things on the screen using bufferStrategy
    private void render() {


        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        // makes mario and chest
        if (level == 3) {
            g.drawImage(marioImg, marioPlayer.xpos, marioPlayer.ypos, marioPlayer.width, marioPlayer.height, null);
            g.drawImage(chest, 500, 300, 120, 120, null);

        }

        // Instructions and Text
        g.setColor(Color.black);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        g.drawString("Instructions: trace the images without leaving the line! ", 140, 40);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        g.drawString("If your cursor moves off the shape, the game restarts", 275, 70);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        g.drawString("Level " + level, 50, 650);
        // you win text on screen
        if (youWin) {
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("You Win! ", 250, 250);
        }

        //draw the images and shape color
        if (level == 1) {
            g.setColor(Color.red);
            g.setStroke(new BasicStroke(30));
            g.drawPolygon(x, y, 3);
            g.setStroke(new BasicStroke(1));
        }

        // Signature: drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)

        // pen drawing color
        if (level == 1) {
            g.setColor(Color.black);

        }


        // drawing rectangle
        else if (level == 2) {
            g.setColor(Color.green);
            g.setStroke(new BasicStroke(30));
            g.drawRect(rectX, rectY, rectW, rectH);
            g.setStroke(new BasicStroke(1));

        }

        // drawing
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(4));

        // draws connected path
        for (int i = 1; i < trail.size(); i++) {
            Point p = trail.get(i - 1);
            Point q = trail.get(i);
            g.drawLine(p.x, p.y, q.x, q.y);
        }
        g.setStroke(new BasicStroke(1));


        // Keep the code below at the end of render()
        g.dispose();
        bufferStrategy.show();

    }



    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    // PSVM: This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            moveThings();  //move all the game objects
            render();  // paint the graphics
            pause(10); // sleep for 10 ms
        }
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private Image getImage(String filename) {
        return Toolkit.getDefaultToolkit().getImage(filename);
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }


    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();


    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // mouse pressed booleans
        drawing = true;
        trail.clear();
        mouseX = e.getX();
        mouseY = e.getY();
        trail.add(new Point(mouseX, mouseY));
        startX = mouseX;
        startY = mouseY;
        hasStart = true;
        shapeDone = false;
        hitA = false;
        hitB = false;
        hitC = false;
        hitD = false;

        // coordinates
       // System.out.println(mouseX);
       // System.out.println(mouseY);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // if mouse is released, drawing resets
        drawing = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        int key = e.getKeyCode();
        //keys
        if (key == 87) { // w is pressed
         marioPlayer.up = true;
        }
        if (key == 83) { // d
            marioPlayer.down = true;
        }
        if (key == 65) { // a
            marioPlayer.left = true;
        }
       if (key == 68) { // s
           marioPlayer.right = true;
       }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(e.getKeyCode());
        int key = e.getKeyCode();
        // keys
        if (key == 87) { // w is pressed
            marioPlayer.up = false;
        }
        if (key == 83) { // d
            marioPlayer.down = false;
        }
        if (key == 65) { // a
            marioPlayer.left = false;
        }
        if (key == 68) { // s
            marioPlayer.right = false;
        }


    }
}






//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
