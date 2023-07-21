package etsuko;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setTitle("Game of life");
        frame.setVisible(true);
        PixelPanel canvas = new PixelPanel(40, 40);
        GameOfLifeUI cp = new GameOfLifeUI(canvas);
        frame.add(cp, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.CENTER);
        GameOfLife game = null;
        Boolean firstCall = true;
        while (true) {
            if (cp.isRunning()) {
                if (game == null) {
                    game = new GameOfLife(canvas);
                }
                if (firstCall) {
                    canvas.removeMouseListener(canvas);
                    canvas.removeMouseMotionListener(canvas);
                    firstCall = false;
                }
                game.apllyRules();
                canvas.repaint();
            } else {
                if (!firstCall) {
                    canvas.addMouseListener(canvas);
                    canvas.addMouseMotionListener(canvas);
                    firstCall = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
