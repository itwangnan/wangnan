package arr;

import org.junit.Test;

import java.util.Arrays;

/**
 * 插入排序 O(n^2)
 */
public class InsertionSort {

    @Test
    public void test() {
        int[] arr = {5, 2, 3, 5, 2, 0, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }


    //分为已排序和未排序两种集合，每次将未排序的一个元素加入到已排序中
    private void sort(int[] arr) {

        for (int i = 1; i < arr.length; i++) {

            int a = arr[i];
            int j = i - 1;

            //[0,j]已排序 i加入其中,从 j开始比较i
            while (j >= 0 && arr[j] > a) {
                arr[j+1] = arr[j];
                j--;
            }

            arr[j+1] = a;
        }


    }
}
