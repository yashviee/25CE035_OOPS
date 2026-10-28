package service;

import java.util.ArrayDeque;
import java.util.Queue;

/** Small wait/notify producer-consumer buffer for the threading practical. */
public class TransactionBuffer<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;
    private boolean closed;

    public TransactionBuffer(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be positive");
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity && !closed) wait();
        if (closed) throw new IllegalStateException("Buffer is closed");
        queue.add(item);
        notifyAll();
    }

    /** Returns null after close and after all queued items have been consumed. */
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty() && !closed) wait();
        if (queue.isEmpty()) return null;
        T item = queue.remove();
        notifyAll();
        return item;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}



