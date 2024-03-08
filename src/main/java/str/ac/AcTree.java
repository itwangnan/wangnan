package str.ac;

import org.apache.commons.collections.MapUtils;

import java.util.*;

/**
 * Ac自动机
 */
public class AcTree {

    private AcNode root;

    public AcTree() {
        this.root = new AcNode(' ',1);
    }


    public AcTree add(List<String> patterns){
        for (String pattern : patterns) {
            add(pattern);
        }
        return this;

    }


    public void add(String pattern) {
        AcNode node = root;
        int level = 1;
        StringBuilder sb = new StringBuilder();
        for (char c : pattern.toCharArray()) {
            level++;
            sb.append(c);
            Map<Character, AcNode> map = node.getMap();
            if (map != null && map.get(c) == null) {
                AcNode children = new AcNode(c,level);
//                children.setLevel(level);
//                children.setPattern(sb.toString());
                map.put(c,children);
            }
            node = map.get(c);
        }
        node.setEnd(true);
    }


//    public void build() {
//        for (int i = 0; i < 26; i++)
//            if (tr[0][i]) q.push(tr[0][i]);
//        while (q.size()) {
//            int u = q.front();
//            q.pop();
//            for (int i = 0; i < 26; i++) {
//                if (tr[u][i])
//                    fail[tr[u][i]] = tr[fail[u]][i], q.push(tr[u][i]);
//                else
//                    tr[u][i] = tr[fail[u]][i];
//            }
//        }
//    }

    // 构建失败指针
    public void buildFailurePointer() {
        AcNode root = this.root;
        Queue<AcNode> queue = new LinkedList<>();

        root.getMap().forEach((x,y) -> {
            if (y != null) {
                queue.offer(y);
                y.setFail(root);
            }
        });


        while (!queue.isEmpty()) {
            AcNode current = queue.poll();
            Map<Character, AcNode> map = current.getMap();
            if (MapUtils.isEmpty(map)){
                continue;
            }


            map.forEach((x,y) -> {

                queue.offer(y);

                AcNode failPointer = current.getFail();
                Map<Character, AcNode> failPointerMap = failPointer.getMap();


                while (failPointer != null && failPointerMap != null && failPointerMap.get(x) == null) {
                    failPointer = failPointer.getFail();
                }
                y.setFail(failPointer != null ? failPointerMap.get(x) : this.root);


            });

        }
    }

    public void match(String text) {
        AcNode current = root;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            while (current != null && current.getMap().get(c) == null) {
                current = current.getFail();
            }

            if (current == null) {
                current = root;
                continue;
            }
            current = current.getMap().get(c);
            sb.append(current.getValue());

            // 匹配到模式串
            if (current.isEnd()) {
                int level = current.getLevel();
                String s = sb.toString();
//                System.out.println("Found pattern: " + s.substring(s.length() - level + 1));
            }

//            // 打印匹配的所有模式串
//            AcNode temp = current;
//            while (temp != root) {
//                if (temp.isEnd()) {
//                    sb.append(current.getValue());
//                    System.out.println("Found pattern: " + sb.toString());
//                    sb = new StringBuilder();
//                }
//                temp = temp.getFail();
//            }
        }
    }


}
