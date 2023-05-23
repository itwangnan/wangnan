package lru;

public class LRUTest {



    public static void main(String[] args) {

        LRU lru = new LRU(2);

        lru.put(1,1);
        lru.put(3,3);
        lru.put(2,2);
//        lru.put(1,2);



        System.out.println(lru.get(1));
    }
}
