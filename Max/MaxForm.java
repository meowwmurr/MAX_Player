package Max;

import javax.swing.*;
import java.awt.*;

public class MaxForm extends JFrame {
    public MaxForm() {
        super("Max Player");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(600, 500);
        setLayout(new BorderLayout(10, 10));

        // Главная панель с отступами
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Видео область (центр)
        ImageIcon icon = new ImageIcon("./items/gif.gif");
        JLabel videoLabel = new JLabel(icon);
        videoLabel.setHorizontalAlignment(JLabel.CENTER);
        videoLabel.setBorder(BorderFactory.createTitledBorder("Видео"));
        mainPanel.add(videoLabel, BorderLayout.CENTER);

        // Панель управления (низ)
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Боковая панель (опционально)
        JPanel sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        add(mainPanel);

        // Центрируем окно
        setLocationRelativeTo(null);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Управление"));

        // Кнопки управления
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton playButton = new JButton("▶ Play");
        JButton pauseButton = new JButton("⏸ Pause");
        JButton stopButton = new JButton("⏹ Stop");

        // Устанавливаем предпочтительный размер для кнопок
        Dimension buttonSize = new Dimension(80, 30);
        playButton.setPreferredSize(buttonSize);
        pauseButton.setPreferredSize(buttonSize);
        stopButton.setPreferredSize(buttonSize);

        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);

        // Слайдер громкости
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        volumePanel.add(new JLabel("🔊"));
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 80);
        volumeSlider.setPreferredSize(new Dimension(100, 40));
        volumePanel.add(volumeSlider);

        // Слайдер прогресса
        JSlider progressSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        progressSlider.setPreferredSize(new Dimension(400, 40));

        // Компоновка элементов управления
        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(progressSlider, BorderLayout.CENTER);
        controlPanel.add(volumePanel, BorderLayout.SOUTH);

        return controlPanel;
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBorder(BorderFactory.createTitledBorder("Плейлист"));
        sidePanel.setPreferredSize(new Dimension(150, 0));

        // Модель плейлиста
        DefaultListModel<String> playlistModel = new DefaultListModel<>();
        playlistModel.addElement("Трек 1");
        playlistModel.addElement("Трек 2");
        playlistModel.addElement("Трек 3");
        playlistModel.addElement("Трек 4");

        JList<String> playlist = new JList<>(playlistModel);
        playlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Кнопки управления плейлистом
        JPanel playlistControls = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton addButton = new JButton("Добавить");
        JButton removeButton = new JButton("Удалить");

        playlistControls.add(addButton);
        playlistControls.add(removeButton);

        sidePanel.add(new JScrollPane(playlist), BorderLayout.CENTER);
        sidePanel.add(playlistControls, BorderLayout.SOUTH);

        return sidePanel;
    }

    public static void main(String[] args) {
        // Устанавливаем системный Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new MaxForm().setVisible(true);
        });
    }
}