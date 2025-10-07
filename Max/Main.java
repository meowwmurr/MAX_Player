package Max;

public class Main {
    public static void main(String[] args) throws Exception {
//        MaxForm form = new MaxForm();
//        form.setVisible(true);
        QueueMusic<Integer> newQueue = new QueueMusic<>();
        newQueue.enqueue(10);
        newQueue.enqueue(20);
        newQueue.enqueue(30);
        newQueue.enqueue(40);
        newQueue.enqueue(50);
        newQueue.dequeue();
        System.out.println(newQueue);
    }
}
