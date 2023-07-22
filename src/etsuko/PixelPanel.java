package etsuko;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

public class PixelPanel extends JPanel implements MouseInputListener {
    private int pixel_size;
    private Dimension canvas_size;
    private static Pixel[][] pixels;
    private Color back = Color.BLACK;
    private Color selected_color = Color.WHITE;
    private static int old_pixel_x = 0;
    private static int old_pixel_y = 0;
    private static Pixel old_pixel = null;

    PixelPanel(int width, int height) {
        super();
        pixels = new Pixel[width][height];
        canvas_size = new Dimension(width, height);
        initPixels();
        old_pixel = pixels[0][0];
        setMinimumSize(new Dimension(width * 10, height * 10));
        setMaximumSize(new Dimension(width * 10, height * 10));
        setPreferredSize(new Dimension(width * 10, height * 10));

        // setForeground(Color.BLACK);
        // setLayout(new GridLayout(width, height));
        setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        pixel_size = calcpixel_size();
    }

    private void initPixels() {
        for (int x = 0; x < canvas_size.width; x++) {
            for (int y = 0; y < canvas_size.height; y++) {
                pixels[x][y] = new Pixel(x, y, back);
            }
        }
    }

    private void changeColor(Color color, Color oldColor) {
        for (int x = 0; x < canvas_size.width; x++) {
            for (int y = 0; y < canvas_size.height; y++) {
                Pixel pixel = pixels[x][y];
                if (pixel.getColor() == oldColor) {
                    pixel.setColor(color);
                }
            }
        }
    }

    public void setBack(Color color) {
        if ((back != null) && !back.equals(color)) {
            changeColor(color, back);
            back = color;
            repaint();
        }
    }

    public Color getBack() {
        return back;
    }

    @Override
    public void paint(Graphics g) {
        pixel_size = calcpixel_size();
        // Clear the frame.
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        // g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the pixels.
        System.out.println("pxels: " + pixels.toString());
        for (int x = 0; x < canvas_size.width; x++) {
            for (int y = 0; y < canvas_size.height; y++) {
                drawPixel(pixels[x][y], g);
            }
        }
        g.dispose();
    }

    public Dimension getCanvasSize() {
        return canvas_size;
    }

    private int normalize_x(int x) throws OutOfBoundsException {
        int norm_x = x / pixel_size;
        if (norm_x >= canvas_size.width) {
            throw new OutOfBoundsException("x out of bounds");
        }
        return norm_x;
    }

    private int normalize_y(int y) throws OutOfBoundsException {
        int norm_y = y / pixel_size;
        if (norm_y >= canvas_size.height) {
            throw new OutOfBoundsException("y out of bounds");
        }
        return norm_y;
    }

    public Pixel getPixel(int x, int y) {
        // The canvas is split into a grid of pixels
        // each pixel has a size of pixel_size
        // This function returns the pixel number of the pixel at (x, y)
        // (x, y) is the coordinate of the mouse click
        // we need to check which pixel contains the mouse click
        // The pixel is square, so we can check if the mouse click is within the
        // boundaries of the pixel.
        return pixels[x][y];
    }

    public void addPixel(Pixel pixel) {
        // if (pixels.size() <= pixel.getX()) {
        // pixels.add(new ArrayList<Pixel>());
        // pixels.get(pixel.getX()).add(pixel);
        // } else if (pixels.get(pixel.getX()).size() <= pixel.getY()) {
        // pixels.get(pixel.getX()).add(pixel);
        // } else {
        // pixels.get(pixel.getX()).set(pixel.getY(), pixel);
        // }
        pixels[pixel.getX()][pixel.getY()] = pixel;
        // pixels[pixel.getX()][pixel.getY()] = pixel;
        // System.out.println("pixels: " + pixels.toString());
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    private void drawPixel(int x, int y) {
        Graphics g = getGraphics();
        try {
            int pixel_x = normalize_x(x);
            int pixel_y = normalize_y(y);
            Pixel pixel = getPixel(pixel_x, pixel_y);// new Pixel(pixel_x, pixel_y, selected_color);// new
                                                     // Color(r.nextInt()));
            pixel.setColor(selected_color);
            g.setColor(selected_color);
            g.fillRect(pixel_x * pixel_size, pixel_y * pixel_size, pixel_size, pixel_size);
            // addPixel(pixel);
        } catch (OutOfBoundsException e) {
            return;
        }
        g.dispose();
    }

    private void drawPixel(Pixel pixel, Graphics g) {
        g.setColor(pixel.getColor());
        g.fillRect(pixel.getX() * pixel_size, pixel.getY() * pixel_size, pixel_size, pixel_size);
    }

    private int calcpixel_size() {
        return Math.min(getWidth(), getHeight()) / Math.min(canvas_size.width, canvas_size.height);
    }

    public Color getSelectedColor() {
        return selected_color;
    }

    public void setSelectedColor(Color color) {
        changeColor(color, selected_color);
        selected_color = color;
        repaint();
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        System.out.println("mouseClicked");
        drawPixel(e.getX(), e.getY());

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        System.out.println("mousePressed");
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        System.out.println("mouseReleased");
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        System.out.println("mouseEntered");
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        System.out.println("mouseExited");
        old_pixel = getPixel(old_pixel_x, old_pixel_y);
        repaintOldPixel();
    }

    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse dragged");
    }

