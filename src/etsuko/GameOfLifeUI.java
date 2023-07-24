package etsuko;

import javax.swing.JColorChooser;
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
import java.awt.event.ItemEvent;

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

        /*
         * Die Hard
         * ...........-.
         * .....--......
         * ......-...---
         */

        /*
         * center of canvas
         * canvas.getCanvasSize().width /2
         * canvas.getCanvasSize().height /2
         * 
         */
        private static boolean gridEnabled;

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
                JButton startButton = new JButton("Start");
                startButton.addActionListener(
                                new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                                start();
                                                stopOnDead.setEnabled(false);
                                                allowEdits.setEnabled(false);
                                        }
                                });
                JButton stopButton = new JButton("Stop");
                stopButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                stop();
                                stopOnDead.setEnabled(true);
                                allowEdits.setEnabled(true);

                        }
                });
                iterationsLabel = new JLabel("Iterations : 0");
                layout.setAutoCreateGaps(true);
                layout.setHorizontalGroup(
                                layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(alive_color_label)
                                                                .addComponent(dead_color_label)
                                                                .addComponent(startButton))
                                                .addComponent(enableGrid)
                                                .addComponent(gridLabel)
                                                .addComponent(gridColorButton,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                .addComponent(status)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(alive_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(dead_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(stopButton))
                                                .addComponent(allowEdits)
                                                .addComponent(stopOnDead)
                                                .addComponent(iterationsLabel)

                );
                layout.setVerticalGroup(
                                layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(alive_color_label)
                                                                .addComponent(alive_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(dead_color_label)
                                                                .addComponent(dead_color_picker,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addComponent(enableGrid)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(gridLabel)
                                                                .addComponent(gridColorButton,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                .addComponent(status)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(startButton)
                                                                .addComponent(stopButton))
                                                .addComponent(allowEdits)
                                                .addComponent(stopOnDead)
                                                .addComponent(iterationsLabel));

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

        public boolean isRunning() {
                return running;
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
