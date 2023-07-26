package etsuko;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Set;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;

public class GameOfLifeUI extends JPanel implements ActionListener {
        private static Dimension colorButtonDimension = new Dimension(20, 20);
        private JLabel alive_color_label;
        private JButton alive_color_picker;
        private JLabel dead_color_label;
        private JButton dead_color_picker;
        private PixelPanel canvas;
        private static boolean running = false;
        private static boolean editable = false;
        private static boolean shouldStop = false;
        private static boolean gridEnabled;
        private static boolean reset = false;
        public static boolean resize = false;

        private JLabel iterationsLabel;
        private JLabel status;

        GameOfLifeUI(PixelPanel canvas) {
                super();
                this.canvas = canvas;
                setSize(100, 150);
                setMaximumSize(new Dimension(100, 150));
                GroupLayout layout = new GroupLayout(this);
                setLayout(layout);
                setVisible(true);
                alive_color_label = new JLabel("Alive color : ");
                alive_color_picker = new JButton();
                alive_color_picker.setToolTipText("Alive cells");
                alive_color_picker.setPreferredSize(colorButtonDimension);
                alive_color_picker.setBackground(canvas.getSelectedColor());
                dead_color_label = new JLabel("Dead color : ");
                dead_color_picker = new JButton();
                dead_color_picker.setToolTipText("Dead cells");
                dead_color_picker.setPreferredSize(colorButtonDimension);
                dead_color_picker.setBackground(canvas.getBack());
                alive_color_picker.addActionListener(this);
                dead_color_picker.addActionListener(this);
                JCheckBox allowEdits = new JCheckBox("Enable edit", editable);
                gridEnabled = canvas.isGridEnabled();
                JCheckBox enableGrid = new JCheckBox("Enable grid", gridEnabled);
                JLabel gridLabel = new JLabel("Grid color : ");
                gridLabel.setVisible(gridEnabled);
                JButton gridColorButton = new JButton();
                gridColorButton.setToolTipText("Grid");
                gridColorButton.setPreferredSize(colorButtonDimension);
                gridColorButton.setBackground(canvas.getGridColor());
                gridColorButton.setVisible(gridEnabled);
                gridColorButton.addActionListener(this);
                status = new JLabel("Not started");
                JCheckBox stopOnDead = new JCheckBox("Stop when possible", shouldStop);
                stopOnDead.setToolTipText("Stop when there is no cells alive.");
                Set<String> b = GameOfLife.predefinedPatterns.keySet();
                String[] a = new String[b.size() + 2];
                a[0] = "None";
                a[b.size() + 1] = "Add pattern";
                System.arraycopy(b.toArray(), 0, a, 1, b.size());
                JComboBox<String> preDefinedPatternsBox = new JComboBox<>(a);
                iterationsLabel = new JLabel("Iterations : 0");
                JButton startButton = new JButton("Start");
                JButton resetButton = new JButton("Reset");
                JButton stopButton = new JButton("Stop");
                JLabel canvasSizeLabel = new JLabel("canvas size : ");
                NumberFormat number = NumberFormat.getNumberInstance();
                JFormattedTextField widthField = new JFormattedTextField(number);
                widthField.setValue(canvas.getCanvasSize().width);
                widthField.setToolTipText("Width");
                JFormattedTextField heightField = new JFormattedTextField(number);
                heightField.setValue(canvas.getCanvasSize().height);
                heightField.setToolTipText("Width");

                widthField.addPropertyChangeListener("value", new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                                repaint();
                                int width = ((Number) widthField.getValue()).intValue();
                                canvas.changeWidth(width);
                                resize = true;
                        }
                });
                heightField.addPropertyChangeListener("value", new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                                repaint();
                                int height = ((Number) heightField.getValue()).intValue();
                                canvas.changeHeight(height);
                                resize = true;
                        }
                });

                preDefinedPatternsBox.addActionListener(new ActionListener() {
                        @SuppressWarnings("unchecked")
                        public void actionPerformed(ActionEvent e) {
                                JComboBox<String> box = (JComboBox<String>) e.getSource();
                                String selectedItem = (String) box.getSelectedItem();
                                if (selectedItem.equals("Add pattern")) {
                                        ArrayList<Pixel> patternToAdd = canvas.getPaintedPixels();
                                        System.out.println(patternToAdd);
                                        int[] bound = findBoundaries(patternToAdd);
                                        transformPattern(patternToAdd, bound);
                                } else {
                                        ArrayList<Pixel> pattern = GameOfLife.predefinedPatterns.get(selectedItem);
                                        canvas.setSelectedPattern(pattern);
                                }
                        }
                });

                stopOnDead.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                                shouldStop = !shouldStop;
                        }
                });
                enableGrid.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                                gridEnabled = !gridEnabled;
                                canvas.setEnableGrid(gridEnabled);
                                gridLabel.setVisible(gridEnabled);
                                gridColorButton.setVisible(gridEnabled);

                        }
                });
                allowEdits.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                                editable = !editable;
                        }
                });
                startButton.addActionListener(
                                new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                                start();
                                                stopOnDead.setEnabled(false);
                                                allowEdits.setEnabled(false);
                                                resetButton.setEnabled(false);
                                        }
                                });
                stopButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                stop();
                                stopOnDead.setEnabled(true);
                                allowEdits.setEnabled(true);
                                resetButton.setEnabled(true);
                        }
                });
                resetButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                status.setText("Not stated");
                                iterationsLabel.setText("Iterations : 0");
                                preDefinedPatternsBox.setSelectedIndex(0);
                                reset = true;
                        }
                });
                layout.setAutoCreateGaps(true);
                layout.setHorizontalGroup(
                                layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(canvasSizeLabel)
                                                                .addComponent(alive_color_label)
                                                                .addComponent(dead_color_label)
                                                                .addComponent(enableGrid)
                                                                .addComponent(gridLabel)
                                                                .addComponent(startButton)
                                                                .addComponent(iterationsLabel)
                                                                .addComponent(allowEdits)
                                                                .addComponent(preDefinedPatternsBox)

                                                )
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(widthField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(alive_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(dead_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(gridColorButton,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(resetButton)
                                                                .addComponent(stopOnDead)
                                                                .addComponent(status))
                                                .addComponent(heightField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(stopButton))

                );
                layout.setVerticalGroup(
                                layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(canvasSizeLabel)
                                                                .addComponent(widthField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(heightField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(alive_color_label)
                                                                .addComponent(alive_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(dead_color_label)
                                                                .addComponent(dead_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(enableGrid))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(gridLabel)
                                                                .addComponent(gridColorButton,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(status)
                                                                .addComponent(iterationsLabel))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(startButton)
                                                                .addComponent(resetButton)
                                                                .addComponent(stopButton))

                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(allowEdits)
                                                                .addComponent(stopOnDead))
                                                .addComponent(preDefinedPatternsBox,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)

                );

        }

        public void actionPerformed(ActionEvent a) {
                JButton pressed_button = (JButton) a.getSource();
                Color color = JColorChooser.showDialog(this,
                                "Select the color for " + pressed_button.getToolTipText().toLowerCase(),
                                pressed_button.getBackground());
                pressed_button.setBackground(color);
                if (pressed_button.getToolTipText().contains("Alive")) {
                        canvas.setSelectedColor(color);
                } else if (pressed_button.getToolTipText().contains("Grid")) {
                        canvas.setGridColor(color);
                } else {
                        canvas.setBack(color);
                }
        };

        private int[] findBoundaries(ArrayList<Pixel> pixels) {
                Pixel pix = pixels.get(0);
                int highX, highY, lowX, lowY;
                highX = lowX = pix.getX();
                highY = lowY = pix.getY();
                for (Pixel pixel : pixels) {
                        int tmpX = pixel.getX();
                        int tmpY = pixel.getY();
                        if (tmpX > highX) {
                                highX = tmpX;
                        }
                        if (tmpY > highY) {
                                highY = tmpY;
                        }
                        if (tmpX < lowX) {
                                lowX = tmpX;
                        }
                        if (tmpY < lowY) {
                                lowY = tmpY;
                        }
                }
                int[] a = { lowX, lowY, (highX - lowX), (highY - lowY) };
                return a;
        }

        private void transformPattern(ArrayList<Pixel> pixels, int[] measurements) {
                // lowX, lowY, deltaX, deltaY
                // transform the Pattern into a rectangle with pixels relative to the center of
                // the rectangle.
                ArrayList<Pixel> transPixels = new ArrayList<Pixel>();
                System.out.println("Center X: " + (measurements[2]) / 2 + " Y: " + (measurements[3]) / 2);
                for (Pixel pixel : pixels) {
                        int newPixelX = (pixel.getX() - measurements[0]) - (measurements[2] / 2);
                        int newPixelY = (pixel.getY() - measurements[1]) - (measurements[3] / 2);
                        transPixels.add(new Pixel(newPixelX, newPixelY, Color.WHITE));
                        System.out.println("add(new Pixel(" + newPixelX + "," + newPixelY + "," + " Color.WHITE));");
                        /*
                         * Die Hard
                         * (-3, 0)
                         * (-2, 0)
                         * (-2, 1)
                         * (2, 1)
                         * (3, -1)
                         * (3, 1)
                         * (4, 1)
                         */
                        // TODO: Complete this.

                }
        }

        public boolean isRunning() {
                return running;
        }

        public boolean shouldReset() {
                return reset;
        }

        public void resetDone() {
                reset = false;
        }

        public boolean isEditable() {
                return editable;
        }

        public JLabel getIterationsLabel() {
                return iterationsLabel;
        }

        public boolean shouldStop() {
                return shouldStop;
        }

        public void stop() {
                running = false;
                status.setText("Paused");
        }

        public void start() {
                running = true;
                status.setText("Running");
        }
}
