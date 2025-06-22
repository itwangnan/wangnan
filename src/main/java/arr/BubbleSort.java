package arr;

import java.util.Arrays;

//冒泡排序 O(n^2)
public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = {2,3,1,3,2,4,6,7,9,2,19};
        sort(arr);
        System.out.println(Arrays.toString(arr));

    }


    public static void sort(int[] arr) {
        int n = arr.length - 1;
        while (n > 0){
            int k = n;
            for (int i = 0; i < k; i++) {
                if (arr[i] > arr[i+1]) {
                    int flag = arr[i];
                    arr[i] = arr[i+1];
                    arr[i+1] = flag;
                    n = i;
                }
            }
            //标记已经排序好的情况
            if (k == n){
                break;
            }
        }
    }

    public static void sort(Integer[] arr) {
        int[] c = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            c[i] = arr[i];
        }
        sort(c);
    }
}
