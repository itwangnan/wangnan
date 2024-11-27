package cf;

import org.junit.Test;

/**
 * https://www.acwing.com/file_system/file/content/whole/index/content/11924428/
 */
public class CF991C {

    @Test
    public void test(){


        System.out.println(exec(68));
//        System.out.println(exec(68));

    }

    public int exec(int n){

        int l = 1;
        int r = n/2;

        while (l <= r){

            //时间
            int mid = l + ((r - l) >>> 1);

            int flag = this.get(n,mid);

            if (flag < n/2){
                l = mid + 1;
            }else {
                //证明可能有更少时间
                //左边未知，右边符合
                r = mid - 1;
            }

        }

        //下界(最小)
        return l;
    }

    private int get(int n, int x) {
        int res = 0;

        while (n > 0){

            if (n < x){
                res += n;
                n = 0;
            }else {
                n -= x;
                res += x;
            }

            int i = n / 10;
            n -= i;
        }


        return res;
    }
}
