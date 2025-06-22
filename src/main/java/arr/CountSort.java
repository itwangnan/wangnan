package arr;

import org.junit.Test;

import java.util.Arrays;

/**
 * 计数排序
 * O(n + w) w是排序数据的值域大小
 */
public class CountSort {

    @Test
    public void test() {
        int[] arr = {5, 2, 3, 5, 2, 0, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public void sort(int[] arr){
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max){
                max = arr[i];
            }
        }

        int[] cntArr = new int[max+1];

        for (int i : arr) {
            cntArr[i]++;
        }

        int idx = 0;
        for (int i = 0; i < cntArr.length; i++) {
            while (cntArr[i]-- > 0){
                arr[idx++] = i;
            }
        }
    }
}
