import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> trackList;
    private JButton button = new JButton("Приветики");

    public UI() {
        setTitle("MaxPlayer");
        setSize(500, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        listModel = new DefaultListModel<>();
        trackList = new JList<>(listModel);
        add(new JScrollPane(trackList), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JButton chooseFolderButton = new JButton("Выбрать папку");
        topPanel.add(chooseFolderButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel controls = new JPanel();
        JButton playButton = new JButton("▶ PLay");
        JButton stopButton = new JButton("◼ Stop");
        controls.add(playButton);
        controls.add(stopButton);
        add(controls, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        UI app = new UI();
        app.setVisible(true);
    }

}