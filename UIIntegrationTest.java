package Max.test.integration;

import Max.UI;
import Max.collections.MyArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UIIntegrationTest {
    private UI ui;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        ui = new UI();
    }
    
    @Test
    void testUIWithCollectionsIntegration() throws Exception {
        // Получаем доступ к приватным полям через рефлексию
        Field dirSongsField = UI.class.getDeclaredField("dirSongs");
        dirSongsField.setAccessible(true);
        
        // Проверяем, что коллекция MyArrayList инициализирована в UI
        MyArrayList<?> dirSongs = (MyArrayList<?>) dirSongsField.get(ui);
        assertNotNull(dirSongs);
        assertTrue(dirSongs.isEmpty());
    }
    
    @Test
    void testSearchAndPlayIntegration() throws Exception {
        // Создаем тестовые WAV файлы
        File folder = tempDir.toFile();
        File testFile = new File(folder, "test.wav");
        
        // Создаем пустой WAV файл для тестирования
        createEmptyWavFile(testFile);
        
        // Получаем доступ к приватным полям и методам через рефлексию
        Field dirSongsField = UI.class.getDeclaredField("dirSongs");
        dirSongsField.setAccessible(true);
        Field listModelField = UI.class.getDeclaredField("listModel");
        listModelField.setAccessible(true);
        Field trackListField = UI.class.getDeclaredField("trackList");
        trackListField.setAccessible(true);
        
        // Вызываем метод searchWavFiles
        java.lang.reflect.Method searchWavFilesMethod = UI.class.getDeclaredMethod("searchWavFiles", File.class);
        searchWavFilesMethod.setAccessible(true);
        searchWavFilesMethod.invoke(ui, folder);
        
        // Проверяем, что файл был найден и добавлен в коллекцию
        MyArrayList<?> dirSongs = (MyArrayList<?>) dirSongsField.get(ui);
        DefaultListModel<?> listModel = (DefaultListModel<?>) listModelField.get(ui);
        assertEquals(1, dirSongs.size());
        assertEquals(1, listModel.size());
        
        // Мокаем AudioSystem и Clip для тестирования воспроизведения
        try (var audioSystemMock = mockStatic(AudioSystem.class)) {
            Clip clipMock = mock(Clip.class);
            audioSystemMock.when(() -> AudioSystem.getClip()).thenReturn(clipMock);
            
            // Устанавливаем выбранный индекс в JList
            JList<?> trackList = (JList<?>) trackListField.get(ui);
            trackList.setSelectedIndex(0);
            
            // Вызываем метод playSelectedTrack
            java.lang.reflect.Method playSelectedTrackMethod = UI.class.getDeclaredMethod("playSelectedTrack");
            playSelectedTrackMethod.setAccessible(true);
            
            // Перехватываем исключение, так как мы не можем полностью эмулировать аудио файл
            try {
                playSelectedTrackMethod.invoke(ui);
            } catch (Exception e) {
                // Ожидаемое исключение из-за мока
            }
            
            // Проверяем, что был вызван метод getClip()
            audioSystemMock.verify(() -> AudioSystem.getClip());
        }
    }
    
    @Test
    void testStopPlaybackIntegration() throws Exception {
        // Получаем доступ к приватным полям через рефлексию
        Field currentClipField = UI.class.getDeclaredField("currentClip");
        currentClipField.setAccessible(true);
        
        // Создаем мок для Clip
        Clip clipMock = mock(Clip.class);
        currentClipField.set(ui, clipMock);
        
        // Вызываем метод stopPlayback
        java.lang.reflect.Method stopPlaybackMethod = UI.class.getDeclaredMethod("stopPlayback");
        stopPlaybackMethod.setAccessible(true);
        stopPlaybackMethod.invoke(ui);
        
        // Проверяем, что был вызван метод stop() и close() у Clip
        verify(clipMock).stop();
        verify(clipMock).close();
    }
    
    // Вспомогательный метод для создания пустого WAV файла
    private void createEmptyWavFile(File file) throws IOException {
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
    }
}