package leetcode.refine.binary_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CombTest {


    private List<List<Integer>> res = new ArrayList<>();
    private List<Integer> path = new ArrayList<>();

    private List<String> strRes = new ArrayList<>();
    private StringBuilder strPath = new StringBuilder();

    /**
     * 组合
     * 时间复杂度:O(k * Cnk) 路径长度×搜索树的叶子数
     * 空间复杂度:O(k) 就是path
     */
    @Test
    public void leetCode77(){
        //https://leetcode.cn/problems/combinations/description/
        int n = 4;
        int k = 2;

//        this.subset1(n,k,1);
//        this.subset2(n,k,1);

//        this.comb1(k,n);
        this.comb2(k,n);

        for (List<Integer> re : res) {
            System.out.println(re);
        }
    }

    /**
     * 组合方式,倒序
     * @param k
     * @param index
     */
    private void comb1(int k,int index) {

//        if (d > index){
//            return;
//        }

        if (path.size() == k){
            res.add(new ArrayList<>(path));
            return;
        }
        //需要的数 - 长度 = 还需要的数，如果循环的时候还需要的数 > i 则不可能成功了，提前减枝
        //如果正续的话 要 d < n -i + 1
        int d = k - path.size();

        for (int i = index; i >= d; i--) {

            //k
            path.add(i);

            comb1(k, i - 1);

            path.remove(path.size() - 1);
        }


    }


    /**
     * 组合方式,选不选
     * @param k
     * @param index
     */
    private void comb2(int k,int index) {

        int d = k - path.size();
        if (d == 0){
            res.add(new ArrayList<>(path));
            return;
        }

        //为什么不是<= 是因为index我们直到1，必须大于0
        if (d < index){
            comb2(k,index - 1);
        }

        path.add(index);

        comb2(k,index - 1);

        path.remove(path.size() - 1);


    }

    /**
     * 每次选或不选
     */
    private void subset1(int n, int k,int index) {

        if (path.size() == k){
            res.add(new ArrayList<>(path));
            return;
        }
        if (n < index){
            return;
        }

        subset1(n,k,index + 1);

        path.add(index);

        subset1(n,k,index + 1);

        path.remove(path.size() - 1);
    }


    /**
     * 结果来看，每次选择后面的
     * @param n
     * @param k
     * @param index
     */
    private void subset2(int n, int k,int index) {

        if (path.size() == k){
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = index; i <= n; i++) {
            //k
            path.add(i);

            subset2(n, k, i + 1);

            path.remove(path.size() - 1);
        }


    }



    @Test
    public void leetCode216(){
        //https://leetcode.cn/problems/combinations/description/


        int n = 4;
        int k = 2;

//        this.subset1(n,k,1);
//        this.subset2(n,k,1);

//        this.comb1(k,n);
        this.comb216(k,n,9);

        for (List<Integer> re : res) {
            System.out.println(re);
        }
    }

    /**
     * 结果方向
     * @param k 个数
     * @param n 总数
     */
    private void comb216(int k, int n,int start) {

        int d = k - path.size();

        //n < 0 不满足, n > 最大的d个数和也不满足，等差数列求和 > 4 or > 4.5 都一样

        if (n < 0 || n > (start + start - d + 1) * d / 2 ){
            return;
        }

        if (d == 0){
            res.add(new ArrayList<>(path));
            return;
        }



        //d 因为一定要k个元素的原因，如果 i < d 则可选元素都不够了
        for (int i = start; i >= d; i--) {

            path.add(i);

            comb216(k,n - i,i-1);

            path.remove(path.size() - 1);
        }



    }


    @Test
    public void leetCode22(){
        //https://leetcode.cn/problems/generate-parentheses/description/
        //22.括号


        int n = 3;

//        this.subset1(n,k,1);
//        this.subset2(n,k,1);

//        this.comb1(k,n);
        this.comb22(new StringBuilder(),n,0);

        for (String re : strRes) {
            System.out.println(re);
        }
    }

    private void comb22(StringBuilder str,int left,int right) {

        if (left == 0 && right == 0){
            strRes.add(str.toString());
            return;
        }

        if (left > 0){
            comb22(str.append("("),left - 1,right + 1);
            str.delete(str.length()-1,str.length());
        }


        if(right > 0){
            comb22(str.append(")"),left,right - 1);
            str.delete(str.length()-1,str.length());
        }

    }
}
