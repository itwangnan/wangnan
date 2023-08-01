package lru;

public class LRUTest {



    public static void main(String[] args) {

        LRU lru = new LRU(1);

        lru.put(1,1);
        lru.put(3,3);
        lru.put(1,4);
        lru.put(6,2);
        lru.put(4,2);
//        lru.put(1,3);



        System.out.println(lru.get(6));
    }
}
