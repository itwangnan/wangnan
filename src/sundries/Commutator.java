package sundries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commutator {

    public static void main(String[] args) {
//        int[] arr = new int[]{1,2,3,4,5};

//        int[] arr = new int[]{1,2,3,4};

//        arr = exec(arr,new int[]{2,3,4,1});
//        arr = exec(arr,new int[]{1,3,4,2});
//        arr = exec(arr,new int[]{4,3,2,1});
//        arr = exec(arr,new int[]{4,3,1,2});
//
//        System.out.println(Arrays.toString(arr));
//
//        arr = exec(arr,new int[]{2,3,4,1});
//        arr = exec(arr,new int[]{1,3,4,2});
//        arr = exec(arr,new int[]{4,3,2,1});
//        arr = exec(arr,new int[]{4,3,1,2});
//
//        System.out.println(Arrays.toString(arr));
//
//        arr = exec(arr,new int[]{2,3,4,1});
//        arr = exec(arr,new int[]{1,3,4,2});
//        arr = exec(arr,new int[]{4,3,2,1});
//        arr = exec(arr,new int[]{4,3,1,2});
//
//        System.out.println(Arrays.toString(arr));

        int[] arr = new int[]{1,2,3,4,5};
        List<Integer> p = Arrays.asList(2, 5, 3, 4, 1);
        System.out.println(invert(p, 5));


//        System.out.println(invert(Arrays.asList(4,1,5,3,2),5));

//        arr = exec(arr,new int[]{2,5,3,4,1});
//        System.out.println(Arrays.toString(arr));
//        arr = exec(arr,new int[]{5,1,3,4,2});
//        System.out.println(Arrays.toString(arr));
//        arr = exec(arr,new int[]{3,4,5,2,1});
//        arr = exec(arr,new int[]{5,2,1,4,3});
//        arr = exec(arr,new int[]{5,4,3,1,2});
//
//        System.out.println(Arrays.toString(arr));
//
//        arr = exec(arr,new int[]{2,5,3,4,1});
//        arr = exec(arr,new int[]{3,4,5,2,1});
//        arr = exec(arr,new int[]{5,2,1,4,3});
//        arr = exec(arr,new int[]{5,4,3,1,2});
//
//        System.out.println(Arrays.toString(arr));
//
//        arr = exec(arr,new int[]{2,5,3,4,1});
//        arr = exec(arr,new int[]{3,4,5,2,1});
//        arr = exec(arr,new int[]{5,2,1,4,3});
//        arr = exec(arr,new int[]{5,4,3,1,2});
//
//        System.out.println(Arrays.toString(arr));

//        List<List<Integer>> all = permute(arr);
//        List<List<Integer>> a5 = commutator(all, arr.length);
//        List<List<Integer>> c_a5 = commutator(a5, arr.length);
//        List<List<Integer>> c_c_a5 = commutator(c_a5, arr.length);
//        List<List<Integer>> c_c_c_a5 = commutator(c_a5, arr.length);
//
//
//        System.out.println(all);
//        System.out.println(a5);
//        System.out.println(c_a5);
//        System.out.println(c_c_a5);
//        System.out.println(c_c_c_a5);
//
//        System.out.println(invert(Arrays.asList(2,5,3,4,1),arr.length));

    }

    public static List<List<Integer>> commutator(List<List<Integer>> all,Integer len){
        List<List<Integer>> a5 = new ArrayList<>();
        for (List<Integer> a : all) {
            for (List<Integer> b : all) {
                List<Integer> list = commutate(a, b, len);
                if (!exist(a5,list)){
                    a5.add(list);
                }

            }
        }
        return a5;
    }

    private static boolean exist(List<List<Integer>> ou, List<Integer> list) {
        for (List<Integer> ouNum : ou) {
            if (ouNum.toString().equals(list.toString())){
                return true;
            }
        }
        return false;
    }

    public static int[] exec(int[] arr,int[] toc){
        int[] res = new int[arr.length];

        for (int i = 0; i < toc.length; i++) {
            int index = toc[i] - 1;
            int next = i+1 == toc.length ? toc[0] - 1 :  toc[i+1] - 1;

            res[next] = arr[index];
        }
        return res;
    }

    public static List<Integer> exec(List<Integer> arr,List<Integer> toc){
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < toc.size(); i++) {
            int index = toc.get(i) - 1;
            int next = i+1 == toc.size() ? toc.get(0) - 1 :  toc.get(i+1) - 1;

            res.set(next,arr.get(index));
        }
        return res;
    }

    public static List<Integer> compose(List<Integer> p1,List<Integer> p2,Integer len){

        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= len; i++) {
            res.add(p2.get(p1.get(i - 1) - 1));
        }

        return res;
    }

    public static List<Integer> invert(List<Integer> p,Integer len){

        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= len; i++) {
            res.add(p.indexOf(i) + 1);
        }
        return res;
    }

    public static List<Integer> commutate(List<Integer> p1,List<Integer> p2,Integer len){
        return compose(compose(compose(p1,p2,len),invert(p1,len),len),invert(p2,len),len);
    }
