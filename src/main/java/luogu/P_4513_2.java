package luogu;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 树上倍增+最大区间（动态规划）
 * https://www.luogu.com.cn/problem/P4513
 */
public class P_4513_2 {
    static final int MAXN = 500005;
    //这段序列的总和

    //原始数组长度
    int len;

    int[] sum;
    //以左端点为头最大的连续序列的和
    int[] maxLeft;
    //以右端点为尾最大的连续序列的和
    int[] maxRight;
    //返回任意区间最大连续值 （答案）
    int[] ans;
//    SegmentTree[] trees;

    static int NODE = 1;

    static FastInput in = new FastInput(1 << 16); // 64 KB 缓冲区
//
//    class SegmentTree{
//        int sum;
//        int maxLeft;
//        int maxRight;
//        int ans;
//    }

    public P_4513_2(int n) throws IOException {
        len = n;
        n -= 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = (n+1) << 1;
        int maxLen = MAXN << 1;
        n = Math.min(maxLen,n);
        sum = new int[n];
        maxLeft = new int[n];
        maxRight = new int[n];
        ans = new int[n];
        build(NODE, 1, len);
    }

    /**
     * node 当前节点
     * @param node
     * @param l
     * @param r
     */
    public void build(int node,int l,int r) throws IOException {
        if (l == r){
            int num = in.nextInt();
            sum[node] = num;
            maxLeft[node] = num;
            maxRight[node] = num;
            ans[node] = num;
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
        sum[node] = sum[node * 2] + sum[node * 2 + 1];
//        该节点的 以左端点为头最大和 ，应该是 左边sum+右边左端点最大和 or 左边左端点最大和
        maxLeft[node] = Math.max(maxLeft[node * 2], sum[node * 2] + maxLeft[node * 2 + 1]);
        maxRight[node] = Math.max(maxRight[node * 2 + 1], sum[node * 2 + 1] + maxRight[node * 2]);
        ans[node] = Math.max(Math.max(ans[node * 2], ans[node * 2 + 1]),maxRight[node * 2] + maxLeft[node * 2 + 1]);
    }

    public void update(int pos, int val) {
        update(NODE, 1, len, pos, val);
    }

    private void update(int node, int l, int r, int pos, int val) {
        if (l == r) {
            sum[node] = val;
            maxLeft[node] = val;
            maxRight[node] = val;
            ans[node] = val;
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
        return query(NODE, 1, len, l, r)[0];
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
    private int[] query(int node, int l, int r, int sl, int sr) {
        if (sl <= l && r <= sr) {
            //返回该[l,r]区间的值
            return new int[]{ans[node],sum[node],maxLeft[node],maxRight[node]};
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
            int[] res = new int[4];
            int[] lrr = query(node * 2, l, mid, sl, sr);
            int[] rrr = query(node * 2 + 1, mid + 1, r, sl, sr);
            int lSum = lrr[1];
            int rSum = rrr[1];
            int llMax = lrr[2];
            int lrMax = lrr[3];
            int rlMax = rrr[2];
            int rrMax = rrr[3];
            int lAns = lrr[0];
            int rAns = rrr[0];
            res[1] = lSum + rSum;
            res[2] = Math.max(llMax, lSum + rlMax);
            res[3] = Math.max(rrMax, rSum + lrMax);
            res[0] = Math.max(Math.max(lAns, rAns),lrMax + rlMax);
            return res;
        }

    }

    public static void main(String[] args) throws IOException {

        int n = in.nextInt();
        int m = in.nextInt();

        P_4513_2 p = new P_4513_2(n);

        for (int i = 0; i < m; i++) {
            int flag = in.nextInt();
            int n1 = in.nextInt();
            int n2 = in.nextInt();
            if (flag == 1){
                int l = Math.min(n1, n2);
                int r = Math.max(n1, n2);
                System.out.println(p.query(l,r));
            }else if (flag == 2){
                p.update(n1,n2);
            }
        }
    }

    static class FastInput {
        private final DataInputStream in;
        private final byte[] buf;
        private int bufPos, bufLen;

        public FastInput(int bufSize) {
            in = new DataInputStream(System.in);
            buf = new byte[bufSize];
            bufPos = bufLen = 0;
        }

        private byte read() throws IOException {
            if (bufPos == bufLen) {
                bufLen = in.read(buf, 0, buf.length);
                if (bufLen == -1) return -1;
                bufPos = 0;
            }
            return buf[bufPos++];
        }

        public int nextInt() throws IOException {
            int c, sign = 1, x = 0;
            do {
                c = read();
            } while (c <= ' ' && c != -1);
            if (c == '-') {
                sign = -1;
                c = read();
            }
            for (; c >= '0' && c <= '9'; c = read()) {
                x = x * 10 + (c - '0');
            }
            return x * sign;
        }
    }

}
