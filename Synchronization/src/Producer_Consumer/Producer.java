package Producer_Consumer;

class Producer extends Thread {
    private final SharedBuffer buffer;

    public Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.produce(i);
                Thread.sleep(1000); // Simulate production time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}