//
//    public static int[] exec2(int[] p1,int[] p2,int len){
//        int[] res = new int[len];
//        for (int i = 1; i <= len; i++) {
//            res[i-1] = p2[p1[i - 1] - 1];
//        }
//        return res;
//    }
//
//    public static int[][] permutation(int[] arr){
//        int len = 1;
//        for (int i = 1; i <= arr.length-1; i++) {
//            len *= i;
//        }
//
//        int[][] res = new int[arr.length][len];
//
//        Stack<int[]> stack = new Stack<>();
//        stack.push(arr);
//        int flagEr = 0;
//        while (!stack.empty()){
//            int[] pop = stack.pop();
//            if (pop.length == 1){
//                //开始合并，合到没有长度是一的
//                int[] flag = null;
//                while (!stack.empty()){
//                    int[] zi = stack.peek();
//                    if (zi.length != 1){
//                       break;
//                    }
//                    int[] zi1 = flag == null ? stack.pop() : flag;
//                    int[] zi2 = stack.pop();
//                    flag = merge(zi2,zi1);
//                }
//                res[flagEr] = flag;
//                if (res[flagEr].length == len){
//                    flagEr++;
//                }
//            }else {
//                for (int i = 0; i < pop.length; i++) {
//                    int[] son = new int[pop.length - 1];
//                    int index = 0;
//                    for (int j = 0; j < pop.length; j++) {
//                        if (i != j){
//                            son[index] = arr[j];
//                            index++;
//                        }
//                    }
//
//                    stack.push(new int[]{pop[i]});
//                    stack.push(son);
//                }
//            }
//        }
//
//
//
////        for (int i = 0; i < arr.length; i++) {
////            int now = arr[i];
////
////            int[] son = new int[arr.length - 1];
////            int index = 0;
////            for (int j = 0; j < arr.length; j++) {
////                if (i != j){
////                    son[index] = arr[j];
////                    index++;
////                }
////            }
////            int[] obj = test(all ,son, flag - 1);
////
////            if (obj instanceof int[]){
////
////
////            }else {
////
////
////            }
////
////        }
//
//
//        return res;
//    }
//
//    private static int[] merge(int[] arr1, int[] arr2) {
//        int len = arr1.length + arr2.length;
//        int[] res = new int[len];
//        for (int i = 0; i < arr1.length; i++) {
//            res[i] = arr1[i];
//        }
//        for (int i = 0; i < arr2.length; i++) {
//            res[arr1.length+i] = arr2[i];
//        }
//        System.out.println(Arrays.toString(res));
//        return res;
//    }


    public static List<List<Integer>> permute(int[] nums) {
        // 首先是特判
        int len = nums.length;
        // 使用一个动态数组保存所有可能的全排列
        List<List<Integer>> res = new ArrayList<>();

        if (len == 0) {
            return res;
        }

        boolean[] used = new boolean[len];
        List<Integer> path = new ArrayList<>();

        dfs(nums, len, 0, path, used, res);
        return res;
    }

    private static void dfs(int[] nums, int len, int depth,
                            List<Integer> path, boolean[] used,
                            List<List<Integer>> res) {
        if (depth == len) {
            // 3、不用拷贝，因为每一层传递下来的 path 变量都是新建的
            res.add(path);
            return;
        }

        for (int i = 0; i < len; i++) {
            if (!used[i]) {
                // 1、每一次尝试都创建新的变量表示当前的"状态"
                List<Integer> newPath = new ArrayList<>(path);
                newPath.add(nums[i]);

                boolean[] newUsed = new boolean[len];
                System.arraycopy(used, 0, newUsed, 0, len);
                newUsed[i] = true;

                dfs(nums, len, depth + 1, newPath, newUsed, res);
                // 2、无需回溯
            }
        }
    }
}
