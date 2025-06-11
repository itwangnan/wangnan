package luogu;

import java.util.Scanner;
/**
 * 树上倍增+最大区间（动态规划）
 * https://www.luogu.com.cn/problem/P4513
 */
public class P_4513 {
    static final int MAXN = 500005;
    //这段序列的总和

    //原始数组长度
    int len;

//    int[] sum;
//    //以左端点为头最大的连续序列的和
//    int[] maxLeft;
//    //以右端点为尾最大的连续序列的和
//    int[] maxRight;
//    //返回任意区间最大连续值 （答案）
//    int[] ans;
    SegmentTree[] trees;

    static int NODE = 1;

    static Scanner scanner = new Scanner(System.in);

    class SegmentTree{
        int sum;
        int maxLeft;
        int maxRight;
        int ans;
    }

    public P_4513(int n) {
        len = n;
        n -= 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = (n+1) << 1;
        int maxLen = MAXN << 1;
//        sum = new int[n];
//        maxLeft = new int[n];
//        maxRight = new int[n];
//        ans = new int[n];
        trees = new SegmentTree[Math.min(maxLen,n)];
        build(NODE, 1, len);
    }

    /**
     * node 当前节点
     * @param node
     * @param l
     * @param r
     */
    public void build(int node,int l,int r){
        if (trees[node] == null) {
            trees[node] = new SegmentTree();
        }
        if (l == r){
            int num = scanner.nextInt();
//            sum[node] = nums[l-1];
//            maxLeft[node] = nums[l-1];
//            maxRight[node] = nums[l-1];
//            ans[node] = nums[l-1];
            SegmentTree tree = trees[node];
            tree.sum = num;
            tree.maxLeft = num;
            tree.maxRight = num;
            tree.ans = num;
            return;
        }

        int mid = (l + r)/ 2;
        this.build(node * 2,l,mid);
        this.build(node * 2 + 1,mid+1,r);

        putin(node);
    }

    /**
     * 合并节点
     * @param node
     */
    private void putin(int node) {
        SegmentTree tree = trees[node];
        SegmentTree treeL = trees[node * 2];
        SegmentTree treeR = trees[node * 2 + 1];
        tree.sum = treeL.sum + treeR.sum;
        tree.maxLeft = Math.max(treeL.maxLeft,treeL.sum + treeR.maxLeft);
        tree.maxRight = Math.max(treeR.maxRight, treeR.sum + treeL.maxRight);
        tree.ans = Math.max(Math.max(treeL.ans, treeR.ans),treeL.maxRight + treeR.maxLeft);

//        sum[node] = sum[node * 2] + sum[node * 2 + 1];
        //该节点的 以左端点为头最大和 ，应该是 左边sum+右边左端点最大和 or 左边左端点最大和
//        maxLeft[node] = Math.max(maxLeft[node * 2], sum[node * 2] + maxLeft[node * 2 + 1]);
//        maxRight[node] = Math.max(maxRight[node * 2 + 1], sum[node * 2 + 1] + maxRight[node * 2]);
//        ans[node] = Math.max(Math.max(ans[node * 2], ans[node * 2 + 1]),maxRight[node * 2] + maxLeft[node * 2 + 1]);
    }

    public void update(int pos, int val) {
        update(NODE, 1, len, pos, val);
    }

    private void update(int node, int l, int r, int pos, int val) {
        if (l == r) {
            SegmentTree tree = trees[node];
            tree.sum = val;
            tree.maxLeft = val;
            tree.maxRight = val;
            tree.ans = val;
//            sum[node] = val;
//            maxLeft[node] = val;
//            maxRight[node] = val;
//            ans[node] = val;
            return;
        }
        int mid = (l + r) / 2;
        if (pos <= mid){
            update(node * 2, l, mid, pos, val);
        } else {
            update(node * 2 + 1, mid + 1, r, pos, val);
        }
        putin(node);
    }

    public int query(int l, int r) {
//        int[] query = query(NODE, 1, len, l, r);
//        System.out.println(Arrays.toString(query));
        return query(NODE, 1, len, l, r).ans;
    }
    /**
     *
     * @param node
     * @param l 数组范围左
     * @param r 数组范围右
     * @param sl 查询区间l
     * @param sr 查询区间r
     * @return
     */
    private SegmentTree query(int node, int l, int r, int sl, int sr) {
        if (sl <= l && r <= sr) {
            //返回该[l,r]区间的值
            return trees[node];
        }

        int mid = (l + r) / 2;

        /**
         * 如果sr <= mid 则sl 一定 <= mid，所以sl <= mid && sr <= mid变为sr <= mid
         * 同理sl > mid && sr > mid 则是
         */
        if (sr <= mid) {
            return query(node * 2, l, mid, sl, sr);
        }else if (sl > mid) {
            return query(node * 2 + 1, mid + 1, r, sl, sr);
        }else {
            //这里一定是中间
            SegmentTree res = new SegmentTree();
            //sl比中间<= 则去左子树获取，因为左子树是[l,mid]
            SegmentTree lrr = query(node * 2, l, mid, sl, sr);
            //sr比中间> 则去右子树获取,因为右子树是[mid+1,r]
            SegmentTree rrr = query(node * 2 + 1, mid + 1, r, sl, sr);

//            int lSum = lrr[1];
//            int rSum = rrr[1];
//            int llMax = lrr[2];
//            int lrMax = lrr[3];
//            int rlMax = rrr[2];
//            int rrMax = rrr[3];
//            int lAns = lrr[0];
//            int rAns = rrr[0];
//
//            res[1] = lSum + rSum;
//            res[2] = Math.max(llMax, lSum + rlMax);
//            res[3] = Math.max(rrMax, rSum + lrMax);
//            res[0] = Math.max(Math.max(lAns, rAns),lrMax + rlMax);
            res.sum = lrr.sum + rrr.sum;
            res.maxLeft = Math.max(lrr.maxLeft , lrr.sum + rrr.maxLeft);
            res.maxRight = Math.max(rrr.maxRight, rrr.sum + lrr.maxRight);
            res.ans = Math.max(Math.max(lrr.ans, rrr.ans), lrr.maxRight + rrr.maxLeft);
            return res;
        }

    }

    public static void main(String[] args) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();

//        int[] nums = new int[n];
//        for (int i = 0; i < n; i++) {
//            nums[i] = scanner.nextInt();
//        }
        P_4513 p = new P_4513(n);

        for (int i = 0; i < m; i++) {
            int flag = scanner.nextInt();
            int n1 = scanner.nextInt();
            int n2 = scanner.nextInt();
            if (flag == 1){
                int l = Math.min(n1, n2);
                int r = Math.max(n1, n2);
                System.out.println(p.query(l,r));
            }else if (flag == 2){
                p.update(n1,n2);
            }
        }
        scanner.close();
    }

}
