package etsuko;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
//import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.util.Random;

public class PixelCanvas extends JFrame implements MouseInputListener {
    // private static final Random r = new Random();
    private int pixel_size;
    private Dimension canvas_size;
    private static Pixel[][] pixels;
    private int old_pixel_x = -1;
    private int old_pixel_y = -1;

    PixelCanvas(int width, int height) {
        super("Pixel Canvas");
        canvas_size = new Dimension(width, height);
        pixels = new Pixel[width][height];
        setMinimumSize(new Dimension(width * 10, height * 10));
        setBackground(Color.BLACK);
        // setForeground(Color.BLACK);
        setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pixel_size = calcpixel_size();
        initPixels();
    }

    private void initPixels() {
        for (int x = 0; x < canvas_size.width; x++) {
            for (int y = 0; y < canvas_size.height; y++) {
                pixels[x][y] = new Pixel(x, y, getBackground());
            }
        }
    }

    public Dimension getCanvasSize() {
        return canvas_size;
    }

    private int calcpixel_size() {
        return Math.min(getWidth(), getHeight()) / Math.min(canvas_size.width, canvas_size.height);
    }

    private int calcpixel_size(int width, int height) {
        return Math.min(width, height) / Math.min(canvas_size.width, canvas_size.height);
    }

    private int calcpixel_size(Dimension d) {
        return Math.min(d.width, d.height) / Math.min(canvas_size.width, canvas_size.height);
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

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        pixel_size = calcpixel_size(width, height);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        pixel_size = calcpixel_size(d);
    }

    private int normalize_x(int x) throws IndexOutOfBoundsException {
        int norm_x = x / pixel_size;
        if (norm_x >= canvas_size.width) {
            throw new IndexOutOfBoundsException("x out of bounds");
        }
        return norm_x;
    }

    private int normalize_y(int y) throws IndexOutOfBoundsException {
        int norm_y = y / pixel_size;
        if (norm_y >= canvas_size.height) {
            throw new IndexOutOfBoundsException("y out of bounds");
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
            Pixel pixel = new Pixel(pixel_x, pixel_y, Color.WHITE);// new Color(r.nextInt()));
            g.setColor(pixel.getColor());
            g.fillRect(pixel_x * pixel_size, pixel_y * pixel_size, pixel_size, pixel_size);
            addPixel(pixel);
            System.out.println(getPixel(pixel_x, pixel_y));
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        g.dispose();
    }

    private void drawPixel(Pixel pixel, Graphics g) {
        g.setColor(pixel.getColor());
        g.fillRect(pixel.getX() * pixel_size, pixel.getY() * pixel_size, pixel_size, pixel_size);
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
        drawPixel(e.getX(), e.getY());
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse entered");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse exited");
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released");
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
                Pixel old_pixel = getPixel(old_pixel_x, old_pixel_y);
                g.setColor(old_pixel.getColor());
                g.fillRect(old_pixel.getX() * pixel_size, old_pixel.getY() * pixel_size, pixel_size, pixel_size);
                g.setColor(Color.WHITE);
                g.fillRect(pixel_x * pixel_size, pixel_y * pixel_size, pixel_size, pixel_size);
                g.dispose();
            }
            old_pixel_x = pixel_x;
            old_pixel_y = pixel_y;
        } catch (IndexOutOfBoundsException ex) {
            return;
        }
    }

    public static void main(String[] args) throws Exception {
        PixelCanvas canvas = new PixelCanvas(4, 4);
        // canvas.setSize(canvas.getWidth()*20, canvas.getHeight()*20);
        // ColorPicker cPicker = new ColorPicker();
        // canvas.add(cPicker, BorderLayout.EAST);
        // System.out.println(cPicker.getWidth());
        // System.out.println(cPicker.getHeight());

        // System.out.println(canvas.getComponent(0));
        // JColorChooser choos = new JColorChooser();
        // canvas.add(choos, BorderLayout.PAGE_END);
        // System.out.println("AAA" +choos.getColor().toString());
    }
}