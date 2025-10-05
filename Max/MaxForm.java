package Max;

import javax.swing.*;
import java.awt.*;

public class MaxForm extends JFrame {
    public MaxForm() {
        super("Max Form");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(500, 500);
        setLayout(new BorderLayout());

//         Видео-компонент (например, из JFXPanel для JavaFX)
//        Component videoScreen ;
//        add(videoScreen, BorderLayout.CENTER);

        // Панель управления
        JPanel controls = new JPanel(new FlowLayout());
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Pause");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        add(slider, BorderLayout.SOUTH);
        add(controls, BorderLayout.WEST);
        controls.add(playButton);
        controls.add(stopButton);
        slider.addChangeListener(e -> {});
        ImageIcon icon = new ImageIcon("./items/gif.gif");
        JLabel label = new JLabel(icon);
        controls.add(label);
    }
}
