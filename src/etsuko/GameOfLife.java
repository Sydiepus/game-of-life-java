package etsuko;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;

public class GameOfLife {
    private ArrayList<Pixel> alive_cells;
    private PixelPanel canvas;
    private static long iteration = 0;
    private static int canvas_width;
    private static int canvas_height;
    private boolean canStop = false;
    public static final HashMap<String, ArrayList<int[]>> predefinedPatterns = new HashMap<String, ArrayList<int[]>>() {
        {
            put("Diehard", new ArrayList<int[]>() {
                {
                    add(new int[] { -3, 0 });
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 3, -1 });
                    add(new int[] { 3, 1 });
                    add(new int[] { 4, 1 });
                }
            });
            put("T", new ArrayList<int[]>() {
                {
                    add(new int[] { -2, -2 });
                    add(new int[] { -1, -2 });
                    add(new int[] { 0, -2 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 1, -2 });
                    add(new int[] { 2, -2 });
                }
            });
            put("C", new ArrayList<int[]>() {
                {
                    add(new int[] { -2, -1 });
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -1, -2 });
                    add(new int[] { -1, 2 });
                    add(new int[] { 0, -2 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 1, -2 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 2, -2 });
                    add(new int[] { 2, 2 });
                }
            });
            put("A", new ArrayList<int[]>() {
                {
                    add(new int[] { -2, -1 });
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -2, 2 });
                    add(new int[] { -1, -2 });
                    add(new int[] { -1, 0 });
                    add(new int[] { 0, -2 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 1, -2 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 2, -1 });
                    add(new int[] { 2, 0 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 2, 2 });
                }
            });
            put("G", new ArrayList<int[]>() {
                {
                    add(new int[] { -2, -1 });
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -1, -2 });
                    add(new int[] { -1, 2 });
                    add(new int[] { 0, -2 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 1, -2 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 2, -2 });
                    add(new int[] { 2, 0 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 2, 2 });
                }
            });
            put("Block", new ArrayList<int[]>() {
                {
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 1, 1 });
                }
            });

            put("Bee-hive ", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, 0 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, -1 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 2, 0 });
                }
            });
            put("Loaf", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, 0 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, -1 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 2, 0 });
                    add(new int[] { 2, 1 });
                }
            });
            put("Boat", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, -1 });
                    add(new int[] { -1, 0 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, 0 });
                }
            });
            put("Tub", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, 0 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, 0 });
                }
            });
            put("Blinker", new ArrayList<int[]>() {
                {
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                }
            });
            put("Toad", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, 1 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 2, 0 });
                }
            });
            put("Beacon", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, -1 });
                    add(new int[] { -1, 0 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 2, 2 });
                }
            });
            put("Pulsar", new ArrayList<int[]>() {
                {
                    add(new int[] { -6, -4 });
                    add(new int[] { -6, -3 });
                    add(new int[] { -6, -2 });
                    add(new int[] { -6, 2 });
                    add(new int[] { -6, 3 });
                    add(new int[] { -6, 4 });
                    add(new int[] { -4, -6 });
                    add(new int[] { -4, -1 });
                    add(new int[] { -4, 1 });
                    add(new int[] { -4, 6 });
                    add(new int[] { -3, -6 });
                    add(new int[] { -3, -1 });
                    add(new int[] { -3, 1 });
                    add(new int[] { -3, 6 });
                    add(new int[] { -2, -6 });
                    add(new int[] { -2, -1 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -2, 6 });
                    add(new int[] { -1, -4 });
                    add(new int[] { -1, -3 });
                    add(new int[] { -1, -2 });
                    add(new int[] { -1, 2 });
                    add(new int[] { -1, 3 });
                    add(new int[] { -1, 4 });
                    add(new int[] { 1, -4 });
                    add(new int[] { 1, -3 });
                    add(new int[] { 1, -2 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 1, 3 });
                    add(new int[] { 1, 4 });
                    add(new int[] { 2, -6 });
                    add(new int[] { 2, -1 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 2, 6 });
                    add(new int[] { 3, -6 });
                    add(new int[] { 3, -1 });
                    add(new int[] { 3, 1 });
                    add(new int[] { 3, 6 });
                    add(new int[] { 4, -6 });
                    add(new int[] { 4, -1 });
                    add(new int[] { 4, 1 });
                    add(new int[] { 4, 6 });
                    add(new int[] { 6, -4 });
                    add(new int[] { 6, -3 });
                    add(new int[] { 6, -2 });
                    add(new int[] { 6, 2 });
                    add(new int[] { 6, 3 });
                    add(new int[] { 6, 4 });
                }
            });
            put("Penta-decathlon", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, -3 });
                    add(new int[] { -1, -2 });
                    add(new int[] { -1, -1 });
                    add(new int[] { -1, 0 });
                    add(new int[] { -1, 1 });
                    add(new int[] { -1, 2 });
                    add(new int[] { -1, 3 });
                    add(new int[] { -1, 4 });
                    add(new int[] { 0, -3 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 0, 4 });
                    add(new int[] { 1, -3 });
                    add(new int[] { 1, -2 });
                    add(new int[] { 1, -1 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 1, 3 });
                    add(new int[] { 1, 4 });
                }
            });
            put("Glider", new ArrayList<int[]>() {
                {
                    add(new int[] { -1, 1 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 1, 1 });
                }
            });
            put("Light-weight spaceship (LWSS) ", new ArrayList<int[]>() {
                {
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -1, 0 });
                    add(new int[] { -1, 1 });
                    add(new int[] { -1, 2 });
                    add(new int[] { 0, -1 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 1, -1 });
                    add(new int[] { 1, 0 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 2, 0 });
                }
            });
            put("Middle-weight spaceship (MWSS) ", new ArrayList<int[]>() {
                {
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -1, 0 });
                    add(new int[] { -1, 1 });
                    add(new int[] { -1, 2 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 1, -1 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 2, -1 });
                    add(new int[] { 2, 0 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 3, 0 });
                }
            });
            put("Heavy-weight spaceship (HWSS)", new ArrayList<int[]>() {
                {
                    add(new int[] { -3, 0 });
                    add(new int[] { -3, 1 });
                    add(new int[] { -2, 0 });
                    add(new int[] { -2, 1 });
                    add(new int[] { -2, 2 });
                    add(new int[] { -1, 0 });
                    add(new int[] { -1, 1 });
                    add(new int[] { -1, 2 });
                    add(new int[] { 0, 0 });
                    add(new int[] { 0, 1 });
                    add(new int[] { 0, 2 });
                    add(new int[] { 1, -1 });
                    add(new int[] { 1, 1 });
                    add(new int[] { 1, 2 });
                    add(new int[] { 2, -1 });
                    add(new int[] { 2, 0 });
                    add(new int[] { 2, 1 });
                    add(new int[] { 3, 0 });
                }
            });

        }
    };

    GameOfLife(PixelPanel canvas) {
        this.canvas = canvas;
        canvas_width = canvas.getCanvasSize().width;
        canvas_height = canvas.getCanvasSize().height;
        initAliveCells();
        canStop = false;
    }

    private void initAliveCells() {
        alive_cells = canvas.getPaintedPixels();
    }

    public void applyRules() {
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
        iteration++;
        if (alive_cells.size() == 0) {
            canStop = true;
        }
    }

    public void applyRules(JLabel label) {
        applyRules();
        // get the last integer from the
        label.setText("Iterations: " + iteration);
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

    public boolean canStop() {
        return canStop;
    }

    public void resetIterations() {
        iteration = 0;
    }
}
