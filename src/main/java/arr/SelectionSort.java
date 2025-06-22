package arr;

import org.junit.Test;

import java.util.Arrays;

/**
 * 选择排序 O(n^2)
 */
public class SelectionSort {

    @Test
    public void test() {
        int[] arr = {5, 2, 3, 5, 2, 0, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    //找出第 i 小的元素（也就是 A_{i..n} 中最小的元素），然后将这个元素与数组第 i 个位置上的元素交换
    public void sort(int[] arr){
        int c = 0;
        while (c < arr.length){
            int min = arr[c];
            int idx = c;
            for (int i = c+1; i < arr.length; i++) {
                if (arr[i] < min){
                    min = arr[i];
                    idx = i;
                }
            }
            arr[idx] = arr[c];
            arr[c] = min;

            c++;
        }
    }
}
