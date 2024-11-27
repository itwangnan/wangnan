package cf;

import org.junit.Test;

import java.util.Arrays;

public class CF2008H {


    @Test
    public void test(){


        int[] nums = {1, 2, 3, 4, 5};
//        System.out.println(this.exec(1,nums));
//        System.out.println(this.exec(2,nums));
//        System.out.println(this.exec(3,nums));
//        System.out.println(this.exec(4,nums));
//        System.out.println(this.exec(5,nums));
//
//        System.out.println("-------------");
//        nums = new int[]{1,2,6,4,1,3};
//        System.out.println(this.exec(2,nums));
//        System.out.println(this.exec(1,nums));
//        System.out.println(this.exec(5,nums));

//        System.out.println("*************");

        int[] init = init(nums);
        System.out.println(init[1]);
        System.out.println(init[2]);
        System.out.println(init[3]);
        System.out.println(init[4]);
        System.out.println(init[5]);
        System.out.println("-------------");
        nums = new int[]{1,2,6,4,1,3};
        init = init(nums);
        System.out.println(init[2]);
        System.out.println(init[1]);
        System.out.println(init[5]);

    }

    private int exec(int x, int[] nums) {

        int[] clone = nums.clone();
        for (int i = 0; i < clone.length; i++) {
            clone[i] %= x;
        }

        Arrays.sort(clone);
        return clone[clone.length / 2];
    }

    private int[] init(int[] nums) {
        int n = nums.length;

        int[] c = new int[n + 1];
        //1≤𝑎𝑖≤𝑛
        for (int i = 0; i < n; i++) {
            c[nums[i]]++;
        }

        // 计算前缀和
        for (int i = 1; i <= n; i++) {
            c[i] += c[i - 1];
        }

        int[] res = new int[n + 1];


        // 对每个 x 计算最小的可能中位数
        for (int x = 1; x <= n; x++) {

            int l = 0, r = x - 1;
            while (l <= r) {
                //目标是找到一个最小的 mid，使得操作后数组中小于或等于这个 mid 的元素数量至少达到n/2,从而使它成为中位数。
                int mid = (l + r) / 2;

                int cnt = c[mid];

                // 使用调和级数来优化计算，模x不超过mid的数量
                //[0,mid] (k * x - 1,k * x + mid]=[k * x,k * x + mid]
//                for (int k = 1; k * x <= n; k++) {
//                    cnt += c[Math.min(k * x + mid, n)] - c[k * x - 1];
//                }
                for (int k = x; k <= n; k+=x) {
                    cnt += c[Math.min(k + mid, n)] - c[k - 1];
                }

                //模x不超过mid的数量 > 一半,cnt == n/2的时候就是结果l
                if (cnt <= n / 2) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
//                if (cnt > n / 2) {
//                    //取左边
//                    r = mid - 1;
//                } else {
//                    //右边
//                    l = mid + 1;
//                }
            }
            //l就是x在nums中的中位数
            res[x] = l;
        }

        return res;
    }

}
