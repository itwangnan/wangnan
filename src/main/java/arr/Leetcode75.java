package arr;

import org.junit.Test;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/0H97ZC/description/
 */
public class Leetcode75 {


    @Test
    public void test() {
//        int[] arr1 = {2,3,1,3,2,4,6,7,9,2,19};

        int[] arr1 = {28,6,22,8,44,17};
        int[] arr2 = {22,28,8,6};
//        int[] arr2 = {2,1,4,3,9,6};
        System.out.println(Arrays.toString(relativeSortArray(arr1, arr2)));
    }

    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int left = 0;
        for (int i = 0; i < arr2.length; i++) {
            int num = arr2[i];
            left = sort(arr1,num,left);
        }
        
        return arr1;
    }

    public int sort(int[] arr,int num,int l) {
        while (l < arr.length) {
            int k = l;
            for (int j = arr.length - 1; j > k; j--) {
                if (arr[j-1] == num){
                    l = j;
                } else if (arr[j] == num){
                    int flag = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = flag;
                    l = j;
                }
                else if (arr[j - 1] > arr[j]){
                    int flag = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = flag;
                }
            }
            if (k == l){
               break;
            }
        }
        return l;
    }


}
