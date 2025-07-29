package Dinning_Philosophers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Philosopher extends Thread {
    private final int id;
    private final Lock leftFork;
    private final Lock rightFork;

    public Philosopher(int id, Lock leftFork, Lock rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking");
        Thread.sleep(1000);
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating");
        Thread.sleep(1000);
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();

                // Try to pick up forks
                if (leftFork.tryLock()) {
                    try {
                        if (rightFork.tryLock()) {
                            try {
                                eat();
                            } finally {
                                rightFork.unlock();
                            }
                        }
                    } finally {
                        leftFork.unlock();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}