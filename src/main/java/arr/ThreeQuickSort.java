package arr;

import org.junit.Test;

import java.util.Arrays;

/**
 * 三路快排
 * 三路快速排序在处理含有多个重复值的数组时,效率远高于原始快速排序
 * 最好的时间复杂度O(n) 平均O(nlogn) 最坏O(n^2) 不稳定
 */
public class ThreeQuickSort {

    private int[] arr;

    @Test
    public void test() {
//        int[] arr = {5, 2, 3, 5, 2, 0, 1};
//        int[] arr = {56, 23, 82, 45, 76, 69, 20, 45,93,16,27};
        int[] arr = {9, 8, 7, 9, 9, 10, 11, 9,9,8,17,2,1,9,9,10,6,9};
        sort(arr);

        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {
        this.arr = arr;
        sort(0,arr.length - 1);
    }

    public void sort(int l,int r){
        if(l >= r){
            return;
        }

        int pivot = arr[l];
        //小于的结尾
        int i = l;
        //等于的结尾
        int k = l+1;
        //大于的结尾
        int j = r;

        int flag = 0;
        while (j >= k){
            //找到第一个不是等于的k
            if (arr[k] == pivot){
                k++;
            } else if (arr[k] > pivot){
                //大于 那要找到j 不大于 pivot
                while (j >= k && arr[j] > pivot){
                    j--;
                }
                if (j < k){
                    break;
                }
                flag = arr[j];
                arr[j] = arr[k];
                arr[k] = flag;
                j--;
                //arr[j] > pivot的话 arr[j] == pivot 会加k
                //arr[j] < pivot的话 else 中会 i++ k++
            }else {
                flag = arr[i];
                arr[i] = arr[k];
                arr[k] = flag;
                i++;
                k++;
            }

        }

        sort(l,i-1);
        sort(j + 1, r);

    }
}
