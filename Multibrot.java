import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.*;


public class Multibrot extends JFrame {
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
    public Multibrot()
    {
        setInitialGUIProperties();
        addCanvas();
        updateFractal();
        canvas.addKeyStrokeEvents();
        canvas.requestFocusInWindow();

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

                int iterCount = computeMultibrotIterations(c_r, c_i, 3); // Cubic Multibrot
;

                int pixelColor = makeColor(iterCount);
                fractalImage.setRGB(x, y, pixelColor);
            }
        }
        canvas.repaint();
    }

    // private int makeColor(int iterCount) {
    // int color = 0b011011100001110101101110;
    // int mask = 0b000000000000011111111111;
    // int shiftMag = iterCount / 13;

    // if (iterCount == MAX_ITER)
    // return Color.BLACK.getRGB();
    // return color | (mask << shiftMag);
    // }
    private int makeColor(int iterCount) {
        if (iterCount == MAX_ITER) {
            return Color.BLACK.getRGB(); // Points inside the set are black
        }

        // Map the iteration count to a grayscale value (0–255)
        int gray = (int) (255.0 * iterCount / MAX_ITER);

        // Create a grayscale color
        return new Color(gray, gray, gray).getRGB();
    }

    private int computeMultibrotIterations(double c_r, double c_i, int degree) {
        /*
         * Multibrot sets use the formula:
         * z' = z^d + c
         * z = z_r + z_i
         * 
         * To calculate:
         * z_r' = (z_r^2 - z_i^2)^d + c_r
         * z_i' = (2 * z_r * z_i)^d + c_i
         */

        double z_r = 0.0; // Real part of z
        double z_i = 0.0; // Imaginary part of z

        int iterCount = 0;

        while ((z_r * z_r + z_i * z_i) <= 4.0) {
          //  double z_r_tmp = z_r;

            // Apply the general polynomial degree
            double r = Math.pow(Math.sqrt(z_r * z_r + z_i * z_i), degree);
            double theta = Math.atan2(z_i, z_r) * degree;

            z_r = r * Math.cos(theta) + c_r;
            z_i = r * Math.sin(theta) + c_i;

            if (iterCount >= MAX_ITER) {
                return MAX_ITER; // Point is inside the set
            }

            iterCount++;
        }

        return iterCount; // Point is outside the set
    }

    private void moveUp() {
        double curHeight = HEIGHT / zoomFactor;
        topLeftY += curHeight / 6;
        updateFractal();
    }

    private void moveDown() {
        double curHeight = HEIGHT / zoomFactor;
        topLeftY -= curHeight / 6;
        updateFractal();
    }

    // private void moveLeft() {
    // double curHeight = WIDTH / zoomFactor;
    // topLeftY -= curHeight / 6;
    // updateFractal();
    // }

    // private void moveRight() {
    // double curHeight = WIDTH / zoomFactor;
    // topLeftY += curHeight / 6;
    // updateFractal();
    // }
    private void moveLeft() {
        double curWidth = WIDTH / zoomFactor; // Use width for horizontal movement
        topLeftX -= curWidth / 6; // Move left by a fraction of the width
        updateFractal();
    }

    private void moveRight() {
        double curWidth = WIDTH / zoomFactor; // Use width for horizontal movement
        topLeftX += curWidth / 6; // Move right by a fraction of the width
        updateFractal();
    }

    private void addCanvas() {
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

    private class Canvas extends JPanel implements MouseListener {

        public Canvas() {
            addMouseListener(this);
            setFocusable(true);

        }

        // @Override
        public Dimension getPreferredSize() {
            return new Dimension(WIDTH, HEIGHT);

        }

        public void addKeyStrokeEvents() {
            KeyStroke wKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
            KeyStroke aKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
            KeyStroke sKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
            KeyStroke dKey = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0);

            Action wPressed = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveUp();
                }
            };
            Action aPressed = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLeft();
                }
            };
            Action sPressed = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveDown();
                }
            };
            Action dPressed = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveRight();
                }
            };

            this.getInputMap().put(wKey, "w_key");
            this.getInputMap().put(aKey, "a_key");
            this.getInputMap().put(sKey, "s_key");
            this.getInputMap().put(dKey, "d_key");

            this.getActionMap().put("w_key", wPressed);
            this.getActionMap().put("a_key", aPressed);
            this.getActionMap().put("s_key", sPressed);
            this.getActionMap().put("d_key", dPressed);

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
                // Left
                case MouseEvent.BUTTON1:
                    adjustZoom(x, y, zoomFactor * 2);
                    break;
                // right
                case MouseEvent.BUTTON3:
                    adjustZoom(x, y, zoomFactor / 2);
                    break;

            }
        }

    }

    public static void main(String[] args)
    {

        new Multibrot();
    }
}
