package etsuko;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import java.awt.Canvas;

public class TT {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pixel Canvas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setBackground(Color.BLACK);
        Canvas canvas = new Canvas();
        MouseListener l = new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("mouseClicked");
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
            }
        };
        canvas.setBackground(Color.BLACK);
        canvas.setVisible(true);
        canvas.addMouseListener(l);
        frame.add(canvas);        
    }
}
