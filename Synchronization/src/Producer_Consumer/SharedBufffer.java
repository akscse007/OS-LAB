package Producer_Consumer;

import java.util.LinkedList;
import java.util.Queue;

class SharedBuffer {
    private final Queue<Integer> queue;
    private final int capacity;

    public SharedBuffer(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // Wait if buffer is full
        }
        queue.add(item);
        System.out.println("Produced: " + item);
        notifyAll(); // Notify consumers that new item is available
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // Wait if buffer is empty
        }
        int item = queue.poll();
        System.out.println("Consumed: " + item);
        notifyAll(); // Notify producers that space is available
        return item;
    }
}