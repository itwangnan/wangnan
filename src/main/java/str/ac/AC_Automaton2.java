package str.ac;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class AC_Automaton2 {

    static final int maxn = 3000001;

    static class TrieNode {
        int[] son = new int[26];
        int fail, flag, depth;
        int stat;

        void init() {
            Arrays.fill(son, 0);
            fail = flag = depth = 0;
        }
    }

    static TrieNode[] trie = new TrieNode[maxn];
    static Queue<Integer> q = new LinkedList<>();
    static int n, cnt, m;
    static int[] dp = new int[maxn];

    static void init() {
        for (int i = 0; i <= trie.length-1; i++) {
            trie[i] = new TrieNode();
            trie[i].init();
        }
        Arrays.fill(dp, 0);
        //次数默认是1
        cnt = 1;
    }

    static void insert(char[] s, int num) {
        int u = 1, len = s.length;

        for (int i = 0; i < len; i++) {
            if (s[i] == 0){
                continue;
            }
            int v = s[i] - 'a';

            if (trie[u].son[v] == 0) {
                cnt++;
                trie[cnt] = new TrieNode();
                trie[u].son[v] = cnt;
            }
            u = trie[u].son[v];
        }
        trie[u].flag = num;
    }

    static void getfail() {
        /**
         * 先设置根节点和对应子节点的fail
         */
        for (int i = 0; i < 26; i++) {
            trie[0].son[i] = 1;
        }
        q.add(1);
        trie[1].fail = 0;

        while (!q.isEmpty()) {
            //u第一次是1
            int u = q.poll();
            int Fail = trie[u].fail;
            //对状态的更新在这里 trie[1].stat = trie[0].stat
            trie[u].stat = trie[Fail].stat;

            //证明是结束状态
            if (trie[u].flag != 0) {
                //1 << trie[u].depth 对应长度位,
                trie[u].stat |= 1 << trie[u].depth;
            }
            for (int i = 0; i < 26; i++) {
                //后续节点
                int v = trie[u].son[i];
                if (v == 0) {
                    //没有值的就设置trie[Fail].son[i]
                    trie[u].son[i] = trie[Fail].son[i];
                } else {
                    //有值就需要
                    //前置节点的路径+1
                    trie[v].depth = trie[u].depth + 1;
                    //失效指针 指向trie[Fail].son[i]
                    trie[v].fail = trie[Fail].son[i];
                    //入队
                    q.add(v);
                }
            }
        }
    }

    static int query(char[] s) {
        int u = 1, mx = 0;
        int st = 1;
        for (int i = 0; i < s.length; i++) {
            int v = s[i] - 'a';
            //子节点index
            u = trie[u].son[v];
            //因为往下跳了一位每一位的长度都+1
            st <<= 1;
            //子节点状态 这里的 & 值是状压 dp 的使用，代表两个长度集的交非空
            if ((trie[u].stat & st) != 0) {
                //两个长度集的交非空 证明长度一致
                st |= 1;
                //最大长度 i+1
                mx = i + 1;
            }
        }
        return mx;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //模式串数
        n = scanner.nextInt();
        //文本串数
        m = scanner.nextInt();
        //初始化
        init();
        for (int i = 1; i <= n; i++) {
            String str = scanner.next();
            //
            insert(str.toCharArray(), i);
        }
        getfail();
        for (int i = 1; i <= m; i++) {
            String str = scanner.next();
            System.out.println(query(str.toCharArray()));
        }
    }
}
