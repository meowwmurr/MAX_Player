package Max.tests;

import Max.UI;
import Max.collections.MyArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UITest {
    private UI ui;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        ui = new UI();
    }
    
    @Test
    void testUIInitialization() {
        assertNotNull(ui);
        assertEquals("MaxPlayer", ui.getTitle());
        assertEquals(500, ui.getWidth());
        assertEquals(350, ui.getHeight());
    }
    
    @Test
    void testSearchWavFilesEmptyFolder() throws Exception {
        // Получаем доступ к приватным полям через рефлексию
        Field dirSongsField = UI.class.getDeclaredField("dirSongs");
        dirSongsField.setAccessible(true);
        Field listModelField = UI.class.getDeclaredField("listModel");
        listModelField.setAccessible(true);
        
        // Создаем пустую папку
        File emptyFolder = tempDir.toFile();
        
        // Вызываем метод через рефлексию
        java.lang.reflect.Method searchWavFilesMethod = UI.class.getDeclaredMethod("searchWavFiles", File.class);
        searchWavFilesMethod.setAccessible(true);
        searchWavFilesMethod.invoke(ui, emptyFolder);
        
        // Проверяем результаты
        MyArrayList<?> dirSongs = (MyArrayList<?>) dirSongsField.get(ui);
        DefaultListModel<?> listModel = (DefaultListModel<?>) listModelField.get(ui);
        
        assertEquals(0, dirSongs.size());
        assertEquals(0, listModel.size());
    }
    
    @Test
    void testSearchWavFilesWithWavFiles() throws Exception {
        // Получаем доступ к приватным полям через рефлексию
        Field dirSongsField = UI.class.getDeclaredField("dirSongs");
        dirSongsField.setAccessible(true);
        Field listModelField = UI.class.getDeclaredField("listModel");
        listModelField.setAccessible(true);
        
        // Создаем тестовые WAV файлы
        File folder = tempDir.toFile();
        File testFile1 = new File(folder, "test1.wav");
        File testFile2 = new File(folder, "test2.wav");
        File testFile3 = new File(folder, "test3.txt"); // Не WAV файл
        
        testFile1.createNewFile();
        testFile2.createNewFile();
        testFile3.createNewFile();
        
        // Вызываем метод через рефлексию
        java.lang.reflect.Method searchWavFilesMethod = UI.class.getDeclaredMethod("searchWavFiles", File.class);
        searchWavFilesMethod.setAccessible(true);
        searchWavFilesMethod.invoke(ui, folder);
        
        // Проверяем результаты
        MyArrayList<?> dirSongs = (MyArrayList<?>) dirSongsField.get(ui);
        DefaultListModel<?> listModel = (DefaultListModel<?>) listModelField.get(ui);
        
        assertEquals(2, dirSongs.size());
        assertEquals(2, listModel.size());
    }
    
    @Test
    void testPlaySelectedTrackNoSelection() throws Exception {
        // Мокаем JOptionPane для проверки вызова сообщения
        try (var mockedStatic = mockStatic(JOptionPane.class)) {
            // Вызываем метод через рефлексию
            java.lang.reflect.Method playSelectedTrackMethod = UI.class.getDeclaredMethod("playSelectedTrack");
            playSelectedTrackMethod.setAccessible(true);
            playSelectedTrackMethod.invoke(ui);
            
            // Проверяем, что было показано сообщение об ошибке
            mockedStatic.verify(() -> 
                JOptionPane.showMessageDialog(
                    eq(ui), 
                    eq("Выберите трек из списка!"),
                    any(),
                    anyInt()
                )
            );
        }
    }
}