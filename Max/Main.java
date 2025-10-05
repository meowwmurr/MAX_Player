package Max;

public class Main {
    public static void main(String[] args) throws Exception {
//        MaxForm form = new MaxForm();
//        form.setVisible(true);
        QueueMusic<Integer> queue = new QueueMusic<>();
        queue = queue.newQueue(1, 2, 3, 4, 5);
        System.out.println(queue.toString());
    }
}
