package sundries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.sort;

public class SortTest {



    //冒泡排序
    public static void test(List<Integer> list) {

        int flag ;
        //这个思想相当于没有经过交换就可以提前退出了，没必要一直往下
        int c = 1;
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < list.size() && c > 0; i++) {
            c = 0;
            for (int j = list.size() - 1; j > i; j--) {
                count1 +=1;
                if (list.get(j) < list.get(j-1)){
                    flag = list.get(j);
                    list.set(j,list.get(j-1));
                    list.set(j-1,flag);
                    c += 1;

                    count2 += 1;
                }

            }


        }
        System.out.println(list);
        System.out.println(count1);
        System.out.println(count2);
    }

    //选择排序
    public static void test2(List<Integer> list) {

        int flag = 0;
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < list.size() - 1; i++) {

            Integer min = i;
            for (int j = i+1; j < list.size(); j++) {
                count1 +=1;
                if (list.get(min) > list.get(j)){
                    min = j;
                    count2 += 1;
                }
            }
            if (min != i){
                flag = list.get(min);
                list.set(min,list.get(i));
                list.set(i,flag);
            }

        }
        System.out.println(list);
        System.out.println(count1);
        System.out.println(count2);
    }


    //直接插入排序
    public static void test3(List<Integer> list) {

        int count1 = 0;
        int count2 = 0;
        int j = 0;

//        for (int i = 0, j = i; i < list.size(); j = ++i) {
//            int ai = a[i + 1];
//            while (ai < a[j]) {
//                a[j + 1] = a[j];
//                if (j-- == left) {
//                    break;
//                }
//            }
//            a[j + 1] = ai;
//        }
        for (int i = 1; i < list.size(); i++) {

            if (list.get(i) < list.get(i-1)){
                Integer temp = list.get(i);
                count1 += 1;
                for (j = i - 1; j >= 0 && list.get(j) > temp ; j--) {
                    list.set(j+1,list.get(j));

                    count2 += 1;
                }
                list.set(j+1,temp);


            }




        }
        System.out.println(list);
        System.out.println(count1);
        System.out.println(count2);
    }


    //直接插入排序 n2 >> 希尔排序 n*log2n
    public static void test4(List<Integer> list) {

        int count1 = 0;
        int count2 = 0;
        int j = 0;
        int tep = list.size();
        do {
            tep = tep / 3 + 1;
            for (int i = tep; i < list.size(); i++) {

                if (list.get(i) < list.get(i-tep)){
                    Integer temp = list.get(i);
                    count1 += 1;
                    for (j = i - tep; j >= 0 && list.get(j) > temp ; j-=tep) {
                        list.set(j+tep,list.get(j));

                        count2 += 1;
                    }
                    list.set(j+tep,temp);

                }



            }
        }while (tep > 1);

        System.out.println(list);
        System.out.println(count1);
        System.out.println(count2);
    }


    //堆排序 n*log2n
    public static void test5(List<Integer> list) {

        int size = list.size()-1;


        for (int i = size / 2; i > 0; i--) {

            heapAdjust(list,i,size);

        }

        for (int i = size ; i > 1; i--) {

            swap(list,1,i);
            heapAdjust(list,1,i-1);
        }

        System.out.println(list);
    }

    private static void swap(List<Integer> list, int start, int last) {

        int temp = list.get(start);
        list.set(start,list.get(last));
        list.set(last,temp);


    }

    private static void heapAdjust(List<Integer> list, int i, int size) {
        Integer flag = list.get(i);

        int j;

        for ( j = 2*i; j <= size; j*=2) {

            if (j < size && list.get(j+1) > list.get(j)){
                j++;
            }

            if (flag >= list.get(j)){
                break;
            }
            Integer flag2 = list.get(j);
            list.set(j,flag);
            flag = flag2;
            j = i;
        }

        list.set(j,flag);

    }

    private static Stack<Map<String,Integer>> stack = new Stack<>();

    public static void agSort(int[] arr,int start,int end) {

        Map<String,Integer> map = new HashMap<>();
        map.put("start",start);
        map.put("end",end);
        stack.insert(map);

        while (stack.exist()){
            Map<String, Integer> res = stack.get();

            int ag = ag(arr, res.get("start"), res.get("end"));

            if (res.get("start") < ag-1){
                Map<String,Integer> map2 = new HashMap<>();
                map2.put("start",start);
                map2.put("end",ag-1);
                stack.insert(map2);
            }

            if (ag+1 < res.get("end")){
                Map<String,Integer> map3 = new HashMap<>();
                map3.put("start",ag+1);
                map3.put("end",end);
                stack.insert(map3);
            }

        }



    }

    private static int ag(int[] arr, int start, int end) {

        int index = start;
        int flag = arr[index];


        boolean temp = true;
        int c;
        while (start != end){
            if (temp){
                if(arr[end] > flag){
                    end--;
                }else {
                    temp = false;
                }
            }else {
                if(arr[start] <= flag){
                    start++;
                }else {
                    c = arr[start];
                    arr[start] = arr[end];
                    arr[end] = c;
                    temp = true;


                }
            }
        }

        arr[index] = arr[start];
        arr[start] = flag;
        index = start;
        return index;
    }


//    public static void quickSort(int[] arr, int startIndex, int endIndex) {
//        // 递归结束条件：startIndex大等于endIndex的时候
//        if (startIndex >= endIndex) {
//            return;
//        }
//        // 得到基准元素位置
//        int pivotIndex = partition(arr, startIndex, endIndex);
//        // 根据基准元素，分成两部分递归排序
//        quickSort(arr, startIndex, pivotIndex - 1);
//        quickSort(arr, pivotIndex + 1, endIndex);
//    }
//    private static int partition(int[] arr, int startIndex, int endIndex) {
//        // 取第一个位置的元素作为基准元素
//        int pivot = arr[startIndex];
//        int left = startIndex;
//        int right = endIndex;
//        while( left != right) {
//        //控制right指针比较并左移
//            while(left<right && arr[right] > pivot){
//                right--;
//            }
////控制right指针比较并右移
//            while( left<right && arr[left] <= pivot) {
//                left++;
//            }
////交换left和right指向的元素
//            if(left<right) {
//                int p = arr[left];
//                arr[left] = arr[right];
//                arr[right] = p;
//            }
//        }
////pivot和指针重合点交换
//        int p = arr[left];
//        arr[left] = arr[startIndex];
//        arr[startIndex] = p;
//        return left;
//    }

    public static void main(String[] args) {

//        List<Integer> arr = Arrays.asList(3, 4, 2, 3, 1, 4, 5, 6, 1);
//        test(arr);
//        List<Integer> arr2 = Arrays.asList(3, 4, 2, 3, 1, 4, 5, 6, 1);
//        test2(arr2);
//        List<Integer> arr3 = Arrays.asList(3, 4, 2, 3, 1, 4, 5, 6, 1);
//        test3(arr3);
//        List<Integer> arr4 = Arrays.asList(3, 4, 2, 3, 1, 4, 5, 6, 1);
//        test4(arr4);
//        List<Integer> arr5 = Arrays.asList(3, 4, 2, 3, 1, 4, 5, 6, 1);
//        test5(arr5);

        int[] arr = new int[]{3, 4, 2, 3, 1, 4, 5, 6, 1};
//        agSort(arr,0,arr.length-1);
        agSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }


}
