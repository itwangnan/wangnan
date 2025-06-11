package leetcode.refine;

/**
 * https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/solutions/2301511/shu-jie-dian-de-di-k-ge-zu-xian-by-leetc-hdxd/
 * 树上倍增
 */
public class TreeAncestor {

    public static void main(String[] args) {
        int[] arr = {-1,0,0,1,1,2,2};
        TreeAncestor treeAncestor = new TreeAncestor(7,arr );
//        System.out.println(treeAncestor.getKthAncestor(3,1));  // 返回 -1 因为不存在满足要求的祖先节点
//        System.out.println(treeAncestor.getKthAncestor(5,2));  // 返回 -1 因为不存在满足要求的祖先节点
        System.out.println(treeAncestor.getKthAncestor(6,6));  // 返回 -1 因为不存在满足要求的祖先节点
    }

    int[][] arr;
    /**
     * TreeAncestor（int n， int[] parent） 对树和父数组中的节点数初始化对象。
     * @param n
     * @param parent
     */
    public TreeAncestor(int n, int[] parent) {
        //最多n个元素，就算n个元素都是一个链条则也最多m个保存节点（最远1位数）
        int m = 32 - Integer.numberOfLeadingZeros(n);
        arr = new int[n][m];
        //首先设置第一层 2^0
        for (int i = 0; i < n; i++) {
            arr[i][0] = parent[i];
        }
        //还有，m-1层需要处理
        for (int i = 0; i < m - 1; i++) {
            //一层一层处理
            for (int x = 0; x < n; x++) {
                int p = arr[x][i];
                /**
                 * k的2^{i+1}的祖节点
                 * arr[x][i + 1] = arr[ arr[x][i] ][i]
                 * arr[x][i] = x/2^n
                 * arr[x][i + 1] = x/2^{n+1}
                 * arr[arr[x][i]][i] = arr[x][i]/2^n
                 *
                 * x/2^{n+1} = {x/2^n} / 2^n
                 */

                arr[x][i + 1] = p < 0 ? -1 : arr[p][i];
            }
        }

//        for (int i = 0; i < n; i++) {
//            Arrays.fill(arr[i],-1);
//            int pow = 0;
//            int pre = 0;
//            int father = i;
//            out:
//            while (father >= 0){
//                int step = (int)Math.pow(2, pow);
//                int num = step - pre;
//                pre = step;
//
//                for (int j = 0; j < num && father >= 0; j++) {
//                    father = parent[father];
//                }
//                arr[i][pow] = father;
//                pow++;
//            }
//        }
    }

    /**
     * getKthAncestor(int node, int k) 返回节点 node 的第 k 个祖先节点。如果不存在这样的祖先节点，返回 -1
     * @param node
     * @param k
     * @return
     */
    public int getKthAncestor(int node, int k) {
//        while (k > 0 && node >= 0){
//            int step = 31 - Integer.numberOfLeadingZeros(k);
//            k = k - flp(k);
//            node = arr[node][step];
//        }

        int m = 32 - Integer.numberOfLeadingZeros(k); // k 的二进制长度
        //这里使用了每次从小到大跳，比如14 = 8 -> 4 -> 2 也可以 2 -> 4 -> 8
        for (int i = 0; i < m; i++) {
            //2 -> 4 -> 8,位运算从右往左跳
            if (((k >> i) & 1) > 0) { // k 的二进制从低到高第 i 位是 1
                node = arr[node][i];
                if (node < 0) break;
            }
        }

        return node;
    }

    public static int flp(int n) {
        //将最高位1右边所有低位都置为1
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        //n包括最高位1和右边都是1，减去右移1，得到最高位1
        return n - (n >> 1);
    }


}
