package etsuko;

import java.util.ArrayList;

public class GameOfLife {
    private ArrayList<Pixel> alive_cells;
    private PixelPanel canvas;
    private int Iteration = 0;
    private static int canvas_width;
    private static int canvas_height;

    GameOfLife(PixelPanel canvas) {
        this.canvas = canvas;
        canvas_width = canvas.getCanvasSize().width;
        canvas_height = canvas.getCanvasSize().height;
        initAliveCells();
    }

    private void initAliveCells() {
        alive_cells = new ArrayList<Pixel>();
        for (int x = 0; x < canvas_width; x++) {
            for (int y = 0; y < canvas_height; y++) {
                Pixel pixel = canvas.getPixel(x, y);
                if (pixel.getColor() != canvas.getBack()) {
                    alive_cells.add(pixel);
                }
            }
        }

    }

    public void apllyRules() {
        ArrayList<Pixel> new_alive_cells = new ArrayList<Pixel>();
        for (int x = 0; x < canvas_width; x++) {
            for (int y = 0; y < canvas_height; y++) {
                Pixel cell = canvas.getPixel(x, y);
                boolean is_alive = alive_cells.contains(cell);
                int alive_neighbours = getAliveNeighbours(cell);
                if (is_alive) {
                    // Any live cell with two or three live neighbours lives on to the next
                    // generation
                    if (alive_neighbours == 2 || alive_neighbours == 3) {
                        new_alive_cells.add(cell);
                    }
                    // Any live cell with fewer than two live neighbours dies, as if by
                    // underpopulation
                    // Any live cell with more than three live neighbours dies, as if by
                    // overpopulation.
                    else {
                        cell.setColor(canvas.getBack());
                    }
                } else {
                    // Any dead cell with exactly three live neighbours becomes a live cell, as if
                    // by reproduction.
                    if (alive_neighbours == 3) {
                        cell.setColor(canvas.getSelectedColor());
                        new_alive_cells.add(cell);

                    }
                }

            }
        }
        alive_cells = new_alive_cells;
        Iteration++;
    }

    private int getAliveNeighbours(Pixel cell) {
        int alive_neighbours = 0;
        for (Pixel alive_cell : alive_cells) {
            if (alive_cell != cell) {
                if (isNeighbour(cell, alive_cell)) {
                    alive_neighbours++;
                }
            }
        }
        return alive_neighbours;
    }

    private boolean isNeighbour(Pixel cell1, Pixel cell2) {
        int x1 = cell1.getX();
        int y1 = cell1.getY();
        int x2 = cell2.getX();
        int y2 = cell2.getY();
        return (x1 == x2 || x1 == x2 - 1 || x1 == x2 + 1) && (y1 == y2 || y1 == y2 - 1 || y1 == y2 + 1);
    }
}
