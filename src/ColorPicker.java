import javax.swing.JColorChooser;
import javax.swing.JPanel;
//import javax.swing.event.MouseInputListener;

import java.awt.Button;
import java.awt.Color;
//import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.Graphics;

import java.util.Random;

public class ColorPicker extends JPanel {
    ColorPicker() {
        super();
        setBackground(Color.BLACK);
        setVisible(true);
        // addMouseListener(this);
        // addMouseMotionListener(this);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Button("Test"));
    }

}
