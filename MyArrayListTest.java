package Max.collections.test;

import Max.collections.MyArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyArrayListTest {
    private MyArrayList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyArrayList<>();
    }

    @Test
    void testAddAndGet() {
        list.add("Элемент 1");
        list.add("Элемент 2");
        
        assertEquals("Элемент 1", list.get(0));
        assertEquals("Элемент 2", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void testRemove() {
        list.add("Элемент 1");
        list.add("Элемент 2");
        list.add("Элемент 3");
        
        String removed = list.remove(1);
        
        assertEquals("Элемент 2", removed);
        assertEquals(2, list.size());
        assertEquals("Элемент 1", list.get(0));
        assertEquals("Элемент 3", list.get(1));
    }

    @Test
    void testIsEmpty() {
        assertTrue(list.isEmpty());
        
        list.add("Элемент");
        assertFalse(list.isEmpty());
        
        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    void testClear() {
        list.add("Элемент 1");
        list.add("Элемент 2");
        
        list.clear();
        
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testIndexOutOfBounds() {
        list.add("Элемент");
        
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void testIterator() {
        list.add("Элемент 1");
        list.add("Элемент 2");
        list.add("Элемент 3");
        
        int count = 0;
        for (String element : list) {
            assertNotNull(element);
            count++;
        }
        
        assertEquals(3, count);
    }
}