package sundries;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueExample {
    static int MAX_SIZE = 10;
    static Queue<Integer> queue = new LinkedList<>();
    static ReentrantLock lock = new ReentrantLock();
    //不为空 消费者用
    static Condition notEmpty = lock.newCondition();
    //不满 生产者用
    static Condition notFull = lock.newCondition();

    static AtomicInteger num =  new AtomicInteger(0);

    static class Producer implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    lock.lock();
                    try{
                        while (queue.size() == MAX_SIZE){
                            notFull.await();
                        }
                        int value = num.incrementAndGet();
                        queue.offer(value);
                        System.out.println(Thread.currentThread().getName()+"-Producer : " + value);
                        notEmpty.signalAll();
                    }finally {
                        lock.unlock();
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    lock.lock();
                    try{
                        while (queue.isEmpty()){
                            notEmpty.await();
                        }
                        Integer value = queue.poll();
                        System.out.println(Thread.currentThread().getName()+"-Consumer : " + value);
                        notFull.signalAll();
                    }finally {
                        lock.unlock();
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }





    public static void main(String[] args){

        for (int i = 0; i < 2; i++) {
            new Thread(new Producer(),"Producer-"+i).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(new Consumer(),"Consumer-"+i).start();
        }
    }

}
