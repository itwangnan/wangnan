package sundries;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerDemo {

    static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

    public static void main(String[] args) {
        // 创建并启动生产者线程
        Thread producer = new Thread(new Producer(), "Producer");
        // 创建并启动消费者线程
        Thread consumer = new Thread(new Consumer(), "Consumer");

        producer.start();
        consumer.start();
    }

    static class Producer implements Runnable {
        private int value = 0;
        @Override
        public void run() {
            while (true) {
                System.out.println("Producer: " + value);
                boolean flag = queue.add(value);
                if (flag) {
                    value++;
                }else {
                    Thread.yield();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true){
                try {
                    Integer take = queue.take();


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
