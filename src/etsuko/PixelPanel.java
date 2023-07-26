package etsuko;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

public class PixelPanel extends JPanel implements MouseInputListener {
    private int pixel_size;
    private Dimension canvasSize;
    private static Pixel[][] pixels;
    private Color back = Color.BLACK;
    private Color selected_color = Color.WHITE;
    private static int oldPixelX = 0;
    private static int oldPixelY = 0;
    private static Pixel oldPixel = null;
    private static boolean pixelGrid = false;
    private Color gridColor = Color.GRAY;
    private ArrayList<Pixel> selectedPattern = null;
    private boolean highlight = false;

    PixelPanel(int width, int height) {
        super();
        pixels = new Pixel[width][height];
        canvasSize = new Dimension(width, height);
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
        for (int x = 0; x < canvasSize.width; x++) {
            for (int y = 0; y < canvasSize.height; y++) {
                pixels[x][y] = new Pixel(x, y, back);
            }
        }
    }

    private void changeColor(Color color, Color oldColor) {
        for (int x = 0; x < canvasSize.width; x++) {
            for (int y = 0; y < canvasSize.height; y++) {
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
        clear(g);
        // Draw the pixels.
        for (int x = 0; x < canvasSize.width; x++) {
            for (int y = 0; y < canvasSize.height; y++) {
                drawPixel(pixels[x][y], g);
            }
        }
        g.dispose();
    }

    public void clear(Graphics g) {
        // Clear the frame.
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void clear() {
        Graphics g = getGraphics();
        clear(g);
        g.dispose();
    }

    public void clean() {
        clear();
        initPixels();
        oldPixelX = 0;
        oldPixelY = 0;
        oldPixel = null;
        selectedPattern = null;
        repaint();
    }

    public Dimension getCanvasSize() {
        return canvasSize;
    }

    public ArrayList<Pixel> getPaintedPixels() {
        ArrayList<Pixel> paintedPixels = new ArrayList<Pixel>();
        for (int x = 0; x < canvasSize.width; x++) {
            for (int y = 0; y < canvasSize.height; y++) {
                Pixel pixel = getPixel(x, y);
                if (!pixel.getColor().equals(getBack())) {
                    paintedPixels.add(pixel);
                }
            }
        }
        return paintedPixels;
    }

    private int normalize_x(int x) throws OutOfBoundsException {
        int norm_x = x / pixel_size;
        if (norm_x >= canvasSize.width || norm_x < 0) {
            throw new OutOfBoundsException("x out of bounds");
        }
        return norm_x;
    }

    private int normalize_y(int y) throws OutOfBoundsException {
        int norm_y = y / pixel_size;
        if (norm_y >= canvasSize.height || norm_y < 0) {
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

    private void handlePattern(int pixel_x, int pixel_y) {
        for (Pixel pixel : selectedPattern) {
            int tmpX = pixel.getX() + pixel_x;
            int tmpY = pixel.getY() + pixel_y;
            drawPixel(tmpX, tmpY);
        }
    }

    private void handleOldPattern() {
        for (Pixel pixel : selectedPattern) {
            try {
                Pixel oldPixel = getPixel(pixel.getX() + oldPixelX, pixel.getY() + oldPixelY);
                drawPixel(oldPixel, getGraphics());
            } catch (IndexOutOfBoundsException ex) {
            }
        }
    }

    private void handleOldPixel() {
        oldPixel = getPixel(oldPixelX, oldPixelY);
        drawPixel(oldPixel, getGraphics());
    }

    private void handleOldStuff() {
        handleOldPixel();
        if (selectedPattern != null) {
            handleOldPattern();
        }
    }

    private void drawPixel(int pixelX, int pixelY) {
        Graphics g = getGraphics();
        try {
            Pixel pixel = getPixel(pixelX, pixelY);
            if (!highlight) {
                pixel.setColor(selected_color);
            }
            drawPixel(pixel, selected_color, g);
        } catch (IndexOutOfBoundsException ex) {
        }
        g.dispose();
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

    public void drawPatternOfPatterns(String patternOfPattern, HashMap<String, ArrayList<Pixel>> patterns, int[] offset,
            boolean wrap, int repeat, int spacing) {
        // patternOfPattern is a string containing the pattern to be drawn in order
        // separated by spaces, a pattern should be available in patterns else it i'll
        // be considered like a space.
        // a space is a ... space between the current and the next pattern.
        // The ArrayList should contain the pixels of the pattern that should be
        // painted, the coordinates of the pixels should be relative to
        // a fictional center as in the center of the smallest rectangle that contains
        // all the pixels
        // offset are the coordinates of the center that you want, e.g if a pattern is
        // 5x5 wide you'ld want an offset of +5, +5 so the pattern would be painted
        // correctly.
        // wrap is used to determine if the pattern should bet painted on the next
        // "line" if it doesn't fit.
        // repeat number of times to repeat pattern, negative means infinite, aka fill
        // the whole canvas.
        // spacing is how much pixels to leave between the patterns
        highlight = false;
        int centerX = offset[0] / 2;
        int centerY = offset[1] / 2;
        int i = 0;
        while (i < repeat | repeat < 0) {
            for (String pattern : patternOfPattern.split(" ")) {
                if (centerX > canvasSize.width && wrap) {
                    centerX = offset[0] / 2;
                    centerY += spacing + offset[1];
                }
                if (centerY > canvasSize.height) {
                    selectedPattern = null;
                    return;
                }
                selectedPattern = patterns.get(pattern);
                if (selectedPattern != null) {
                    System.out.println("Drawing : " + pattern);
                    handlePattern(centerX, centerY);
                }
                centerX += spacing + offset[0];
            }
            i++;
        }
        selectedPattern = null;
    }

    public void changeWidth(int width) {
        int oldWidth = canvasSize.width;
        canvasSize.setSize(width, canvasSize.height);
        if (width == oldWidth) {
            return;
        } else if (width > oldWidth) {
            Pixel[][] newPixels = new Pixel[width][canvasSize.height];
            for (int i = 0; i < oldWidth; i++) {
                System.arraycopy(pixels[i], 0, newPixels[i], 0, pixels.length);
            }
            for (int i = oldWidth-1; i < width; i++) {
                for (int y = 0; y < canvasSize.height; y++) {
                    newPixels[i][y] = new Pixel(i, y, back);
                }
            }
            pixels = newPixels;
        } else {
            Pixel[][] newPixels = new Pixel[width][canvasSize.height];
            pixels = newPixels;
            initPixels();
        }
        oldPixelX = 0;
        oldPixelY = 0;
        oldPixel = null;
        selectedPattern = null;
        calculatePixelSize();
        repaint();
        //setSize(width*10, canvasSize.height*10);
    }

    public void changeHeight(int height) {
        int oldHeight = canvasSize.height;
        canvasSize.setSize(canvasSize.width, height);
        if (height == oldHeight) {
            return;
        } else if (height > oldHeight) {
            Pixel[][] newPixels = new Pixel[canvasSize.width][height];
            for (int i = 0; i < canvasSize.width; i++) {
                System.arraycopy(pixels[i], 0, newPixels[i], 0, pixels[i].length);
                for (int y = oldHeight-1; y < height; y++) {
                    newPixels[i][y] = new Pixel(i, y, back);
                }
            }
            pixels = newPixels;
        } else {
            Pixel[][] newPixels = new Pixel[canvasSize.width][height];
            pixels = newPixels;
            initPixels();
        }
        oldPixelX = 0;
        oldPixelY = 0;
        oldPixel = null;
        selectedPattern = null;
        calculatePixelSize();
        repaint();
    }

    private int calculatePixelSize() {
        return Math.min(getWidth(), getHeight()) / Math.min(canvasSize.width, canvasSize.height);
    }

    public void setSelectedPattern(ArrayList<Pixel> selectedPattern) {
        this.selectedPattern = selectedPattern;
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

    private void handleClickEvent(MouseEvent e) {
        highlight = false;
        try {
            int pixelX = normalize_x(e.getX());
            int pixelY = normalize_y(e.getY());
            if (selectedPattern != null) {
                handlePattern(pixelX, pixelY);
            } else {
                drawPixel(pixelX, pixelY);
            }
        } catch (OutOfBoundsException ex) {
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        handleClickEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        handleOldStuff();
        oldPixelX = oldPixelY = 0;
    }

    public void mouseDragged(MouseEvent e) {
        handleClickEvent(e);
    }

    public void mouseMoved(MouseEvent e) {
        highlight = true;
        Graphics g = getGraphics();
        try {
            int pixelX = normalize_x(e.getX());
            int pixelY = normalize_y(e.getY());
            if (pixelX == oldPixelX && pixelY == oldPixelY) {
                return;
            }
            handleOldStuff();
            if (selectedPattern != null) {
                handlePattern(pixelX, pixelY);
            } else {
                drawPixel(pixelX, pixelY);
            }
            oldPixelX = pixelX;
            oldPixelY = pixelY;
        } catch (OutOfBoundsException ex) {
            handleOldStuff();
            oldPixelX = oldPixelY = 0;
        } finally {
            g.dispose();
        }
    }

    class OutOfBoundsException extends Exception {
        public OutOfBoundsException(String error) {
            super(error);
        }
    }
}
