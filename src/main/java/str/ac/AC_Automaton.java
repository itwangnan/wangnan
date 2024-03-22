package str.ac;

import java.util.*;

class AC_Automaton {

    static final int maxN = 200010;
    static final int maxS = 200010;
    static final int maxT = 2000010;

    static char[] s = new char[maxS + 10];
    static char[] t = new char[maxT + 10];
    static int[] cnt = new int[maxN + 10];

    /**
     * 这里是将关系绑定，但是所有的节点其实都在Node[] node里的
     */
    static class Node {
        //存的是子节点在Node[] node的下标
        int[] son = new int[30];
        //匹配次数
        int val;
        //fail node中的index
        int fail;
        int head;
        //输入的第几次，如果重复就会有多个
        Deque<Integer> index = new LinkedList<>();
    }

    static class Edge {
        int head;
        int next;
    }

    static Node[] node = new Node[maxS + 10];
    static Edge[] edge = new Edge[maxS + 10];

    static int root;
    static int ncnt;
    static int ecnt;

    static AhoCorasickAutomaton ACM = new AhoCorasickAutomaton();

    static class AhoCorasickAutomaton {
        //node[0] 根节点，node[0].son[1]=1、                                                             node[1].index+1(该节点对应终结节点，并且index记录是第几次的入参)
        //node[0] 根节点，node[0].son[2]=2、 node[2].son[2]=3                                            node[3].index+2
        //node[0] 根节点，node[0].son[1]有值1、node[1].son[1]=4                                           node[4].index+3
        //node[0] 根节点，node[0].son[1]有值1、node[1].son[2]=5、node[5].son[1]=6、node[6].son[1]=7       node[7].index+4
        // 插入模式串到 AC 自动机的 Trie 树中
        void Insert(char[] str, int i) {
            int u = root;
            for (int j = 1; j < str.length; j++) {
                //str[j] 对应的字符
                if(str[j] == 0){
                    continue;
                }
                int index = str[j] - 'a' + 1;
                //会将node数组
                Node node = AC_Automaton.node[u];
                if (node.son[index] == 0){
                    node.son[index] = ++ncnt;
                }
                u = node.son[index];
            }
            //第几次的信息
            node[u].index.add(i);
        }

        // 构建 AC 自动机的 Fail 指针
        //node[0] 根节点，node[0].son[1]=1、                                                             node[1].index+1(该节点对应终结节点，并且index记录是第几次的入参)
        //node[0] 根节点，node[0].son[2]=2、 node[2].son[2]=3                                            node[3].index+2
        //node[0] 根节点，node[0].son[1]有值1、node[1].son[1]=4                                           node[4].index+3
        //node[0] 根节点，node[0].son[1]有值1、node[1].son[2]=5、node[5].son[1]=6、node[6].son[1]=7       node[7].index+4
        void Build() {
            Deque<Integer> q = new ArrayDeque<>();
            for (int i = 1; i <= 26; i++) {
                int code = node[root].son[i];
                //将根节点下的子节点，有效的都放在q队列中
                if (code != 0) {
                    q.add(code);
                }
            }


            while (!q.isEmpty()) {
                //node[]下标
                int u = q.poll();
                for (int i = 1; i <= 26; i++) {
                    //u子节点 在node[]中的下标
                    int nodeIndex = node[u].son[i];
                    //u节点的fail指针，在node[]中的下标
                    int failNodeIndex = node[u].fail;
                    if (nodeIndex != 0) {
                        //子节点的fail <- fail节点的子节点
                        node[nodeIndex].fail = node[failNodeIndex].son[i];
                        q.add(nodeIndex);
                    } else {
                        //这步的作用 node[u].son[i]是不存在的，表示在匹配失败时应该回溯到node[failNodeIndex].son[i]
                        node[u].son[i] = node[failNodeIndex].son[i];
                    }
                }
            }
        }

        // 在 AC 自动机上进行文本匹配
        void Query(char[] str) {
            int u = root;
            for (int i = 1; i < str.length; i++) {
                if(str[i] == 0){
                    continue;
                }
                //这里就是直接匹配 匹配到的就将u 指向对应节点，如果没有匹配到的就会指向对应fail节点。
                u = node[u].son[str[i] - 'a' + 1];
                //对应节点匹配到的数量+1
                node[u].val++;
            }
        }

