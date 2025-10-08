package Max.tests.e2e;

import Max.UI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.await;

public class PlayerE2ETest extends ApplicationTest {
    private UI ui;
    private Robot robot;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() throws Exception {
        ui = new UI();
        ui.setVisible(true);
        robot = new Robot();
        // Даем время для отрисовки UI
        robot.delay(500);
    }
    
    @AfterEach
    void tearDown() {
        if (ui != null) {
            ui.dispose();
        }
    }
    
    @Test
    void testFullUserJourney() throws Exception {
        // Подготовка тестовых файлов
        File folder = tempDir.toFile();
        File testFile1 = createEmptyWavFile(folder, "test1.wav");
        File testFile2 = createEmptyWavFile(folder, "test2.wav");
        
        // 1. Выбор папки с музыкой
        // Симулируем нажатие на кнопку "Выбрать папку"
        JButton chooseFolderButton = findButtonByText(ui, "Выбрать папку");
        assertNotNull(chooseFolderButton, "Кнопка 'Выбрать папку' не найдена");
        
        // Мокаем JFileChooser для выбора нашей тестовой папки
        JFileChooser mockChooser = new JFileChooser() {
            @Override
            public int showOpenDialog(Component parent) {
                setSelectedFile(folder);
                return JFileChooser.APPROVE_OPTION;
            }
        };
        
        // Заменяем стандартный JFileChooser на наш мок
        UIManager.put("FileChooserUI", mockChooser.getUI().getClass().getName());
        
        // Нажимаем на кнопку выбора папки
        SwingUtilities.invokeLater(() -> chooseFolderButton.doClick());
        robot.delay(1000);
        
        // 2. Проверяем, что файлы загружены в список
        JList<?> trackList = findJListInContainer(ui);
        assertNotNull(trackList, "Список треков не найден");
        
        // Ждем, пока список треков будет заполнен
        await().atMost(5, TimeUnit.SECONDS).until(() -> trackList.getModel().getSize() == 2);
        
        // 3. Выбираем трек из списка
        SwingUtilities.invokeLater(() -> trackList.setSelectedIndex(0));
        robot.delay(500);
        
        // 4. Нажимаем кнопку Play
        JButton playButton = findButtonByText(ui, "▶ Play");
        assertNotNull(playButton, "Кнопка 'Play' не найдена");
        SwingUtilities.invokeLater(() -> playButton.doClick());
        robot.delay(1000);
        
        // 5. Нажимаем кнопку Stop
        JButton stopButton = findButtonByText(ui, "◼ Stop");
        assertNotNull(stopButton, "Кнопка 'Stop' не найдена");
        SwingUtilities.invokeLater(() -> stopButton.doClick());
        robot.delay(1000);
        
        // 6. Переключаемся на следующий трек
        JButton skipButton = findButtonByText(ui, "Skip ⏭");
        assertNotNull(skipButton, "Кнопка 'Skip' не найдена");
        SwingUtilities.invokeLater(() -> skipButton.doClick());
        robot.delay(1000);
        
        // 7. Проверяем, что выбран второй трек
        assertEquals(1, trackList.getSelectedIndex(), "Не произошло переключение на следующий трек");
    }
    
    // Вспомогательные методы
    
    private JButton findButtonByText(Container container, String text) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JButton && ((JButton) component).getText().contains(text)) {
                return (JButton) component;
            } else if (component instanceof Container) {
                JButton button = findButtonByText((Container) component, text);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
    
    private JList<?> findJListInContainer(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JList) {
                return (JList<?>) component;
            } else if (component instanceof JScrollPane) {
                Component view = ((JScrollPane) component).getViewport().getView();
                if (view instanceof JList) {
                    return (JList<?>) view;
                }
            } else if (component instanceof Container) {
                JList<?> list = findJListInContainer((Container) component);
                if (list != null) {
                    return list;
                }
            }
        }
        return null;
    }
    
    private File createEmptyWavFile(File folder, String fileName) throws IOException {
        File file = new File(folder, fileName);
        
        // Создаем минимальный WAV-файл для тестирования
        byte[] emptyWavHeader = {
            // RIFF header
            0x52, 0x49, 0x46, 0x46, // "RIFF"
            0x24, 0x00, 0x00, 0x00, // Size
            0x57, 0x41, 0x56, 0x45, // "WAVE"
            
            // fmt chunk
            0x66, 0x6d, 0x74, 0x20, // "fmt "
            0x10, 0x00, 0x00, 0x00, // Chunk size: 16
            0x01, 0x00,             // Format: 1 (PCM)
            0x01, 0x00,             // Channels: 1
            0x44, (byte) 0xAC, 0x00, 0x00, // Sample rate: 44100
            0x88, 0x58, 0x01, 0x00, // Byte rate: 88200
            0x02, 0x00,             // Block align: 2
            0x10, 0x00,             // Bits per sample: 16
            
            // data chunk
            0x64, 0x61, 0x74, 0x61, // "data"
            0x00, 0x00, 0x00, 0x00  // Chunk size: 0
        };
        
        Files.write(file.toPath(), emptyWavHeader);
        return file;
    }
}