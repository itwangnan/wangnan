package cf;

import org.junit.Test;

import java.util.Random;
import java.util.Scanner;

/**
 * https://www.acwing.com/solution/content/221487/
 */
public class CF1701C {

    @Test
    public void test(){


        System.out.println(exec(2, new int[]{1, 2, 1, 2}));
        System.out.println(exec(2, new int[]{1, 1, 1, 1}));
        System.out.println(exec(5, new int[]{5,1,3,2,4}));
        System.out.println(exec(1, new int[]{1}));

    }

    public int exec(int n, int[] arr){
        // n 工人 m 每个总数
        int m = arr.length;

        //每个工人擅长的任务数
        int[] cnt = new int[n];
        for (int i : arr) {
            cnt[i-1]++;
        }

        int l = 0;
        int r = m - 1;

        while (l <= r){

            //时间
            int mid = l + ((r - l) >>> 1);

            int taskNum = this.get(cnt,mid);

            if (taskNum < m){
                l = mid + 1;
            }else {
                //证明可能有更少时间
                r = mid - 1;
            }

        }


        return l;
    }

    private int get(int[] cnt, int x) {

        int res = 0;
        for (int i = 0; i < cnt.length; i++) {

            if (cnt[i] < x){
                res += cnt[i];
                res += ((x - cnt[i]) / 2);
            }else {
                res += x;
            }
        }


        return res;
    }
}
