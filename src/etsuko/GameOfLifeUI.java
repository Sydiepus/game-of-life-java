package etsuko;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import java.awt.Color;
//import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOfLifeUI extends JPanel implements ActionListener {
        private JLabel alive_color_label;
        private JButton alive_color_picker;
        private JLabel dead_color_label;
        private JButton dead_color_picker;
        private PixelPanel canvas;
        private static boolean running = false;

        GameOfLifeUI(PixelPanel canvas) {
                super();
                this.canvas = canvas;
                setSize(100, 150);
                setMaximumSize(new Dimension(100, 150));
                // setBackground(Color.BLACK);
                GroupLayout layout = new GroupLayout(this);
                setLayout(layout);
                setVisible(true);
                // addMouseListener(this);
                // addMouseMotionListener(this);
                // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                alive_color_label = new JLabel("Alive color : ");
                alive_color_picker = new JButton();
                alive_color_picker.setToolTipText("Alive cells");
                alive_color_picker.setPreferredSize(new Dimension(20, 20));
                alive_color_picker.setBackground(canvas.getSelectedColor());
                dead_color_label = new JLabel("Dead color : ");
                dead_color_picker = new JButton();
                dead_color_picker.setToolTipText("Dead cells");
                dead_color_picker.setPreferredSize(new Dimension(20, 20));
                dead_color_picker.setBackground(canvas.getBack());
                alive_color_picker.addActionListener(this);
                dead_color_picker.addActionListener(this);
                JButton startButton = new JButton("Start");
                startButton.addActionListener(
                                new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                                running = true;
                                        }
                                });
                JButton stopButton = new JButton("Stop");
                stopButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                running = false;
                        }
                });
                layout.setAutoCreateGaps(true);
                layout.setHorizontalGroup(
                                layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(alive_color_label)
                                                                .addComponent(dead_color_label)
                                                                .addComponent(startButton))
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
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                                .addComponent(startButton)
                                                                .addComponent(stopButton)));

        }

        public void actionPerformed(ActionEvent a) {
                JButton pressed_button = (JButton) a.getSource();
                Color color = JColorChooser.showDialog(this,
                                "Select the color for " + pressed_button.getToolTipText().toLowerCase(),
                                pressed_button.getBackground());
                pressed_button.setBackground(color);
                if (pressed_button.getToolTipText().contains("Alive")) {
                        canvas.setSelectedColor(color);
                } else {
                        canvas.setBack(color);
                }
        };

        public boolean isRunning() {
                return running;
        }
}
