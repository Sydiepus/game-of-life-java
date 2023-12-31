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
        private JFormattedTextField scaleField;
        private static boolean running = false;
        private static boolean editable = false;
        private static boolean shouldStop = false;
        private static boolean gridEnabled;
        private static boolean reset = false;
        private static boolean resize = false;
        private static boolean etsukoModeActivated = false;

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
                alive_color_picker.setToolTipText("Alive cells, to delete hold control down.");
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
                a[0] = "Select pattern";
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
                JLabel scaleLabel = new JLabel("Scale : ");
                scaleField = new JFormattedTextField(number);
                scaleField.setValue(canvas.getScale());
                JCheckBox etsukoMode = new JCheckBox("Etsuko mode", etsukoModeActivated);

                etsukoMode.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                                etsukoModeActivated = !etsukoModeActivated;
                                int width = 142;
                                int height = 80;
                                // int height = 86;
                                if (etsukoModeActivated) {
                                        widthField.setValue(width);
                                        widthField.repaint();
                                        widthField.setEnabled(false);
                                        heightField.setValue(height);
                                        heightField.repaint();
                                        heightField.setEnabled(false);
                                        canvas.changeWidth(width);
                                        canvas.changeHeight(height);
                                        resize = true;
                                        scaleField.setValue(9);
                                        canvas.setScale(9);
                                        scaleField.setEnabled(false);
                                        // change Alive color to c8ded2
                                        Color etsukoAlive = new Color(200, 222, 210);
                                        Color etsukoDead = new Color(0, 3, 7);
                                        Color etsukoGrid = new Color(63, 104, 106);
                                        alive_color_picker.setBackground(etsukoAlive);
                                        alive_color_picker.setEnabled(false);
                                        canvas.setSelectedColor(etsukoAlive);
                                        dead_color_picker.setBackground(etsukoDead);
                                        dead_color_picker.setEnabled(false);
                                        canvas.setBack(etsukoDead);
                                        enableGrid.setSelected(true);
                                        enableGrid.setEnabled(false);
                                        gridColorButton.setBackground(etsukoGrid);
                                        canvas.setGridColor(etsukoGrid);
                                        gridColorButton.setEnabled(false);
                                        resetButton.setEnabled(false);
                                        allowEdits.setSelected(false);
                                        editable = false;
                                        allowEdits.setEnabled(false);
                                        preDefinedPatternsBox.setEnabled(false);
                                        // GGTCTTCCCCAT{8}TCTTCTGGAGGA{2}TCTTCTTTGGGT{1}
                                        // TCTTCTGGAGGCGGTCTTCCCCATGGTCTTCCCCATCTTCTTCTTCTT
                                        // GGT{4}ATTCTTCTTCTCGGTGGTCCCACT GGTCTTCCCCAT{5}
                                        // offset
                                        int[] offset = new int[] { 5, 5 };
                                        int[] padding = new int[] { 4, 5, 2, 3 };
                                        canvas.drawPatternOfPatterns("G G T C T T C C C C A T",
                                                        GameOfLife.predefinedPatterns, offset, true, 8, 1, padding);
                                        canvas.drawPatternOfPatterns("T C T T C T G G A G G A",
                                                        GameOfLife.predefinedPatterns, offset, true, 2, 1, padding);
                                        canvas.drawPatternOfPatterns("T C T T C T T T G G G T",
                                                        GameOfLife.predefinedPatterns, offset, true, 1, 1, padding);
                                        canvas.drawPatternOfPatterns(
                                                        "T C T T C T G G A G G C G G T C T T C C C C A T G G T C T T C C C C A T C T T C T T C T T C T T",
                                                        GameOfLife.predefinedPatterns, offset, true, 1, 1, padding);
                                        canvas.drawPatternOfPatterns("G G T", GameOfLife.predefinedPatterns, offset,
                                                        true, 4, 1, padding);
                                        canvas.drawPatternOfPatterns("A T T C T T C T T C T C G G T G G T C C C A C T",
                                                        GameOfLife.predefinedPatterns, offset, true, 1, 1, padding);
                                        canvas.drawPatternOfPatterns("G G T C T T C C C C A T",
                                                        GameOfLife.predefinedPatterns, offset, true, 5, 1, padding);

                                } else {
                                        widthField.setEnabled(true);
                                        heightField.setEnabled(true);
                                        scaleField.setEnabled(true);
                                        alive_color_picker.setEnabled(true);
                                        dead_color_picker.setEnabled(true);
                                        enableGrid.setEnabled(true);
                                        gridColorButton.setEnabled(true);
                                        resetButton.setEnabled(true);
                                        allowEdits.setEnabled(true);
                                        preDefinedPatternsBox.setEnabled(true);

                                }
                        }
                });

                scaleField.addPropertyChangeListener("value", new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                                repaint();
                                int scale = ((Number) scaleField.getValue()).intValue();
                                canvas.setScale(scale);
                                resize = true;
                        }
                });

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
                                        if (patternToAdd.isEmpty()) {
                                                preDefinedPatternsBox.setSelectedIndex(0);
                                                return;
                                        }
                                        int[] bound = findBoundaries(patternToAdd);
                                        transformPattern(patternToAdd, bound);
                                } else {
                                        ArrayList<int[]> pattern = GameOfLife.predefinedPatterns.get(selectedItem);
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
                                if (!etsukoModeActivated) {
                                        stopOnDead.setEnabled(true);
                                        allowEdits.setEnabled(true);
                                        resetButton.setEnabled(true);
                                }
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
                                                                .addComponent(scaleLabel)
                                                                .addComponent(alive_color_label)
                                                                .addComponent(dead_color_label)
                                                                .addComponent(enableGrid)
                                                                .addComponent(gridLabel)
                                                                .addComponent(startButton)
                                                                .addComponent(iterationsLabel)
                                                                .addComponent(allowEdits)
                                                                .addComponent(preDefinedPatternsBox)
                                                                .addComponent(etsukoMode)

                                                )
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(widthField,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(scaleField,
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
                                                                .addComponent(scaleLabel)
                                                                .addComponent(scaleField,
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
                                                .addComponent(etsukoMode)

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
                // lowX, lowY, deltaX, deltaY
                // transform the Pattern into a rectangle with pixels relative to the center of
                // the rectangle.
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
                for (Pixel pixel : pixels) {
                        int newPixelX = (pixel.getX() - measurements[0]) - (measurements[2] / 2);
                        int newPixelY = (pixel.getY() - measurements[1]) - (measurements[3] / 2);
                        System.out.println("add(new int[] { " + newPixelX + "," + newPixelY + "});");
                }
                System.out.println(
                                "Copy these into GameOfLife predefined patterns following the syntax already available.");
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

        public boolean resized() {
                boolean ret = resize;
                if (resize) {
                        resize = false;
                }
                return ret;
        }

        public void changeScaleField(int scale) {
                scaleField.setValue(scale);
        }
}
