package str.ac;

import java.util.*;

public class AC_Automaton3 {

    static class TrieNode {
        int[] son;
        int fail, len;
        boolean flag;

        TrieNode(int len) {
            son = new int[26];
            this.len = len;
            fail = 0;
            flag = false;
        }
    }

    static List<TrieNode> trie = new ArrayList<>();
    static int[] dp;

    static void init(int n) {
//        trie = new TrieNode[n + 2];
        dp = new int[n + 2];
        for (int i = 0; i <= n + 1; i++) {
            trie.add(new TrieNode(0));
        }
    }

    static void insert(String s, int num) {
        int u = 1, len = s.length();
        for (int i = 0; i < len; i++) {
            int v = s.charAt(i) - 'a';
            if (trie.get(u).son[v] == 0) {
                trie.get(u).son[v] = trie.size();
                trie.add(new TrieNode(i + 1));
            }
            u = trie.get(u).son[v];
        }
        trie.get(u).flag = true;
    }

    static void buildFail() {
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            if (trie.get(1).son[i] != 0) {
                q.add(trie.get(1).son[i]);
            }
        }

        while (!q.isEmpty()) {
            int u = q.poll();

            for (int i = 0; i < 26; i++) {
                int v = trie.get(u).son[i];
                if (v != 0) {
                    trie.get(v).fail = trie.get(trie.get(u).fail).son[i];
                    q.add(v);
                } else {
                    trie.get(u).son[i] = trie.get(trie.get(u).fail).son[i];
                }
            }
        }
    }

    static void query(String s) {
        int u = 1, len = s.length(), l = 0;

        for (int i = 0; i < len; i++) {
            int v = s.charAt(i) - 'a';
            int k = trie.get(u).son[v];

            while (k > 1) {
                if (trie.get(k).flag && (dp[i - trie.get(k).len] > 0 || i - trie.get(k).len == -1)) {

                    dp[i] = dp[i - trie.get(k).len] + trie.get(k).len;
                }
                k = trie.get(k).fail;
            }
            u = trie.get(u).son[v];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        init(n);

        for (int i = 1; i <= n; i++) {
            String str = scanner.next();
            insert(str, i);
        }

        buildFail();

        for (int i = 1; i <= m; i++) {
            String str = scanner.next();
            query(str);
            System.out.println(Arrays.stream(dp).max().orElse(0));
        }
    }
}