    public void mouseMoved(MouseEvent e) {
        System.out.println("Mouse moved");
        try {
            int pixel_x = normalize_x(e.getX());
            int pixel_y = normalize_y(e.getY());
            Graphics g = getGraphics();
            System.out.println("pixel_x: " + pixel_x + ", pixel_y: " + pixel_y);
            System.out.println("old_pixel_x: " + old_pixel_x + ", old_pixel_y: " + old_pixel_y);
            if ((pixel_x != old_pixel_x || pixel_y != old_pixel_y) && (old_pixel_x != -1 && old_pixel_y != -1)) {
                old_pixel = getPixel(old_pixel_x, old_pixel_y);
                g.setColor(old_pixel.getColor());
                g.fillRect(old_pixel.getX() * pixel_size, old_pixel.getY() * pixel_size, pixel_size, pixel_size);
                g.setColor(selected_color);
                g.fillRect(pixel_x * pixel_size, pixel_y * pixel_size, pixel_size, pixel_size);
                g.dispose();
                old_pixel_x = pixel_x;
                old_pixel_y = pixel_y;
            } else if ((old_pixel_x == pixel_x && old_pixel_y == pixel_y)
                    && (old_pixel = getPixel(old_pixel_x, old_pixel_y)).getColor().equals(back)) {
                repaintOldPixel(g, selected_color);
                g.dispose();

            }
        } catch (OutOfBoundsException ex) {
            old_pixel = getPixel(old_pixel_x, old_pixel_y);
            repaintOldPixel();
            return;
        }
    }

    public void repaintOldPixel(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(old_pixel.getX() * pixel_size, old_pixel.getY() * pixel_size, pixel_size, pixel_size);

    }

    public void repaintOldPixel() {
        Graphics g = getGraphics();
        repaintOldPixel(g, old_pixel.getColor());
        g.dispose();
    }

    class OutOfBoundsException extends Exception {
        public OutOfBoundsException(String error) {
            super(error);
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Pixelpanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        PixelPanel panel = new PixelPanel(4, 4);
        frame.add(panel, BorderLayout.CENTER);
        GameOfLifeUI cp = new GameOfLifeUI(panel);
        frame.add(cp, BorderLayout.WEST);
        System.out.println(UIManager.getColor("Panel.background"));
        // JColorChooser cp = new JColorChooser();
        // cp.addPropertyChangeListener(new PropertyChangeListener() {
        // @Override
        // public void Pro
        // });
        // frame.add(cp, BorderLayout.WEST);
        // System.out.println(cp.getSelectionModel().toString());
        // frame.add(new JButton("asdasdaAA"), BorderLayout.EAST);
        // Button to add color.
        // it's another button added to the pallet, when clicked it updates the
        // selected_color var that is used to highlight and paint pixels.
        // Action listener for button.

        // Pixelpanel color_picker = new Pixelpanel(6, 1);
        // frame.add(color_picker);
        // panel.add(panel.new Pixel(0, 0, new Color(r.nextInt())));
        // panel.add(panel.new Pixel(0, 1, new Color(r.nextInt())));

        // panel.setSize(500, 500);
        // panel.setLocation(0, 0);

        // for (int i = 0; i < 100; i++) {
        // pixels.add(new ArrayList<Pixel>());
        // for (int j = 0; j < 100; j++) {
        // pixels.get(i).add(panel.new Pixel(i, j, new Color(r.nextInt())));
        // panel.add(pixels.get(i).get(j));
        // }
        // }
    }
}
