package leetcode;

import arr.Arr;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;
import sun.misc.Unsafe;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;

/**
 * 34. 在排序数组中查找元素的第一个和最后一个位置
 * 中等
 * 相关标签
 * 相关企业
 * 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。
 *
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 *
 * 你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
 */
public class Tc {


    public static void main(String[] args) {

        //给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。
        //
        //如果数组中不存在目标值 target，返回 [-1, -1]。
        //
        //你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。

//        int[] nums = new int[]{1,2,4,6,6,6,6,7,8};
//        System.out.println(bin1(nums, 6));

        double ucb = ucb(5);
        System.out.println(ucb);

        //>=
//        System.out.println(bin1(nums, 22));
//        System.out.println(bin2(nums, 22));
//        System.out.println(bin3(nums, 22));
//        System.out.println(bin4(nums, 22));
        //<=
//        System.out.println(binR2(nums, 22));
//        System.out.println(binR3(nums, 22));
//        System.out.println(binR4(nums, 22));
//        System.out.println(Arrays.toString(test1(nums, 8)));
//        test1(nums,6);

    }

    private static double ucb(int k){

        Random rd = new Random();
        NormalDistribution[] arr = new NormalDistribution[k];
        double[] values = new double[k];
        double[] deltaMus = new double[k];
        int[] cnts = new int[k];
        for (int i = 0; i < k; i++) {
            double desiredStdDev = 10.0; // 期望标准差
            int mean = rd.nextInt(100);
            NormalDistribution normalDist = new NormalDistribution(mean, desiredStdDev);
            arr[i] = normalDist;

            double sample = normalDist.sample();
            values[i] = sample;
            deltaMus[i] = 1.0 / Math.sqrt(1);
            cnts[i] = 1;
            System.out.println(mean);
        }

        int cnt = 1000;
        while (cnt > 0){
            double maxValue = 0d;
            int index = 0;
            for (int i = 0; i < values.length; i++) {
                double max = values[i] + deltaMus[i];
                if (max > maxValue){
                    maxValue = max;
                    index = i;
                }
            }

            cnts[index]++;
            deltaMus[index] = 1.0 / Math.sqrt(cnts[index]);

            cnt--;
        }

        double maxValue = 0d;
        int index = 0;
        for (int i = 0; i < values.length; i++) {
            double max = values[i] + deltaMus[i];
            if (max > maxValue){
                maxValue = max;
                index = i;
            }
        }
        return arr[index].getMean();
    }

    private static int t1(int[] nums,int target){

        int flag = 0;
        for (int i = 0; i < nums.length; i++) {
             if (nums[i] == target){
                 flag = i;
             }
        }

        int l = getT1(nums,target);

        int flagValue = nums[flag];
        nums[flag] = nums[l];
        nums[l] = flagValue;

        System.out.println(Arrays.toString(nums));
        //l 一定<=m
        return l;
    }

    private static int getT1(int[] nums, int target) {
        //闭区间
        //首先要保证一个性质就是在遍历过程中一定要一直是闭区间
        //所以当nums[m] < t, l = m + 1,保证[m+1,r]是未检测的闭区间，如果是(m,r]就打破了这种限制
        //所以当nums[m] > t, r = m - 1,保证[l,m-1]是未检测的闭区间，如果是[r,m - 1)就打破了这种限制
        int l = 1;
        int r = nums.length + 1;

        //当l == r时候也要检查，一旦检查完一定l>r,
        // 并且[0，r] / [0，l - 1]是< t
        // 并且[l，n] / [r+1，n]是 >= t
        while (l + 1 < r){
            int m = (l + r) >>> 1;
            if (nums[m] <= target){
                l = m; //[m+1,right]
            } else {
                r = m; //[left,m-1]
            }
        }
        return l;
    }

    private static int bin4T(int[] nums,int target){


        int l = -1;
        int r = nums.length;

        //（l,r） 这时因为如果要保证集合内有值则需要(i,i+2)至少要隔开2个才有效,
        //所以 l + 1 < r 或 l + 2 <= r
        while (l + 1 < r){

            int m = l + ((r - l) >>> 1);

            if (nums[m] <= target){

                l = m;          //(m,right)

            }else if (nums[m] > target){

                r = m;          //(left,m)

            }
        }

        //r = l + 1
        return l;
    }

    private static int[] test1(int[] nums,int target){


        int l = 0;
        int r = nums.length - 1;


        while (l <= r){

            int m = (l + r) >>> 1;

            if (nums[m] < target){

                l = m + 1;

            }else if (nums[m] > target){

                r = m - 1;

            }else {
                if(nums[l] == target && nums[r] == target){
                    break;
                }
                if (nums[l] != target){
                    l++;
                }

                if (nums[r] != target){
                    r--;
                }
            }

        }

        if (l > r){
            return new int[]{-1,-1};
        }


        return new int[]{l,r};
    }


    private static int bin1(int[] nums,int target){

        //闭区间
        //首先要保证一个性质就是在遍历过程中一定要一直是闭区间
        //所以当nums[m] < t, l = m + 1,保证[m+1,r]是未检测的闭区间，如果是(m,r]就打破了这种限制
        //所以当nums[m] > t, r = m - 1,保证[l,m-1]是未检测的闭区间，如果是[r,m - 1)就打破了这种限制
        int l = 0;
        int r = nums.length - 1;

        //当l == r时候也要检查，一旦检查完一定l>r,
        // 并且[0，r] / [0，l - 1]是< t
        // 并且[l，n] / [r+1，n]是 >= t
        while (l <= r){
            int m = (l + r) >>> 1;

            if (nums[m] < target){
                //不符合
                l = m + 1; //[m+1,right]

            }else if (nums[m] >= target){
                //左边未知，右边符合
                r = m - 1; //[left,m-1]

            }
        }

        //右边符合返回左指针
        return l;
    }

