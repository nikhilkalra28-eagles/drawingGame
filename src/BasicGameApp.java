// Nik Kalra
// CS1 Game Submission
//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// Graphics Libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
// Array lists are lists that grow and you can set their size
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
public class BasicGameApp implements Runnable,MouseMotionListener, MouseListener, KeyListener {

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;


    //Variable Definition Section
    Image background; // the background image
    Image marioImg; // the image of Mario for level 3
    Image chest; // the image of the chest for Level 3
    mario marioPlayer; // mario that contains the hitbox

    int mouseX = 0; // mouseX starting
    int mouseY = 0; // mouse Y starting
    // Rect location
    int rectX = 300; // rect dimension
    int rectY = 200; // rect dimension
    int rectW = 400; // rect dimension
    int rectH = 250; // rect dimension
    Rectangle rectDetect = new Rectangle(rectX - 15, rectY - 15, rectW + 30, rectH + 30);
    // the rectDetect is the rectantle detection for level 2
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
    // Array lists are lists that grow and you can set their size
    // I used Arraylist to store mouse positions when the user draws so then the points can be connected and show the trail
    boolean drawing = false;
    boolean shapeDone = false;
    // corners of triangle and square
    boolean hitA = false; // corner of triangle
    boolean hitB = false; //corner of triangle
    boolean hitC = false; // corner of triangle
    boolean hitD = false; // corner of triangle
    boolean youWin = false; // youWin boolean
    Rectangle chestBox; // chest hitbox


    //You can set their initial values too

    // Like Mario mario = new Mario(); //



    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor
        setUpGraphics();
        // triangle detection, I used rectangles as detection for level 1
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


        // adding mouse listener and mouse motion listener
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
    // Method for moving items
    public void moveThings() {
        // mario can move
        if (level == 3) {
            marioPlayer.move();
            // if mario intersects chest, the game is over and you win
            if (marioPlayer.hitbox.intersects(chestBox)) {
                youWin = true;
            }
        }

        // rectangle detection, ends if hits outside
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
            int ax = rectX, ay = rectY; // corner of rect
            int bx = rectX + rectY, by = rectY; // corner of rect
            int cx = rectX + rectW, cy = rectY + rectH; // corner of rect
            int dx = rectX, dy = rectY + rectH; // corner of rect

            // distance from cursor to corners
            // Math hypot calculates the distance between two points using the pythagorean theorem.
            // I used it to detect when the mouse is close to the corner of the shapes
            double dA = Math.hypot(mouseX - ax, mouseY - ay);
            double dB = Math.hypot(mouseX - bx, mouseY - by);
            double dC = Math.hypot(mouseX - cx, mouseY - cy);
            double dD = Math.hypot(mouseX - dx, mouseY - dy);

            // if all these are hit, the corner are hit
            if (dA < 40) {
                hitA = true;
            }
            if (dB < 40) {
                hitB = true;
            }
            if (dC < 40) {
                hitC = true;
            }
            if (dD < 40) {
                hitD = true;
            }

            // if the corners are hit, level up
            if (hitA && hitB && hitC && hitD) {
                shapeDone = true;
            }

        }

        // level 1 advancing if mouse is near where it began
        if (!drawing) return;
        boolean nearStart = hasStart && mouseX >= startX - startRadius && mouseX <= startX + startRadius &&  mouseY >= startY - startRadius && mouseY <= startY + startRadius;
        if (level == 1) {
            // if the rectangle hitboxes around the triangle are hit by the cursor, game resets
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
        // level up for level 1
        if (dA < 40) {
            hitA = true;
        }
        if (dB < 40) {
            hitB = true;
        }
        if (dC < 40) {
            hitC = true;
        }

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
        g.setFont(new Font("Times New Roman", Font.BOLD, 30)); // Font
        g.drawString("Instructions: trace the images without leaving the line! ", 140, 40); // Instructions

        g.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // Font
        g.drawString("If your cursor moves off the shape, the game restarts", 275, 70); // Instructions

        g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        g.drawString("Level " + level, 50, 650);
        // you win text on screen
        if (youWin) { // you win text on screen
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("You Win! ", 250, 250);
        }

        //draw the images and shape color if level = 1
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

        // draws connected path so player can see the trail
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

    // So you can move mouse across screen
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();


    }
    // so you can drag the mouse across the screen
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    // click mouse
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
        // hitting corners
        hitA = false;
        hitB = false;
        hitC = false;
        hitD = false;

        // coordinates
       // System.out.println(mouseX);
       // System.out.println(mouseY);

    }

    @Override
    public void mouseReleased(MouseEvent e) { // mouseReleased
        // if mouse is released, drawing resets
        drawing = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) { //mouseEntered

    }

    @Override
    public void mouseExited(MouseEvent e) { // mouseExited

    }

    @Override
    public void keyTyped(KeyEvent e) { // key typed

    }

    @Override
    public void keyPressed(KeyEvent e) { // key pressed method
        System.out.println(e.getKeyCode());
        int key = e.getKeyCode();
        // keys pressed
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
    public void keyReleased(KeyEvent e) { // key released method
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
