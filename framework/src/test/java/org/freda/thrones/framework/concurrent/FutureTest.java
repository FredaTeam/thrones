package org.freda.thrones.framework.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create on 2018/8/9 14:18
 */
public class FutureTest {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();


    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        Thread t1 = new Thread(new Thread1(service), "thread1");
        t1.start();

        Thread.sleep(1000);
        service.signal();

    }

    static class Service {

        void await() {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " start await");
//                condition.await();
                condition.await(10000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        void signal() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " wake up");
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    static class Thread1 implements Runnable {

        Service service;

        private Thread1(Service service) {
            this.service = service;
        }

        @Override
        public void run() {
            service.await();
            System.out.println(Thread.currentThread().getName() + " print");
        }
    }
}
