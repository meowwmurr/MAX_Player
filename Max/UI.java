package Max;

import Max.collections.MyArrayList;
import Max.collections.MyList;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class UI extends JFrame {
    private final MyList<String> dirSongs = new MyArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> trackList = new JList<>(listModel);
    private Clip currentClip;

    public UI() {
        setTitle("MaxPlayer");
        setSize(500, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JScrollPane(trackList), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JButton chooseFolderButton = new JButton("Выбрать папку");
        topPanel.add(chooseFolderButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel controls = new JPanel();
        JButton prevButton = new JButton("⏮ Prev");
        JButton playButton = new JButton("▶ Play");
        JButton stopButton = new JButton("◼ Stop");
        JButton skipButton = new JButton("Skip ⏭");

        controls.add(prevButton);
        controls.add(playButton);
        controls.add(stopButton);
        controls.add(skipButton);
        add(controls, BorderLayout.SOUTH);

        chooseFolderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFolder = fileChooser.getSelectedFile();
                searchWavFiles(selectedFolder);
            }
        });

        playButton.addActionListener(e -> playSelectedTrack());
        stopButton.addActionListener(e -> stopPlayback());
        skipButton.addActionListener(e -> skipTrack());
        prevButton.addActionListener(e -> previousTrack());
    }

    private void searchWavFiles(File folder) {
        dirSongs.clear();
        listModel.clear();
        if (folder == null || !folder.isDirectory()) return;
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".wav")) {
                dirSongs.add(file.getAbsolutePath());
                listModel.addElement(file.getName());
            }
        }
        JOptionPane.showMessageDialog(
                this,
                "Найдено " + dirSongs.size() + " WAV-файлов",
                "Результат",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void playSelectedTrack() {
        int index = trackList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Выберите трек из списка!");
            return;
        }

        stopPlayback();

        try {
            File file = new File(dirSongs.get(index));
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            
            // Получаем формат аудио
            AudioFormat baseFormat = audioStream.getFormat();
            
            // Преобразуем в поддерживаемый формат, если необходимо
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            
            // Создаем поток с преобразованным форматом
            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            
            currentClip = AudioSystem.getClip();
            currentClip.open(decodedStream);
            currentClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace(); // Для отладки
            JOptionPane.showMessageDialog(this, "Ошибка при воспроизведении: " + ex.getMessage());
        }
    }

    private void stopPlayback() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
    }

    private void skipTrack() {
        int index = trackList.getSelectedIndex();
        if (index < listModel.size() - 1) {
            trackList.setSelectedIndex(index + 1);
            playSelectedTrack();
        }
    }

    private void previousTrack() {
        int index = trackList.getSelectedIndex();
        if (index > 0) {
            trackList.setSelectedIndex(index - 1);
            playSelectedTrack();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI().setVisible(true));
    }
}
