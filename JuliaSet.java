import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class JuliaSet extends JFrame {
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

    public JuliaSet() {
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

                int iterCount = computeIterations(c_r, c_i);

                int pixelColor = makeColor(iterCount);
                fractalImage.setRGB(x, y, pixelColor);
            }
        }
        canvas.repaint();
    }

    
    private int makeColor(int iterCount) {
        if (iterCount == MAX_ITER) {
            return Color.BLACK.getRGB(); // Points inside the set are black
        }

        // Generate a gradient using the iteration count
        int red = (iterCount * 9) % 256; // Modulate red intensity
        int green = (iterCount * 7) % 256; // Modulate green intensity
        int blue = (iterCount * 3) % 256; // Modulate blue intensity

        // Combine RGB components into a single color
        return new Color(red, green, blue).getRGB();
    }

    private int computeIterations(double z_r, double z_i) {
        /*
         * For Julia sets:
         * z' = z^2 + c
         * where z starts as the complex point being iterated
         * and c is a constant complex number.
         */

        // Define the constant c for the Julia set
        double c_r = -0.7; // Example real part of c
        double c_i = 0.27015; // Example imaginary part of c

        int iterCount = 0;

        // Escape condition: |z|^2 <= 4
        while (z_r * z_r + z_i * z_i <= 4.0) {
            double z_r_tmp = z_r;

            // Julia set iteration formula: z' = z^2 + c
            z_r = z_r * z_r - z_i * z_i + c_r;
            z_i = 2 * z_r_tmp * z_i + c_i;

            // Check if maximum iterations are reached
            if (iterCount >= MAX_ITER) {
                return MAX_ITER; // Point remains bounded
            }
            iterCount++;
        }

        return iterCount; // Point escapes
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
                //Left
                case MouseEvent.BUTTON1:
                    adjustZoom(x, y, zoomFactor * 2);
                    break;
                //right
                case MouseEvent.BUTTON3:
                    adjustZoom(x, y, zoomFactor / 2);
                    break;

            }
        }

    }

    public static void main(String[] args) {
        new JuliaSet();
    }
}
