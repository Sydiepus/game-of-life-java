import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Pixelpanel extends JPanel {
    private static final Random r = new Random();
    private int pixel_size;
    private Dimension canvas_size;
    private static ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>();
    private JFrame frame;

    Pixelpanel(int width, int height, JFrame frame) {
        super();
        canvas_size = new Dimension(width, height);
        setBackground(Color.RED);
        setMinimumSize(new Dimension(width * 100, height * 100));
        // setForeground(Color.BLACK);
        setLayout(new GridLayout(width, height));
        setVisible(true);
        this.frame = frame;
        pixel_size = calcpixel_size();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        pixel_size = calcpixel_size();
    }

    private int calcpixel_size() {
        return Math.min(frame.getWidth(), frame.getHeight()) / Math.min(canvas_size.width, canvas_size.height);
    }

    private int calcpixel_size(int width, int height) {
        return Math.min(width, height) / Math.min(canvas_size.width, canvas_size.height);
    }

    private int calcpixel_size(Dimension d) {
        return Math.min(d.width, d.height) / Math.min(canvas_size.width, canvas_size.height);
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

    public class Pixel extends JComponent implements MouseInputListener {
        private int x;
        private int y;
        private Color color;

        public Pixel(int x, int y, Color color) {
            super();
            setVisible(true);
            // setBackground(Color.BLACK);
            addMouseListener(this);
            // setSize(pixel_size, pixel_size);
            //setBounds(x * pixel_size, y * pixel_size, pixel_size, pixel_size);
            setSize(pixel_size, pixel_size);
            setLocation(x * pixel_size, y * pixel_size);
            this.x = x;
            this.y = y;
            this.color = color;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(color);
            g.fillRect(0, 0, pixel_size, pixel_size);
        }

        public void mouseClicked(MouseEvent e) {
            System.out.println("Mouse clicked");
            color = new Color(r.nextInt());
            repaint();
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
            System.out.println("x: " + e.getX() + " y: " + e.getY());
        }
    }

    public static void main() {
        JFrame frame = new JFrame("Pixelpanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        Pixelpanel panel = new Pixelpanel(4, 4, frame);
        frame.add(panel);
        panel.add(panel.new Pixel(0, 0, new Color(r.nextInt())));
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
