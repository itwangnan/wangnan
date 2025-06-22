package arr;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 桶排序，“数值区间”划分处理 数组实现O(d * n)
 */
public class BucketSort {

    @Test
    public void test() {
        int[] arr = {18, 11, 28, 45, 23, 49, 26};
        arr = sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private int[] sort(int[] arr) {
        //比如按照[10,20)....[40,50)区间分开，桶里在用排序算法，最后取出来就是有序的
        LinkedList<Integer>[] bucks = new LinkedList[10];

        //每个桶收集
        for (int i = 0; i < arr.length; i++) {
            int idx = arr[i] / 10;
            LinkedList<Integer> buck = bucks[idx];
            if (buck == null) {
                bucks[idx] = new LinkedList<>();
            }
            bucks[idx].add(arr[i]);
        }

        int[] res = new int[arr.length];
        int idx = 0;
        for (int i = 0; i < bucks.length; i++) {
            if (bucks[i] == null) {
                continue;
            }
            LinkedList<Integer> buckSorts = sortBucks(bucks[i]);
            for (int j = 0; j < buckSorts.size(); j++) {
                res[idx] = buckSorts.get(j);
                idx++;
            }
        }

        return res;
    }

    //桶内排序可以使用不同的排序算法
    private LinkedList<Integer> sortBucks(LinkedList<Integer> buck) {
        buck.sort(Integer::compare);
        return buck;
    }


}
