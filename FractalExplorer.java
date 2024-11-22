import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
import java.util.*;


public class FractalExplorer extends JFrame {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    static final int MAX_ITER = 200;

    Canvas canvas;
    BufferedImage fractalImage;


    public FractalExplorer() {
        setInitialGUIProperties();
        addCanvas();
    }

    private int computeIterations(double c_r, double c_i) {
        /*
         *let c = c_r + c_i
         let z  = z_r + z_i
        
         z' = z*z + c
            = (z_r + z_i)(z_r + z_i) + c_r + c_i
            = z_r^2 + 2*z_r*z_i - z_i^2 + c_r + c_i
        
            z_r' = z_r^2 - z_i^2 + c_r
            z_i' = 2*z_i*z_r + c_i
        
            
            
         */

        double z_r = 0.0;
        double z_i = 0.0;

        int iterCount = 0;

        //sq(a^2 + b^2) <= 2
        // a^2 + b^2 <= 4 since sq is compute heavy

        while (z_r * z_i + z_r * z_i <= 4.0) {
            double z_r_tmp = z_r;

            z_r = z_r * z_r - z_i * z_i + c_i;
            z_i = 2 * z_r_tmp * z_i + c_i;

            // point was inside MandleBort set
            if (iterCount >= MAX_ITER) {
                return MAX_ITER;
            }
            iterCount++;
        }

        //Complex point was outside MandleBort set
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

    private class Canvas extends JPanel
    {
       // @Override
       public Dimension getPreferredSize() {
           return new Dimension(WIDTH, HEIGHT);

       }

       public void paintComponent(Graphics drawingObj) {
           drawingObj.drawImage(fractalImage, 0, 0, null);
        }
        
    }

    public static void main(String [] args)
    {
        new FractalExplorer();
    }

}