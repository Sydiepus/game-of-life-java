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
        GameOfLifeUI gameUI = new GameOfLifeUI(canvas);
        frame.add(gameUI, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.CENTER);
        GameOfLife game = null;
        Boolean firstCall = true;
        while (true) {
            if (gameUI.isRunning()) {
                if (game == null || gameUI.isEditable()) {
                    game = new GameOfLife(canvas);
                }
                if (firstCall) {
                    canvas.removeMouseListener(canvas);
                    canvas.removeMouseMotionListener(canvas);
                    firstCall = false;
                }
                game.applyRules(gameUI.getIterationsLabel());
                canvas.repaint();
                if (game.canStop() && gameUI.shouldStop()) {
                    gameUI.stop();
                    game.resetIterations();
                }
            } else {
                if (!firstCall && gameUI.isEditable()) {
                    canvas.addMouseListener(canvas);
                    canvas.addMouseMotionListener(canvas);
                    firstCall = true;
                } else if (firstCall && !gameUI.isEditable()) {
                    canvas.removeMouseListener(canvas);
                    canvas.removeMouseMotionListener(canvas);
                    firstCall = false;
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
