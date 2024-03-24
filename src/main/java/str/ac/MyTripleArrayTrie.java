package str.ac;

import java.util.*;

/**
 * 三数组字典树
 * check[base[s] + c] = s
 * next[base[s] + c] = t
 * 结尾next[base[s] + c] = -1
 */
public class MyTripleArrayTrie {

    private int[] base;
    private int[] check;
    private int[] next; // 用于存储同一层级中下一个兄弟节点的索引

    //目前数组大小
    private int allocSize;

    //最大有值的下标,这里记录的是next和check数组中有值的最大索引，
    // 其实base数组可以用另一字段维护
    private int maxIndex;

    private int nextBasePos;

    private int nextCheckPos;

    //65536 hash是UTF16
    private static final int HASH_SIZE = 1 << 16;

    //32 * 65536 内存对齐
    private static final int RSIZE =  HASH_SIZE * 32;

    private boolean degBug = false;


    public MyTripleArrayTrie(TreeSet<String> treeSet) {
//        resize(16);
        resize(RSIZE);

        this.base[0] = 1;

        treeSet.forEach(this::add);

        shrink();
    }

    // 查询字符串的方法
    public boolean search(String word) {
        int s = 0;

        for (int i = 0; i < word.length(); i++) {
            //base[s] + c
            int index = base[s] + (int) word.charAt(i) + 1;

            if (check[index] != s) {
                // 不存在该字符串
                return false;
            }

            //next[base[s] + c] = t
            s = next[index];
        }


        int p = base[s] + 1;

        //base[next[base[s] + 0]] = t
        int n = next[p]; //查询base

        //check[base[s] + 0] = base[s]
        if (s == check[p] && n < 0){ //状态转移成功且对应词语结尾
//            int index = -n - 1; //获得字典序
            return true;
        }
        // 检查是否到达字符串末尾
        return false;
    }

    // 添加字符串的方法
    public void add(String word) {
        if (word == null || word.trim().length() == 0){
            return;
        }

        int s = 0;
        word += "\0";
        int preIndex = 0;

        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i);

            //t  = base[s] + c
            if (base[s] == 0){
                //找begin
                int b = this.getB(Collections.singletonList(c));
                base[s] = b;
            }

            int index = base[s] + c + 1;

            if (allocSize <= index){
                resize(index + 1);
            }


            if (check[index] == -1) {
                int t = nextBasePos++;
                //next[base[s] + c] == 0 ,不冲突
                //check[base[s] + c] = s
                check[index] = s;
//                base[t] = b;

                if (preIndex != 0){
                    //next[base[s] + c] = t
                    //得记录下一次
                    next[preIndex] = t;
                }

                preIndex = index;

                s = nextBasePos;
            }
            //next[base[s] + c] != 0 ,冲突
            else if (check[index] != s){


                //s 和 check[index] 争取 c字符
                this.conflict(s, check[index], c);

                //分配完在执行
                int t = nextBasePos++;

                index = base[s] + c + 1;


                //check[base[s] + c] = s
                check[index] = s;


                //next[base[s] + c] = t
                if (preIndex != 0){
                    //next[base[s] + c] = t
                    //得记录下一次
                    next[preIndex] = t;
                }

                preIndex = index;

                s = nextBasePos;
            }
            //next[base[s] + c] == 0 ,共同前缀
            else {

                s = next[index];

            }


            if (index > maxIndex){
                maxIndex = index;
            }

        }

        next[preIndex] = -1;

