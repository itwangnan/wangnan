package arr;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class Arr {


    public static void main(String[] args) {
        exec(Arrays.asList(1,2,3,5,4));
        exec(Arrays.asList(1,2,3,5,4,1,2,3,4,1,2,3));
        exec(Arrays.asList(1,2,3,5,4));
        exec(Arrays.asList(3,3,3,3,3));

    }


    public static void exec(List<Integer> list){
        System.out.println("输入"+ list);
        if (CollectionUtils.isEmpty(list)){
            System.out.println("输出 0,0");
            return;
        }

        int l = 0;
        int r = 0;
        Integer flag = null;
        boolean isStart = false;


        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Integer num = list.get(i);
            if (flag != null){
                if (flag + 1 == num ){
                    //证明开始增加了
                    if (!isStart){
                        l = (i - 1 < 0 ? 0 : i - 1);
                        isStart = true;
                    }
                    r = i;
                }else{

                    if (isStart){
                        //一段结束 继续
                        isStart = false;
                        nodes.add(new Node(l,r));
                    }
                }
            }

            flag = num;
        }

        if (isStart){
            isStart = false;
            nodes.add(new Node(l,r));
        }


        Optional<Node> optional = nodes.stream().sorted(Comparator.comparing(Node::getLen).reversed()).findFirst();
        if (optional.isPresent()){
            Node node = optional.get();
            int i = node.r + 1;
            System.out.println("输出:"+node.l+","+i);
        }else {
            System.out.println("输出 0,0");
        }

    }

    static class Node{
        int l;
        int r;
        int len;

        public Node(int l, int r) {
            this.l = l;
            this.r = r;
            this.len = r - l;
        }

        public int getLen() {
            return len;
        }
    }
}
