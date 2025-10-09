package Max.collections;

import java.util.Arrays;
import java.util.Iterator;

public class MyArrayList<T> implements MyList<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    @Override
    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T removeElement = (T) elements[index];
        
        // Сдвигаем все элементы после удаляемого на одну позицию влево
        if (index < size - 1) {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        }
        
        elements[size - 1] = null; // Очищаем последний элемент
        size--;
        return removeElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator();
    }

    private void ensureCapacity() {
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, size * 2);
        }
    }

    private class MyArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            return (T) elements[currentIndex++];
        }
    }
}
