package etsuko;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PixelCanvas canvas = new PixelCanvas(50, 50);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Edit the canvas and press enter to start the game");
        scanner.nextLine();
        GameOfLife game = new GameOfLife(canvas);
        while (true) {
            canvas.removeMouseListener(canvas);
            canvas.removeMouseMotionListener(canvas);
            game.apllyRules();
            canvas.repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
