package etsuko;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
//import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;

import java.util.Random;

public class ColorPicker extends JPanel {
    ColorPicker() {
        super();
        setBackground(Color.BLACK);
        setVisible(true);
        setLayout(new GridLayout());
        // addMouseListener(this);
        // addMouseMotionListener(this);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JButton("Test"));
    }

}
