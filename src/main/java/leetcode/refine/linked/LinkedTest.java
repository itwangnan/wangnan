package leetcode.refine.linked;

import org.junit.Test;

public class LinkedTest {


    @Test
    public void leetCode206(){


        Linked<Integer> cur = this.getLinked(6);

        println(cur);

        System.out.println("-------------");
        //反转
        Linked<Integer> pre = null;
        while (cur != null){
            //缓存下原本的下一个
            Linked<Integer> nxt = cur.next;

            cur.next = pre;
            pre = cur;

            cur = nxt;
        }

        println(pre);

    }

    private void println(Linked<Integer> linked) {
        while (linked != null){
            System.out.println(linked.value);
            linked = linked.next;
        }
    }

    private Linked<Integer> getLinked(int end) {
        Linked<Integer> pre = null;
        Linked<Integer> first = null;
        for (int i = 1; i <= end ; i++) {
            Linked<Integer> cur = new Linked<>();
            cur.value = i;
            if (pre != null){
                pre.next = cur;
            }else {
                first = cur;
            }

            pre = cur;
        }
        return first;
    }


    @Test
    public void leetCode92(){
        //https://leetcode.cn/problems/reverse-linked-list-ii/description/

        int left = 1;
        int right = 3;

        Linked<Integer> cur = this.getLinked(6);

        println(cur);

        System.out.println("-------------");

        //反转

        Linked<Integer> res = this.reverse(left,right,cur);
        println(res);

    }

    private Linked<Integer> reverse(int left, int right, Linked<Integer> cur) {
        Linked<Integer> dummy = new Linked<>();
        dummy.next = cur;

        //先创建新头节点，为了避免left直接等于头节点
        Linked<Integer> start = dummy;

        //执行left - 1次找到left前一个节点start，start.next要等于pre(最后一个right的节点),start.next.next指向cur(最后一个right的节点的下一个节点)
        for (int i = 0; i < left - 1; i++){
            start = start.next;
        }

        cur = start.next;

        Linked<Integer> pre = null;
        for (int i = 0; i < right - left + 1; i++) {
            Linked<Integer> nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }

        start.next.next = cur;
        start.next = pre;

        //最后返回真头节点
        return dummy.next;
    }


    @Test
    public void leetCode25(){
        //https://leetcode.cn/problems/reverse-nodes-in-k-group/description/

        int count = 3;

        Linked<Integer> cur = this.getLinked(6);

        println(cur);

        System.out.println("-------------");

        //反转

        Linked<Integer> res = this.reverseCount2(count,cur);
        println(res);

    }

    private Linked<Integer> reverseCount(int count, Linked<Integer> cur) {
        Linked<Integer> dummy = new Linked<>();
        dummy.next = cur;

        //先创建新头节点，为了避免left直接等于头节点
        Linked<Integer> start = dummy;

        Linked<Integer> pre = null;
        flag:while (start.next != null){

            Linked linked = start.next;

            for (int i = 0; i < count; i++) {
                linked = linked.next;
                if (linked == null){
                    break flag;
                }
            }

            cur = start.next;
            for (int i = 0; i < count; i++) {
                Linked<Integer> nxt = cur.next;
                cur.next = pre;
                pre = cur;
                cur = nxt;
            }
            Linked sn = start.next;
            start.next.next = cur;
            start.next = pre;

            start = sn;
        }


        //最后返回真头节点
        return dummy.next;
    }