//        if (flag.equals("原神芙宁娜贰JR1")){
//            degBug = true;
//        }
//        if (degBug && !this.search2("原神芙宁娜贰JR1")){
//            System.out.println(flag);
//        }
    }

    private void conflict(int s, int p,int c) {
        //证明需要重新
        //父节点的子节点字符
        List<Integer> parCList = this.getC(p);
        //当节点的子节点
        List<Integer> curCList = this.getC(s);

        List<Integer> smallList;
        int smallS;
        //先要找 begin
        int b;
        if (parCList.size() >= curCList.size()){
            smallList = curCList;
            smallS = s;

            smallList.add(c);
            b = this.getB(smallList);
            smallList.remove(smallList.size()-1);
        }else {
            smallList = parCList;
            smallS = p;

            b = this.getB(smallList);
        }

        //重新分配
        relocate(smallS,b,smallList);
    }


    /**
     * 找check[begin + a1...an]  == 0的n个空闲空间
     * @return begin
     */
    public int getB(List<Integer> siblings){

        int pos = Math.max(siblings.get(0) + 1, nextCheckPos + 1) - 1;


        int begin = 1;

        outer:
        // 此循环体的目标是找出满足check[begin + a1...an]  == 0的n个空闲空间,a1...an是siblings中的n个节点
        while (true) {
            pos++;

            if (allocSize <= pos){
                resize(pos + 1);
            }

            //check[pos] 是第一个下标的选取，如果直接有值，直接重找
            if (check[pos] != -1) {
                continue;
            }

            //这里为什么要选择第一个节点作为基准点，是因为双数组的设计就是为了让结构更加紧凑，如果选择最后一个节点做基准点，
            // 肯定冲突更少，也更容易找到，但是结构就没那么紧凑了，pos代表第一个兄弟节点尝试check[pos]
            begin = pos - siblings.get(0); // 当前位置离第一个兄弟节点的距离

            //距离加上最后一个节点 （最长距离）大于等于目前 数组大小，进行扩容65535
            if (allocSize <= (begin + siblings.get(siblings.size() - 1) + 1)) {
                resize(begin + siblings.get(siblings.size() - 1) + 1 + Character.MAX_VALUE);
            }

            //满足check[begin + a1...an]  == 0
            for (Integer sibling : siblings){
                if (check[begin + sibling + 1] != -1){
                    continue outer;
                }
            }

            break;
        }


        nextCheckPos = pos;

        return begin;

    }


    /**
     *
     * s 字符，b 新下标
     * Procedure Relocate(s : state; b : base_index)
     * { Move base for state s to a new place beginning at b }
     * begin
     *     foreach input character c for the state s
     *     { i.e. foreach c such that check[base[s] + c]] = s }
     *     begin
     *     check[b + c] := s; { mark owner }
     *     next[b + c] := next[base[s] + c]; { copy data }
     *     check[base[s] + c] := none { free the cell }
     *     end;
     *     base[s] := b
     * end
     */
    private void relocate(int s,int b,List<Integer> siblings) {
        //1。 找s的所有后缀

        for (Integer c : siblings) {
            check[b + c + 1] = s;
            next[b + c + 1] = next[base[s] + c + 1];
            check[base[s] + c + 1] = -1;
//            next[base[s] + c + 1] = 0;
        }

        //  重新分配后如果最大的索引大于maxIndex
        if (b + siblings.get(siblings.size() - 1) + 1 > maxIndex){
            maxIndex = b + siblings.get(siblings.size() - 1) + 1;
        }

        base[s] = b;
    }


    private List<Integer> getC(int s){
        List<Integer> list = new ArrayList<>();
        int bs = base[s];
        for (int i = bs - 1; i <= maxIndex; i++) {
            if (check[i] != s){
                continue;
            }
            int c = i - bs - 1;
            list.add(c);
        }
        return list;
    }



    /**
     * 数组扩容方法
     * @param size 新数组大小
     */
    private void resize(int size){
        int[] newBase = new int[size];
        int[] newNext = new int[size];
        int[] newCheck = new int[size];


        Arrays.fill(newCheck, -1);

        if (allocSize > 0) {
            //如果已经初始化过了，就要迁移
            System.arraycopy(base, 0, newBase, 0, allocSize);
            System.arraycopy(check, 0, newCheck, 0, allocSize);
            System.arraycopy(next, 0, newNext, 0, allocSize);
        }

        base = newBase;
        check = newCheck;
        next = newNext;

        allocSize = size;
    }


    /**
     * 释放空闲的内存
     */
    private void shrink() {
        int len = maxIndex + 1;

        int[] nbase = new int[maxIndex + 65535];
        System.arraycopy(base, 0, nbase, 0, len);
        base = nbase;

        int[] ncheck = new int[maxIndex + 65535];
        System.arraycopy(check, 0, ncheck, 0, len);
        check = ncheck;


        int[] nNext = new int[maxIndex + 65535];
        System.arraycopy(next, 0, nNext, 0, len);
        next = nNext;
    }




}
