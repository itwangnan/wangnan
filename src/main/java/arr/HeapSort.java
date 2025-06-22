package arr;

import org.junit.Test;
import tree.Heap;

import java.util.Arrays;

/**
 * 堆排序
 */
public class HeapSort {

    @Test
    public void test() {
        int[] arr = {5, 2, 3, 5, 2, 0, 1};
        arr = sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private int[] sort(int[] arr) {
        Heap heap = new Heap(arr);

        int[] res = new int[arr.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = heap.poll();
        }

        return res;
    }
}
