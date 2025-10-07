package Max;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> trackList;
    private JLabel curTrack;

    public UI() {
        setTitle("MaxPlayer");
        setSize(500, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        listModel = new DefaultListModel<>();
        trackList = new JList<>(listModel);
        add(new JScrollPane(trackList), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton chooseFolderButton = new JButton("Выбрать папку");
        topPanel.add(chooseFolderButton, BorderLayout.WEST);

        curTrack = new JLabel("Текущая песня: не выбрано");
        curTrack.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(curTrack, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel controls = new JPanel();
        JButton prevButton = new JButton("⏮ Prev");
        JButton playButton = new JButton("▶ PLay");
        JButton stopButton = new JButton("◼ Stop");
        JButton skipButton = new JButton("Skip ⏭");

        controls.add(prevButton);
        controls.add(playButton);
        controls.add(stopButton);
        controls.add(skipButton);
        add(controls, BorderLayout.SOUTH);

        trackList.addListSelectionListener(e ->
                curTrack.setText("Текущая песня: " + trackList.getSelectedValue())
        );

        listModel.addElement("Song 1");
        listModel.addElement("Song 2");
        listModel.addElement("Song 3");
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            UI player = new UI();
            player.setVisible(true);
        });
    }
}