package str.ac;

import org.apache.commons.collections.MapUtils;

import java.util.*;

/**
 * Ac自动机
 */
public class AcTree {

    private AcNode root;

    public AcTree() {
        this.root = new AcNode("");
    }


    public AcTree add(List<String> patterns){
        for (String pattern : patterns) {
            add(pattern);
        }
        return this;

    }


    //字典树增加
    public void add(String pattern) {
        AcNode node = root;

        StringBuilder sb = new StringBuilder();
        for (char c : pattern.toCharArray()) {

            sb.append(c);
            Map<Character, AcNode> map = node.map;

            if (map.get(c) == null) {
                AcNode children = new AcNode();
                map.put(c,children);
            }

            node = map.get(c);
        }

        node.isEnd = true;
        node.value = sb.toString();
    }

    // 构建失败指针
    public void buildFailurePointer() {
        AcNode root = this.root;
        Queue<AcNode> queue = new LinkedList<>();

        root.map.forEach((x,y) -> {
            if (y != null) {
                queue.offer(y);
                y.fail = root;
            }
        });

        //层次遍历
        while (!queue.isEmpty()) {

            AcNode current = queue.poll();
            Map<Character, AcNode> map = current.map;
            if (MapUtils.isEmpty(map)){
                continue;
            }


            map.forEach((x,y) -> {

                queue.offer(y);

                AcNode failPointer = current.fail;

                Map<Character, AcNode> failPointerMap = failPointer.map;

                while (failPointer != null && failPointerMap != null && failPointerMap.get(x) == null) {
                    failPointer = failPointer.fail;
                }

                if (failPointer == null || failPointerMap == null){
                    y.fail = this.root;

                }else {
                    y.fail = failPointerMap.get(x);
                }

            });

        }
    }

    //匹配
    public List<String> match(String text) {
        AcNode current = root;
        List<String> res = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (current == null){
                current = root;
            }

            while (current != root && current.map.get(c) == null) {
                current = current.fail;
            }

            current = current.map.get(c);


            // 打印匹配的所有模式串
            if (current != null && current.isEnd) {
                AcNode temp = current;
                while (temp != null && temp != root) {
                    if (temp.isEnd) {
                        String s = temp.value;
                        res.add(s);
                    }
                    temp = temp.fail;
                }
            }

        }

        return res;
    }


    public class AcNode {

        /**
         * 是否结束
         */
        boolean isEnd;


        /**
         * 失效指针
         */
        AcNode fail;

        /**
         * 截止节点保存数据
         */
        String value;

        /**
         * 后续节点
         */
        Map<Character, AcNode> map;

        public AcNode(String value) {
            this.value = value;
            this.isEnd = true;
            this.map = new HashMap<>();
        }

        public AcNode() {
            this.isEnd = false;
            this.map = new HashMap<>();
        }



    }


}
