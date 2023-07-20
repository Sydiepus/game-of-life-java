package etsuko;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

public class Pixelpanel extends JPanel {
    private static final Random r = new Random();
    private int pixel_size;
    private Dimension canvas_size;
    private static Pixel[][] pixels;
    private boolean first = false;

    Pixelpanel(int width, int height) {
        super();
        canvas_size = new Dimension(width, height);
        pixels = new Pixel[width][height];
        setBackground(Color.RED);
        setMinimumSize(new Dimension(width * 10, height * 10));
        // setForeground(Color.BLACK);
        // setLayout(new GridLayout(width, height));
        setVisible(true);
        // addMouseListener(this);
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

    @Override
    public void paint(Graphics g) {
        pixel_size = calcpixel_size();
        for (int x = 0; x < canvas_size.width; x++) {
            for (int y = 0; y < canvas_size.height; y++) {
                if (!first) {
                    add(pixels[x][y]);
                } else {
                    pixels[x][y].repaint();
                }
            }
        }
        // add(new Pixel(0, 0, new Color(r.nextInt())));
        super.paint(g);
        first = true;

    }

    private int calcpixel_size() {
        return Math.min(getWidth(), getHeight()) / Math.min(canvas_size.width, canvas_size.height);
    }

    public class Pixel extends JComponent implements MouseInputListener {
        private int pix_x;
        private int pix_y;
        private Color pix_color;
        private Color old_color;

        public Pixel(int norm_x, int norm_y, Color pix_color) {
            super();
            this.pix_x = norm_x;
            this.pix_y = norm_y;
            this.pix_color = pix_color;
            this.old_color = pix_color;
            setVisible(true);
            setMinimumSize(new Dimension(pixel_size, pixel_size));
            // setBackground(Color.BLACK);
            addMouseListener(this);
            // setSize(pixel_size, pixel_size);
            // setBounds(x * pixel_size, y * pixel_size, pixel_size, pixel_size);
            setSize(pixel_size, pixel_size);
            setLocation(pix_x * pixel_size, pix_y * pixel_size);
        }

        @Override
        public void paintComponent(Graphics g) {
            setSize(pixel_size, pixel_size);
            setLocation(pix_x * pixel_size, pix_y * pixel_size);
            super.paintComponent(g);
            g.setColor(pix_color);
            g.fillRect(0, 0, pixel_size, pixel_size);
            // g.setColor(Color.BLACK);
            // g.drawRect(0, 0, pixel_size, pixel_size);
        }

        // @Override
        // public void paint(Graphics g) {
        // setLocation(pix_x * pixel_size, pix_y * pixel_size);
        // super.paint(g);
        // g.setColor(color);
        // g.fillRect(0, 0, pixel_size, pixel_size);
        // // g.setColor(Color.BLACK);
        // // g.drawRect(0, 0, pixel_size, pixel_size);
        // }

        public void mouseClicked(MouseEvent e) {
            pix_color = new Color(r.nextInt());
            old_color = pix_color;
            repaint();
        }

        public void mouseEntered(MouseEvent e) {
            old_color = pix_color;
            pix_color = Color.BLACK;
            repaint();
        }

        public void mouseExited(MouseEvent e) {
            pix_color = old_color;
            repaint();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Pixelpanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        Pixelpanel panel = new Pixelpanel(4, 4);
        frame.add(panel);
        //Pixelpanel color_picker = new Pixelpanel(6, 1);
        //frame.add(color_picker);
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