    private Linked<Integer> reverseCount2(int count, Linked<Integer> cur) {
        Linked<Integer> dummy = new Linked<>();
        dummy.next = cur;

        //先创建新头节点，为了避免left直接等于头节点
        Linked<Integer> start = dummy;

        int sum = this.count(cur);

        Linked<Integer> pre = null;
        //int j = sum; j >= count; j -= count 如果是余下的不处理，因为 5 - 3 = 2 < 3
        //int j = 0; j < sum; j += count      如果是全部处理   , 因为 0 + 3 + 3 = 6 > 5
        for (int j = sum; j >= count; j -= count) {

            for (int i = 0; i < count; i++) {
                Linked<Integer> nxt = cur.next;
                cur.next = pre;
                pre = cur;
                cur = nxt;
            }

            //start要反转的前一个节点
            //为什么start.next是下次的start，因为start后面反转完则原本start.next会是反转后的最后一个，所以指向就行
            Linked<Integer> nxt = start.next;

            start.next.next = cur;
            start.next = pre;

            start = nxt;
        }


        //最后返回真头节点
        return dummy.next;
    }

    private int count(Linked<Integer> cur) {
        int i = 0;
        while (cur != null){

            i++;

            cur = cur.next;
        }

        return i;
    }

    @Test
    public void leetCode876(){
        //https://leetcode.cn/problems/middle-of-the-linked-list/description/

        Linked<Integer> cur = this.getLinked(9);
        System.out.println(this.quickSlow(cur));
        cur = this.getLinked(10);
        System.out.println(this.quickSlow(cur));
    }

    private Integer quickSlow(Linked<Integer> cur) {

        Linked<Integer> start = cur;
//        while (start.next != null){
//            cur = cur.next;
//
//            start = start.next;
//            if (start.next != null){
//                start = start.next;
//            }
//        }

        while (start != null && start.next != null){
            cur = cur.next;
            start = start.next.next;
        }

        return cur.value;
    }


    @Test
    public void leetCode141(){
        //https://leetcode.cn/problems/linked-list-cycle/description/

        Linked<Integer> cur = this.getLinked(5);
        Linked flag = cur.next;
        Linked<Integer> head = cur;
        while (head.next != null){
            head = head.next;
        }
        head.next = flag;
        System.out.println(this.isRing(cur));
    }

    private boolean isRing(Linked<Integer> cur) {

        Linked<Integer> one = cur;

        Linked<Integer> two = cur;
        while (two != null && two.next != null){

            one = one.next;
            two = two.next.next;

            if (one == two){
                return true;
            }
        }


        return false;
    }


    @Test
    public void leetCode142(){
        //https://leetcode.cn/problems/linked-list-cycle-ii/solutions/1999271/mei-xiang-ming-bai-yi-ge-shi-pin-jiang-t-nvsq/
        //1。分三段,a是到循环开始节点距离，b是循环开始节点到碰撞点距离，c碰撞点重新回到循环开始节点。b+c的循环长度为n
        //2。b不会大于等于n，因为快指针刚好在慢指针前，只需要n-1步就可以到达，（在循环里，可以把慢指针看作不动，快指针步长为1）
        //则 2a + 2b = a + b + k(b + c)
        //a = c + (k-1)(b+c)
        //代表着如果从头节点开始和碰撞点开始，直到产生新碰撞则是a点
        //利用了当快慢指针碰撞时，

        Linked<Integer> cur = this.getLinked(4);
//        Linked flag = cur.next.next;
//        Linked<Integer> head = cur;
//        while (head.next != null){
//            head = head.next;
//        }
//        head.next = flag;

        System.out.println(this.isRing2(cur));
    }


    private int isRing2(Linked<Integer> cur) {

        Linked<Integer> one = cur;

        Linked<Integer> two = cur;
        while (two != null && two.next != null){

            one = one.next;
            two = two.next.next;

            if (one == two){
                Linked<Integer> head = cur;
                int i = 0;
                while(head != one){
                    i++;
                    head = head.next;
                    one = one.next;
                }
                return i;
            }
        }


        return -1;
    }

