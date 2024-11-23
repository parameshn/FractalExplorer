import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import java.util.*;



public class FractalExplorer extends JFrame {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    static final int MAX_ITER = 200;

    static final double DEFAULT_ZOOM = 100.0;
    static final double DEFAULT_TOP_LEFT_X = -3.0;
    static final double DEFAULT_TOP_LEFT_Y = +3.0;


    double zoomFactor = DEFAULT_ZOOM;
    double topLeftX = DEFAULT_TOP_LEFT_X;
    double topLeftY = DEFAULT_TOP_LEFT_Y;

    

    Canvas canvas;
    BufferedImage fractalImage;


    public FractalExplorer() {
        setInitialGUIProperties();
        addCanvas();
        updateFractal();
    }
    
    private double getXPos(double x) {
        return x / zoomFactor + topLeftX;
    }

    private double getYPos(double y) {
        return y / zoomFactor - topLeftY;
    }

    public void updateFractal() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double c_r = getXPos(x);
                double c_i = getYPos(y);

                int iterCount = computeIterations(c_r, c_i);

                int pixelColor = makeColor(iterCount);
                fractalImage.setRGB(x, y, pixelColor);
            }
        }
        canvas.repaint();
    }
    
    private int makeColor(int iterCount) {
        if (iterCount == MAX_ITER) {
            return Color.BLACK.getRGB();
        }
        return Color.BLUE.getRGB();
    }

    

    private int computeIterations(double c_r, double c_i) {
        /*
         * let c = c_r + c_i
         * let z = z_r + z_i
         * 
         * z' = z*z + c
         * = (z_r + z_i)(z_r + z_i) + c_r + c_i
         * = z_r^2 + 2*z_r*z_i - z_i^2 + c_r + c_i
         * 
         * z_r' = z_r^2 - z_i^2 + c_r
         * z_i' = 2*z_i*z_r + c_i
         * 
         * 
         * 
         */

        double z_r = 0.0;
        double z_i = 0.0;

        int iterCount = 0;

        // sq(a^2 + b^2) <= 2
        // a^2 + b^2 <= 4 since sq is compute heavy

        while (z_r * z_i + z_r * z_i <= 4.0) {
            double z_r_tmp = z_r;

            z_r = z_r * z_r - z_i * z_i + c_r;
            z_i = 2 * z_r_tmp * z_i + c_i;

            // point was inside MandleBort set
            if (iterCount >= MAX_ITER) {
                return MAX_ITER;
            }
            iterCount++;
        }

        // Complex point was outside MandleBort set
        return iterCount;

    }
    

   
    

    private void  addCanvas(){
        canvas = new Canvas();
        fractalImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
        canvas.setVisible(true);
        this.add(canvas, BorderLayout.CENTER);

    }

    public void setInitialGUIProperties() {
        this.setTitle("Fractal Explorer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void adjustZoom(double newX, double newY, double newZoomFactor) {
        topLeftX += newX / zoomFactor;
        topLeftY -= newY / zoomFactor;

        zoomFactor = newZoomFactor;

        topLeftX -= (WIDTH / 2) / zoomFactor;
        topLeftY += (HEIGHT / 2) / zoomFactor;

        updateFractal();


    }

    private class Canvas extends JPanel implements MouseListener
    {

        public Canvas()
        {
            addMouseListener(this);
        }
       // @Override
       public Dimension getPreferredSize() {
           return new Dimension(WIDTH, HEIGHT);

       }

       public void paintComponent(Graphics drawingObj) {
           drawingObj.drawImage(fractalImage, 0, 0, null);
       }
        
       @Override
       public void mouseReleased(MouseEvent mouse) {
       }
        
       @Override
       public void mouseClicked(MouseEvent mouse) {
       }
       
       @Override
       public void mouseEntered(MouseEvent mouse) {
       }
       
       @Override
       public void mouseExited(MouseEvent mouse) {
       }
       
       public void mousePressed(MouseEvent mouse) {

           double x = (double) mouse.getX();
           double y = (double) mouse.getY();

           switch (mouse.getButton()) {
               //Left
               case MouseEvent.BUTTON1:
                  adjustZoom(x, y, zoomFactor * 2);
                   break;
                   //right
                case MouseEvent.BUTTON3:
                 adjustZoom(x, y, zoomFactor /2);
                 break;
           
           }
       }
       
      
    }

    public static void main(String [] args)
    {
        new FractalExplorer();
    }

}