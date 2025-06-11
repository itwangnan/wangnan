package leetcode;

import org.junit.Test;


/**
 * 并查集
 */
public class LeetCode1971 {

    private int[] arr;

    @Test
    public void test() {
        int[][] edges = {{0, 7}, {0, 8}, {6, 1}, {2, 0}, {0, 4}, {5, 8}, {4, 7}, {1, 3}, {3, 5}, {6, 5}};
        System.out.println(this.validPath(10, edges, 7, 5));
    }


    public boolean validPath(int n, int[][] edges, int source, int destination) {
        if (source == destination) {
            return true;
        }
        arr = new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        for (int i = 0; i < edges.length; i++) {
            int[] edge = edges[i];
            add(edge[0], edge[1]);
        }

        return select(source, destination);
    }

    public boolean select(int a, int b) {
        return find(a) == find(b);
    }


    /**
     * 注意这里的find递归回溯不能在select中改成arr[a] == arr[b]，创建是将当前节点和父节点都设置成根节点，
     * 但是子节点没更新，所以查询时也得链路一纬化
     * @param a
     * @return
     */
    public int find(int a) {
        if (a == arr[a]) {
            return a;
        }

        //这步将当前节点和父节点都设置成根节点
        arr[a] = find(arr[a]);
        return arr[a];
    }

    public void add(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) {
            return;
        }
        arr[b] = a;
    }


}
