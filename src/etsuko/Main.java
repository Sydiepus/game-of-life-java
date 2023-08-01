package etsuko;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setTitle("Game of life");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        PixelPanel canvas = new PixelPanel(40, 40);
        GameOfLifeUI gameUI = new GameOfLifeUI(canvas);
        canvas.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int scale = canvas.getScale();
                if (e.getWheelRotation() < 0) {
                    // Up
                    canvas.setScale(scale + 1);
                } else {
                    // Down
                    canvas.setScale(scale - 1);
                }
                frame.pack();
                gameUI.changeScaleField(scale);
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        frame.add(gameUI, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        frame.add(canvas, c);
        GameOfLife game = null;
        Boolean firstCall = true;
        frame.pack();
        frame.setVisible(true);
        while (true) {
            if (gameUI.shouldReset()) {
                if (game != null) {
                    game.resetIterations();
                    game = null;
                }
                canvas.clean();
                gameUI.resetDone();
            }
            if (gameUI.isRunning()) {
                if (game == null || (gameUI.isEditable() && firstCall) || canvas.deletedPixels()) {
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
                if (gameUI.resized()) {
                    frame.pack();
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