    @Test
    public void leetCode143(){
        //https://leetcode.cn/problems/reorder-list/solutions/1999276/mei-xiang-ming-bai-yi-ge-shi-pin-jiang-t-u66q/
        //思路:1。先找到中间点mid，快慢指针
        //    2。反转mid-end, 1->2->3<-4<-5,如何在交换

        Linked<Integer> linked = this.getLinked(4);

        Linked<Integer> quick = linked;
        Linked<Integer> slow = linked;
        while (quick != null && quick.next != null){
            quick = quick.next.next;
            slow = slow.next;
        }

        Linked<Integer> cur = slow;

        Linked<Integer> pre = null;
        while (cur != null){
            Linked nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }

        //cur 到尾

        Linked<Integer> head = linked;
        Linked<Integer> tail = pre;

        while (tail != null && tail.next != null){

            Linked hNext = head.next;
            Linked TNext = tail.next;

            head.next = tail;
            tail.next = hNext;

            head = hNext;
            tail = TNext;
        }

        println(linked);
    }

    @Test
    public void leetCode237(){
        //https://leetcode.cn/problems/delete-node-in-a-linked-list/description/

        Linked<Integer> linked = this.getLinked(2);

        Linked<Integer> head = linked;
        while (head != null && head.next != null){
            head = head.next;
        }

        head.next = new Linked<>(3,null);


        //现在给删除节点 进行删除
        Linked<Integer> next = head.next;

        head.value = next.value;

        head.next = next.next;


        println(linked);
    }


    @Test
    public void leetCode97(){

        //https://leetcode.cn/problems/remove-nth-node-from-end-of-list/description/

        int n = 1;
        //1
        Linked<Integer> linked = this.getLinked(6);
//        int sum = this.count(linked);
//
//        Linked<Integer> dummy = new Linked<>(null,linked);
//
//        int index = sum - flag;
//
//
//
//        Linked<Integer> head = linked;
//        Linked<Integer> pre = dummy;
//        while (index > 0){
//
//            pre = head;
//            head = head.next;
//            index--;
//        }
//
//        pre.next = head.next;
//
//        println(dummy.next);

        //第二种做法先让right走n个节点，然后left和right同时往尾巴靠近，最后right到达的时候，left就是倒数第n个

        Linked<Integer> dummy = new Linked<>(null,linked);

        Linked<Integer> right = dummy;
        for (int i = 0; i < n; i++) {
            right = right.next;
        }

        Linked<Integer> left = dummy;

        while (right.next != null){
            right = right.next;
            left = left.next;
        }

        //现在left就是倒数第n+1个节点
        left.next = left.next.next;

        println(dummy.next);

    }


    @Test
    public void leetCode83(){

        //https://leetcode.cn/problems/remove-duplicates-from-sorted-list/description/

        Linked<Integer> linked = this.getLinked(6,2);
//        println(linked);
        Linked<Integer> cur = new Linked<>(0,linked);
        Linked<Integer> dummy = new Linked<>(null,cur);

        Linked<Integer> head = dummy;


        while (head.next != null && head.next.next != null){

            Integer value = head.next.value;
            if (value.equals(head.next.next.value)){
                //则有重复
                while (head.next != null && (value.equals(head.next.value))){
                    head.next = head.next.next;
                }
            }else {
                head = head.next;
            }
        }

        println(dummy.next);

    }

    private Linked<Integer> getLinked(int end,int count) {
        Linked<Integer> pre = null;
        Linked<Integer> first = null;
        for (int i = 1; i <= end ; i++) {

            for (int j = 0; j < count; j++) {
                Linked<Integer> cur = new Linked<>();
                cur.value = i;
                if (pre != null){
                    pre.next = cur;
                }else {
                    first = cur;
                }

                pre = cur;
            }

        }
        return first;
    }


    @Test
    public void leetCode82(){

        //https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/description/

        Linked<Integer> linked = this.getLinked(6,2);
//        println(linked);

        Linked<Integer> head = linked;

        //因为我删除是删除靠后的节点，所以就不用dummy了
//        Linked<Integer> pre = null;
        while (head.next != null){

            if (head.value.equals(head.next.value)){
                //则代表重复
                head.next = head.next.next;
            }else {
                head = head.next;
            }

        }

        println(linked);

    }

}
