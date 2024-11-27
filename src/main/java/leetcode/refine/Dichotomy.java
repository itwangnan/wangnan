package leetcode.refine;

public class Dichotomy {

    public static void main(String[] args) {

        int[] arr = {1, 2, 3, 5, 8, 9, 10};
        System.out.println(exec2(arr, 0));
        System.out.println(exec1(arr, 8));
        System.out.println(exec1(arr, 11));
    }

    /**
     * 二分法
     * @param arr
     * @param target
     * @return
     */
    private static int exec1(int[] arr,int target) {

        /**
         * 左右闭区间
         *
         * 循环不变量
         * r+1 一定是大于
         * l-1 一定是小于
         * 最后是 r l,则l = r+1就是第一个符合的大于
         */
        int l = 0;
        int r = arr.length - 1;

        while (r >= l){

            int m = l + (r - l) / 2;

            if (arr[m] > target){
                r = m - 1;
            }else {
                l = m + 1;
            }
        }

        return l == arr.length ? -1 : arr[l];
    }

    private static int exec2(int[] arr,int target) {

        /**
         * 左右闭区间
         *
         * 循环不变量
         * r+1 一定是大于
         * l-1 一定是小于
         * 最后是 r l,则l = r+1就是第一个符合的大于
         */
        int l = 0;
        int r = arr.length - 1;

        while (r >= l){

            int m = l + (r - l) / 2;

            // > 则是获取 <=target
            if (arr[m] > target){
                r = m - 1;
            }else {
                l = m + 1;
            }
        }

        return r < 0 ? -1 : arr[r];
    }

}
