//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// Graphics Libraries
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.MouseMotionListener;
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
public class BasicGameApp implements Runnable, MouseMotionListener, MouseListener {

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;


    //Variable Definition Section
    Image background;
    int mouseX = 0;
    int mouseY = 0;
    // location of triangle
    int[] x = {300, 500, 700};
    int[] y = {550, 200, 550};
    boolean playing = false;
    int level = 1; // shows level
    int corner = 0;
    double tolerance = 20; // // how close mouse must be to line
    ArrayList<Point> trail = new ArrayList<>();
    boolean drawing = false;


    //You can set their initial values too

    // Like Mario mario = new Mario(); //


    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor
        setUpGraphics();

        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);

        //variable and objects
        background = Toolkit.getDefaultToolkit().getImage("grey.jpg");
        //create (construct) the objects needed for the game


    }
    // end BasicGameApp constructor

    public void moveThings() {
        playing = true;
        // corners of triangle
        int ax = x[0], ay = y[0];
        int bx = x[1], by = y[1];
        int cx = x[2], cy = y[2];

        double dA = Math.hypot(mouseX - ax, mouseY - ay);
        double dB = Math.hypot(mouseX - bx, mouseY - by);
        double dC = Math.hypot(mouseX - cx, mouseY - cy);


        // game over
        if (mouseX < 250 || mouseX > 750 || mouseY < 150 || mouseY > 600) {
            playing = false;
            corner = 0;
            trail.clear();
            return;
        }
        if (drawing) {
            trail.add(new Point(mouseX, mouseY));
        }
        // level up
        if (corner == 0 && dB < 30) corner = 1;
        else if (corner == 1 && dC  < 30) corner = 2;
        else if (corner == 2 && dA < 30)  {
            level++;
            playing = false;
            corner = 0;
            trail.clear();
        }
    }


    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);

        // Instructions and Text
        g.setColor(Color.black);
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.drawString("Instructions: trace the images without leaving the line!", 250, 40);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        g.drawString("If your cursor moves off the shape, the game restarts", 275, 70);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        g.drawString("Level " + level, 50, 650);


        //draw the images
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(15));
        g.drawPolygon(x, y, 3);
        g.setStroke(new BasicStroke(1));
        // Signature: drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)

        // drawing
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(4));
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
        drawing = true;
        mouseX = e.getX();
        mouseY = e.getY();
        trail.add(new Point(mouseX, mouseY));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawing = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}






//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
