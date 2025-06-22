package arr;

import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 基数排序
 * 如果桶里是链表实现，O(d * (n + r)) ,n是数组放到对应桶里（分配） + r每个桶里元素拿出来（收集），d最大数字长度（while循环次数）
 * 数组实现O(d * n)
 */
public class RadixSort {

    class Node{
//        Node pre;
        Node post;
        String str;

        public Node(String str) {
            this.str = str;
        }
    }

    class Linked{
        Node head;
        Node tail;

        public void add(Node node){
            if(head == null){
                head = node;
                tail = node;
            }else {
                tail.post = node;
                tail = node;
            }

        }

        public void add(Linked linked){
            if(head == null){
                head = linked.head;
                tail = linked.tail;
            }else {
                tail.post = linked.head;
                tail = linked.tail;
            }

        }

        public Node head(){
            return head;
        }

        @Override
        public String toString() {
            Node node = head;
            if (node == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            while (node != null) {
                sb.append(node.str).append(",");
                node = node.post;
            }
            sb.delete(sb.length()-1, sb.length());
            return sb.toString();
        }
    }

    @Test
    public void test() {

//        System.out.println(Integer.valueOf("345".substring(2,3)));
//        System.out.println(Integer.valueOf("345".substring(1,2)));
//        System.out.println(Integer.valueOf("345".substring(0,1)));
        int[] arr = {348, 425, 162, 8, 425, 763, 402, 169, 51, 592};
        sort2(arr);
        System.out.println(Arrays.toString(arr));
    }
    private void sort2(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }

        int max = nums[0];
        for (int num : nums) {
            max = Math.max(num,max);
        }

        int exp = 1;

        int[] buffer = new int[nums.length];

        while (max / exp > 0){
            int[] count = new int[10];
            // 2. 分配：统计每个桶中的元素数量
            for (int num : nums) {
                int digit = (num / exp) % 10;
                count[digit]++;
            }

            // 3. 前缀和：确定每个元素应该放在哪个位置，因为是升序所以 0 1 2 3 4 5 每个顺序都是前者加上自身数量的位置
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            // 4. 收集，从后往前遍历原数组，将元素放入 buffer 中（确保稳定性）
            /**
             * 例子 [101, 211, 121]
             * 第一位1，count[1] = 3，可以发现从后往前才能保证 顺序不变 121还在最后
             */
            for (int i = nums.length - 1; i >= 0; i--) {
                int digit = (nums[i] / exp) % 10;
                //[0,n-1]下标
                --count[digit];
                buffer[count[digit]] = nums[i];

            }

            // 5. 将 buffer 拷贝回原数组
            System.arraycopy(buffer, 0, nums, 0, nums.length);

            exp *= 10;
        }


    }


    private void sort(int[] arr) {
        Linked all = new Linked();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            String s = String.valueOf(arr[i]);
            all.add(new Node(s));
            len = Math.max(len, s.length());
        }
        Linked[] listArr = new Linked[10];

        //根据最长的数字 从小到大
        for (int i = 0; i < len; i++) {

            //分配
            Node node = all.head;
            while (node != null) {
                String str = node.str;
                Integer idx = str.length() <= i ? 0 : Integer.valueOf(str.substring(str.length() - i - 1,str.length() - i));
                if (listArr[idx] == null) {
                    listArr[idx] = new Linked();
                }
                listArr[idx].add(new Node(str));
                node = node.post;
            }


            all = new Linked();
            //收集
            for (int j = 0; j < listArr.length; j++) {
                Linked linked = listArr[j];
                if (linked == null){
                    continue;
                }
                all.add(linked);
                listArr[j] = null;
            }
            System.out.println(all);
        }


    }
}
