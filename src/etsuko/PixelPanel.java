package etsuko;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

public class PixelPanel extends JPanel implements MouseInputListener {
    private int pixel_size;
    private Dimension canvas_size;
    private static Pixel[][] pixels;
    private Color back = Color.BLACK;
    private Color selected_color = Color.WHITE;
    private static int oldPixelX = 0;
    private static int oldPixelY = 0;
    private static Pixel oldPixel = null;
    private static boolean pixelGrid = false;
    private Color gridColor = Color.GRAY;

    PixelPanel(int width, int height) {
        super();
        pixels = new Pixel[width][height];
        canvas_size = new Dimension(width, height);
        initPixels();
        oldPixel = pixels[0][0];
        setMinimumSize(new Dimension(width * 10, height * 10));
        setMaximumSize(new Dimension(width * 10, height * 10));
        setPreferredSize(new Dimension(width * 10, height * 10));
        setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        pixel_size = calculatePixelSize();
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
        pixel_size = calculatePixelSize();
        // Clear the frame.
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        // Draw the pixels.
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
        if (norm_x >= canvas_size.width || norm_x < 0) {
            throw new OutOfBoundsException("x out of bounds");
        }
        return norm_x;
    }

    private int normalize_y(int y) throws OutOfBoundsException {
        int norm_y = y / pixel_size;
        if (norm_y >= canvas_size.height || norm_y < 0) {
            throw new OutOfBoundsException("y out of bounds");
        }
        return norm_y;
    }

    public Pixel getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void addPixel(Pixel pixel) {
        pixels[pixel.getX()][pixel.getY()] = pixel;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    private void drawPixel(int x, int y) {
        Graphics g = getGraphics();
        try {
            int pixel_x = normalize_x(x);
            int pixel_y = normalize_y(y);
            Pixel pixel = getPixel(pixel_x, pixel_y);
            pixel.setColor(selected_color);
            drawPixel(pixel, selected_color, g);
        } catch (OutOfBoundsException e) {
            return;
        } finally {
            g.dispose();
        }
    }

    private void drawPixel(Pixel pixel, Graphics g) {
        drawPixel(pixel, pixel.getColor(), g);
    }

    private void drawPixel(Pixel pixel, Color color, Graphics g) {
        g.setColor(color);
        g.fillRect(pixel.getX() * pixel_size, pixel.getY() * pixel_size, pixel_size, pixel_size);
        if (pixelGrid) {
            g.setColor(gridColor);
            g.drawRect(pixel.getX() * pixel_size, pixel.getY() * pixel_size, pixel_size, pixel_size);
        }
    }

    private int calculatePixelSize() {
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

    public void setGridColor(Color color) {
        gridColor = color;
        if (pixelGrid) {
            repaint();
        }
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setEnableGrid(boolean enable) {
        pixelGrid = enable;
        repaint();
    }

    public boolean isGridEnabled() {
        return pixelGrid;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        drawPixel(e.getX(), e.getY());

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        oldPixel = getPixel(oldPixelX, oldPixelY);
        drawPixel(oldPixel, getGraphics());
    }

    public void mouseDragged(MouseEvent e) {
        drawPixel(e.getX(), e.getY());

    }

    public void mouseMoved(MouseEvent e) {
        try {
            int pixelX = normalize_x(e.getX());
            int pixelY = normalize_y(e.getY());
            Graphics g = getGraphics();
            if ((pixelX != oldPixelX || pixelY != oldPixelY) && (oldPixelX != -1 && oldPixelY != -1)) {
                oldPixel = getPixel(oldPixelX, oldPixelY);
                drawPixel(oldPixel, g);
                Pixel selectedPixel = getPixel(pixelX, pixelY);
                drawPixel(selectedPixel, selected_color, g);
                g.dispose();
                oldPixelX = pixelX;
                oldPixelY = pixelY;
            } else if ((oldPixelX == pixelX && oldPixelY == pixelY)
                    && (oldPixel = getPixel(oldPixelX, oldPixelY)).getColor().equals(back)) {

                drawPixel(oldPixel, selected_color, g);
                g.dispose();

            }
        } catch (OutOfBoundsException ex) {
            oldPixel = getPixel(oldPixelX, oldPixelY);
            drawPixel(oldPixel, getGraphics());
            return;
        }
    }

    class OutOfBoundsException extends Exception {
        public OutOfBoundsException(String error) {
            super(error);
        }
    }
}
