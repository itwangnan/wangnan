package str.ac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通字典树
 */
public class TrieTree {

    private TrieNode root;

    public TrieTree() {
        this.root = new TrieNode(' ');
    }


    public TrieTree add(List<String> patterns){
        for (String pattern : patterns) {
            add(pattern);
        }
        return this;

    }


    public void add(String pattern) {
        TrieNode node = root;
        for (char c : pattern.toCharArray()) {
            Map<Character, TrieNode> map = node.getMap();
            if (map == null){
                map = new HashMap<>();
            }
            if (map.get(c) == null) {
                TrieNode children = new TrieNode(c);
                map.put(c,children);
            }
            node.setMap(map);

            node = map.get(c);
        }

        node.setEnd(true);
    }


    public boolean match(String text) {

        TrieNode current = root;

        char[] arr = text.toCharArray();
        for (int i = 0; i < arr.length; i++) {

            char c = arr[i];

            current = current.getMap().get(c);

            if (current == null){
                return false;
            }else if (i == arr.length - 1 && current.isEnd()) {
                // 匹配到模式串
                return true;
            }

        }


        return false;
    }


    public TrieNode getRoot(){
        return root;
    }
}
