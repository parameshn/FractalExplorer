import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
import java.util.*;


public class FractalExplorer extends JFrame {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    Canvas canvas;
    BufferedImage fractalImage;


    public FractalExplorer() {
        setInitialGUIProperties();
        addCanvas();
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