    private static int binR1(int[] nums,int target){

        //闭区间
        //首先要保证一个性质就是在遍历过程中一定要一直是闭区间
        //所以当nums[m] < t, l = m + 1,保证[m+1,r]是未检测的闭区间，如果是(m,r]就打破了这种限制
        //所以当nums[m] > t, r = m - 1,保证[l,m-1]是未检测的闭区间，如果是[r,m - 1)就打破了这种限制
        int l = 0;
        int r = nums.length - 1;

        //当l == r时候也要检查，一旦检查完一定l>r,
        // 并且[0，r] / [0，l - 1]是< t
        // 并且[l，n] / [r+1，n]是 >= t
        while (l <= r){

            int m = (l + r) >>> 1;


            if (nums[m] <= target){

                l = m + 1; //[m+1,right][

            }else if (nums[m] > target){

                r = m - 1; //left,m-1]

            }
        }

        return r;
    }


    private static int bin2(int[] nums,int target){

        int l = 0;
        int r = nums.length;

        //[l,r) 当 [i,i)时空集，[i,i+1) 有值 或 [i-1,i)有值, r - l > 0 时有值
        while (l < r){

            int m = (l + r) >>> 1;

            if (nums[m] < target){

                l = m + 1; //[m+1,right)

            }else if (nums[m] >= target){

                r = m;    //[left,m)

            }
        }

        return l;
    }

    private static int binR2(int[] nums,int target){

        //左闭、右开区间
        int l = 0;
        int r = nums.length;

        //[l,r) 当 [i,i)时空集，[i,i+1) 有值 或 [i-1,i)有值, r - l > 0 时有值
        while (l < r){

            int m = l + ((r - l) >>> 1);

            if (nums[m] <= target){

                l = m + 1; //[m+1,right)

            }else if (nums[m] > target){

                r = m;    //[left,m)

            }
        }

        return r - 1;
    }

    private static int bin3(int[] nums,int target){

        //左开、右闭区间
        int l = -1;
        int r = nums.length - 1;

        //(l,r] 当 (i,i]时空集，(i-1,i]有值 或 (i,i+1]有值 r - l > 0 时有值
        while (l < r){

            //但是因为是（l,r]得取上整数
            //[left,right] 两个元素的时候，(l,m-1] (m,r],m在右半区，m必须上取整，不然到达不了[r,r], （m-1，m)m刚好就是上整数
            int m = l + ((r - l + 1) >> 1);

            if (nums[m] < target){

                l = m;      //(m,right]

            }else if (nums[m] >= target){

                r = m - 1;  //(left,m-1]

            }
        }

        //因为left是开要+1
        return l + 1;
    }

    private static int binR3(int[] nums,int target){

        //左开、右闭区间
        int l = -1;
        int r = nums.length - 1;

        //(l,r] 当 (i,i]时空集，(i-1,i]有值 或 (i,i+1]有值 r - l > 0 时有值
        //[left,right] 两个元素的时候，[l,m) (m,r],m在右半区，m必须上取整，不然到达不了[r,r], （m-1，m)m刚好就是上整数
        while (l < r){

            //但是因为是（l,r]得取上整数
            int m = l + ((r - l + 1) >>> 1);

            if (nums[m] <= target){

                l = m; //[m+1,right)

            }else if (nums[m] > target){

                r = m - 1;    //[left,m)

            }
        }

        return r;
    }


    private static int bin4(int[] nums,int target){


        int l = -1;
        int r = nums.length;

        //（l,r） 这时因为如果要保证集合内有值则需要(i,i+2)至少要隔开2个才有效,
        //所以 l + 1 < r 或 l + 2 <= r
        while (l + 1 < r){

            int m = l + ((r - l) >>> 1);

            if (nums[m] < target){

                l = m;          //(m,right)

            }else if (nums[m] >= target){

                r = m;          //(left,m)

            }
        }

        //r = l + 1
        return l + 1;
    }

    private static int binR4(int[] nums,int target){


        int l = -1;
        int r = nums.length;

        //（l,r） 这时因为如果要保证集合内有值则需要(i,i+2)至少要隔开2个才有效,
        //所以 l + 1 < r 或 l + 2 <= r
        while (l + 1 < r){

            int m = l + ((r - l) >>> 1);

            if (nums[m] <= target){

                l = m; //[m+1,right)

            }else if (nums[m] > target){

                r = m;    //[left,m)

            }
        }

        //r = l + 1
        return r - 1;
    }




//    private  static int calcSz(int i, int[] preHash, int[] powBase, Set<Integer>[] sets) {
//
//        // 开区间二分，left 一定满足要求，right 一定不满足要求
//        int left = 0;
//        //注意left和right 其实算的是 i + ？ 的距离
//        int right = Math.min(preHash.length - 1 - i, sets.length) + 1;
//
//        while (left + 1 < right){
//            int mid = (left + right) >>> 1;
//
//            //子串hash
//            long sub = (((long) preHash[i + mid] - (long) preHash[i] * powBase[mid]) % MOD + MOD) % MOD;
//
//            if (sets[mid - 1].contains((int)sub)){
//
//                left = mid;
//
//            }else{
//                right = mid;
//            }
//
//        }
//        return left;
//    }
}