        // 添加边，构建 AC 自动机的 Fail 树

        /**
         * 添加边
         * @param tail failIndex    failfailIndex 都是node[]下标
         * @param head currentIndex failIndex     都是node[]下标
         */
        void addEdge(int tail, int head) {
            //边数+1
            ecnt++;
            //edge[] 就是fail数。只不过一维化了

            //fail树head -> nodeIndex
            //创建一条新的边，这条边的头指针是当前节点
            edge[ecnt].head = head;

            //fail树next -> nodeIndex的fail节点的头节点 默认是0
            //node[tail].head 表示节点 tail 的边链表的头指针
            //将这条新边的next指针指向 fail节点的边链表的head
            edge[ecnt].next = node[tail].head;

            //nodeIndex的fail节点的头节点 -> ecnt，
            //更新fail节点的边链表的头指针head，使其指向新添加的边，即
            node[tail].head = ecnt;

//            0  1 nil a
//            edge[1].head = 1
//            edge[1].next = node[0].head = 0;
//            node[0].head = 1;

//            0  2 nil b
//            edge[2].head = 2
//            edge[2].next = node[0].head = 1;
//            node[0].head = 2;

//            2  3  b b
//            edge[3].head = 3
//            edge[3].next = node[2].head = 0;
//            node[2].head = 3;

//            1  4  a a
//            edge[4].head = 4
//            edge[4].next = node[1].head = 0;
//            node[1].head = 4;

//            2  5  b b
//            edge[5].head = 5
//            edge[5].next = node[2].head = 3;
//            node[1].head = 5;

//            1  6 a a
//            edge[6].head = 6
//            edge[6].next = node[1].head = 4;
//            node[1].head = 6;

//            4  7 a a
//            edge[7].head = 7
//            edge[7].next = node[4].head = 0;
//            node[4].head = 7;


            /**
             * node中通过head节点可以获取到,ecnt下标，
             * 在通过edge[ecnt].head获取 nodeIndex               head是之前的叶子节点
             * 在通过edge[ecnt].next获取 fail节点对应的ecnt       fail节点
             */
        }

        // DFS 遍历 Fail 树，统计子树和
        void DFS(int u) {
            /**
             * 遍历 node[u].head 找到fail树ecnt  edge[e].next fail节点对应的ecnt
             */
            for (int e = node[u].head; e != 0; e = edge[e].next) {
                int v = edge[e].head;//head是之前的叶子节点 nodeIndex
                DFS(v);//继续找nodeIndex
                //u是上级的，v是下级的，u是v的后缀，所以匹配数要增加到上级
                node[u].val += node[v].val;
            }
            //i 代表输入第几次的信息
            //最后对应第几次的信息 计算总数
            for (int i : node[u].index) {
                cnt[i] += node[u].val;
            }
        }

        // 构建 Fail 树并统计子树和
        void FailTree() {
            //因为所有的节点都在node[]上，ncnt节点数，只需遍历一次即可
            for (int currentIndex = 1; currentIndex <= ncnt; currentIndex++) {
                //每一个u 都对应在node里有值
                //对应节点fail指针，对应在node中的索引
                int failIndex = node[currentIndex].fail;
                addEdge(failIndex, currentIndex);
            }
            DFS(root);
        }
    }

    public static void main(String[] args) {

        // 输入模式串数量
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        // 初始化数据结构
        for (int i = 0; i <= maxS; i++) {
            node[i] = new Node();
        }

        for (int i = 0; i <= maxS; i++) {
            edge[i] = new Edge();
        }

        // 插入模式串到 AC 自动机中
        for (int i = 1; i <= n; i++) {
            String str = scanner.next();
            str.getChars(0, str.length(), s, 1);
            ACM.Insert(s, i);
        }

        // 构建 AC 自动机的 Trie 树和 Fail 指针
        ACM.Build();

        // 输入待匹配的文本
        String text = scanner.next();
        text.getChars(0, text.length(), t, 1);

        // 在 AC 自动机上进行文本匹配
        ACM.Query(t);

        // 构建 Fail 树并统计子树和
        ACM.FailTree();

        // 输出每个模式串在文本中出现的次数
        for (int i = 1; i <= n; i++) {
            System.out.println(cnt[i]);
        }
    }

}
