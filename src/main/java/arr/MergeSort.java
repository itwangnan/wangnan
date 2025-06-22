package arr;

import org.junit.Test;

import java.util.Arrays;


/**
 * 归并排序 时间复杂度(nlogn) 空间复杂度O(n)
 *
 */
public class MergeSort {

    int[] arr;

    @Test
    public void test() {
        arr = new int[]{5, 2, 3, 5, 2, 0, 1};
        sortByOne(0, arr.length);
        System.out.println(Arrays.toString(arr));
    }


    //
    private int[] sort(int left,int right) {
        int[] res = new int[right-left];
        if (right - left <= 1) {
            res[0] = arr[left];
            return res;
        }
        int m = (left + right) / 2;
        int[] sortL = sort(left, m);
        int[] sortR = sort(m, right);
        //将两个合并

        int l = 0;
        int r = 0;

        for (int i = 0; i < res.length; i++) {
            if (r >= sortR.length){
                res[i] = sortL[l];
                l++;
            }else if (l >= sortL.length){
                res[i] = sortR[r];
                r++;
            }else if (sortL[l] < sortR[r]){
                res[i] = sortL[l];
                l++;
            }else {
                res[i] = sortR[r];
                r++;
            }
        }
        return res;
    }

    //空间复杂度O(1)
    private void sortByOne(int left,int right) {
        if (right - left <= 1) {
            return;
        }
        int m = (left + right) / 2;
        sortByOne(left, m);
        sortByOne(m, right);
        int l = left;
        int r = m;
        while (l < m && r < right) {
            if (arr[l] <= arr[r]){
                l++;
            }else {
                int flag = arr[r];
                //将 [l,r-1] 右移动一位 本来 [l ... r] [r l ... r-1]
                for (int i = r; i > l; i--) {
                    arr[i] = arr[i - 1];
                }
                arr[l] = flag;

                l++;
                r++;
                m++;
            }
        }


//        mergeInPlace(arr, left, m, right);
    }
}
