package str.sam;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.*;

/**
 * 后缀自动机
 * 五要素:字符集、状态集、状态转移函数、开始、结束
 *
 * 状态集：指的是endpos集合，即对应子串结束位置集合，例子aabbaa aa:{2,6}
 * 状态转移函数: trans[u][c]:u状态加上c字符后转移的状态,如果为null证明该状态不在后缀自动机中
 * 开始:init
 * 结束:end
 */


public class SAM {

    /**
     * 节点个数
     */
    private int size;

    /**
     * 上一个更新位置
     */
    private int last;

    /**
     * index 是计数size，值 对应最大长度。
     * 最小长度是 minLen = maxLen(当前节点父节点) + 1，
     *
     */
    private List<Integer> maxLen = new ArrayList<>();

    /**
     * 后缀链接，用来endpos集合长度的连接
     * index 是计数size，值 对应下个节点的计数。
     */
    private List<Integer> link = new ArrayList<>();

    /**
     * 状态转移函数trans[v][c] = q,v状态加c等于q状态
     * index 是计数size，Map key 字符 value转移状态计数size。
     */
    private List<Map<Character,Integer>> trans = new ArrayList<>();


    /**
     * 增量法添加字符
     *
     * @param c 字符
     */
    public void extend(char c){
        //当前状态，也是新的终止节点
        int z = ++size;
        //新节点 最长长度是上一个节点 + 1 ，因为是在后面增加字符
        maxLen.set(z,maxLen.get(last) + 1);

        /**
         * 情况1 trans[v][c] = Null (v状态转移到c，重来没有过直接让trans[v][c] 指向 终止节点z即可)
         * 对于suffix_path(u -> S)的任意状态v,都有trans[v][c]=Null
         * 这时我们只要令trans[v][c] = z,并且令link[z] = s即可
         */
        Integer v;

        /**
         * v默认是之前终止节点，如果满足 不是第一次且trans[v][c] = Null 就trans[v][c] = z
         * 接着link[v]找短的子串执行
         *
         * 问题：为什么找到第一个trans[v][c] != Null 就行
         * 原因: trans[start...v-1][c] trans[v...w][c] trans[w+1...end][c]
         *      1。首先如果下标trans[w][c] != Null，则所有tran[1...w][c] != null，因为如果后面有一个满足trans[v][c] == Null，则该vc后缀一直不存在，违反后缀自动机构造
         *      2。trans[start...v-1][c]这部分节点会经过link(trans[v...w][c])找到，因为我们只是拆成两个 -> 1 ->  变成 -> 1 -> 2 -> 头尾箭头是不变的
         */
        for (v = last; v != null && (MapUtils.isEmpty(trans.get(v)) || trans.get(v).get(c) == null); v = link.get(v)) {

            Map<Character, Integer> map = trans.get(v);
            if (MapUtils.isEmpty(map)){
                map = new HashMap<>();
            }
            map.put(c,z);

            trans.set(v,map);
        }

        //第一次进来，直接link[z] = init , last = z 就行
        if (v == null){
            link.set(z,1);
            last = z;
            return;
        }

        //情况2 trans[v][c] != Null
        //x 已经存在的节点
        int x = trans.get(v).get(c);

        /**
         * 情况2.1
         * x中包含的最长子串就是 v中包含的最长子串接上字符c
         * 即：maxlen(v) + 1 = maxlen(x) ,
         * z的后缀链接恰好是x。
         *
         * 只要增加link[z] = x; 只要把link后缀指向该x就行，这样link链接就连续了。
         *
         */

        if (maxLen.get(v) + 1 == maxLen.get(x)){
            //x存在x的后缀一定存在
            link.set(z,x);
            last = z;
            return;
        }

        /**
         * 情况2.2
         * maxlen(p) + 1 < maxlen(x)
         * 这种情况是 x状态节点中有多个且更长的子串 x {lt eq gt} -> y{lt eq} x{gt} 需要拆成两块
         * 并让 trans[v][c] -> y{lt eq}
         * y{lt eq} -> x {lt eq gt} (y的下一个节点指向原来x的下一个节点)
         * link[y] = link[x] ,link[x] = link[z] = y
         */
        //多个节点，因为复制的原因
        int cl = ++size;
        //
        maxLen.set(cl,maxLen.get(v) + 1);
        trans.set(cl,trans.get(x));

        link.set(cl,link.get(x));

        while (v != null && trans.get(v).get(c) == x){

            trans.get(v).put(c,cl);
            v = link.get(v);
        }

        link.set(x,cl);
        link.set(z,cl);
        last = z;
    }




}
