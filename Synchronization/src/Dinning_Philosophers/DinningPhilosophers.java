package Dinning_Philosophers;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DinningPhilosophers {
    public static void main(String[] args) {
        final int NUM_PHILOSOPHERS = 5;
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        Lock[] forks = new ReentrantLock[NUM_PHILOSOPHERS];

        // Create forks
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new ReentrantLock();
        }

        // Create philosophers
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Lock leftFork = forks[i];
            Lock rightFork = forks[(i + 1) % NUM_PHILOSOPHERS];

            // To prevent deadlock, make one philosopher pick up right fork first
            if (i == NUM_PHILOSOPHERS - 1) {
                philosophers[i] = new Philosopher(i, rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(i, leftFork, rightFork);
            }

            philosophers[i].start();
        }
    }
}
