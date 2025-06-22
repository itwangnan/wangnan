package arr;

import org.junit.Test;

import java.util.Arrays;

/**
 * 快速排序
 * 最坏O(n^2) 平均O(nlogn) 不稳定
 */
public class QuickSort {

    private int[] arr;

    @Test
    public void test() {
//        int[] arr = {5, 2, 3, 5, 2, 0, 1};
        int[] arr = {56, 23, 82, 45, 76, 69, 20, 45,93,16,27};
        sort(arr);

        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {
        this.arr = arr;
        int l = 0;
        int r = arr.length - 1;
        sort(l,r);
    }

    public void sort(int l,int r){
        if(l >= r){
            return;
        }

        int pivot = arr[l];
        int fl = l;
        int fr = r;



        while (fr > fl){
            //里面不用fl++;和fr--;因为交换过后就会被while判断到继续找
            while (fr > fl && arr[fr] >= pivot){
//                if (arr[fr] < temp) {
//                    arr[fl] = arr[fr];
//                    fl++;
//                    break;
//                }
                fr--;
            }
            arr[fl] = arr[fr];

            while (fr > fl && arr[fl] <= pivot){
//                if (arr[fl] > temp) {
//                    arr[fr] = arr[fl];
//                    fr--;
//                    break;
//                }
                fl++;
            }
            arr[fr] = arr[fl];
        }

        arr[fl] = pivot;

        sort(l,fl-1);
        sort(fr+1,r);

    }
}
