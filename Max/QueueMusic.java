package Max;

public class QueueMusic<E>{
    private static class QueueMusicItem<E> {
        public E value;
        public QueueMusicItem<E> next;

        public QueueMusicItem(E value, QueueMusicItem<E> next) {
            this.value = value;
            this.next = next;
        }


    }

    private QueueMusicItem<E> head = null;
    private QueueMusicItem<E> tail = null;
    private int size = 0;

    public E element() throws Exception {
        if (head == null) {
            throw new Exception("Queue is empty");
        }
        return head.value;
    }

    public void enqueue(E value) {
        if (head == null) {
            head = tail = new QueueMusicItem<>(value, null);
        }
        else {
            tail.next = new QueueMusicItem<>(value, null);
            tail = tail.next;
        }
        size++;
    }

    public void dequeue() throws Exception {
        if (head == null) {
            throw new Exception("Queue is empty");
        }
        E value = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
    }

    @SafeVarargs
    public final QueueMusic<E> newQueue(E ... values) {
        int n = values.length;
        if(n == 0){
            throw new IllegalArgumentException("Queue is empty");
        }
        QueueMusic<E> new_queue = new QueueMusic<E>();
        QueueMusicItem<E> temp = head;
        for (E value : values) {
            new_queue.enqueue(value);
        }
        head = temp;
        return new_queue;
    }

    @Override
    public String toString() {
        QueueMusicItem<E> stack = head;
        QueueMusicItem<E> temp = head;
        String stack_toString = "[";
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                stack_toString += stack.value.toString() + ", ";
                stack = stack.next;

            } else {
                stack_toString += stack.value.toString();
                stack = stack.next;
            }
        }
        head = temp;
        return stack_toString + "]";
    }
}