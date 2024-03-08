package str.ac;

import java.util.HashMap;
import java.util.Map;

/**
 * Ac的节点
 */
public class TrieNode {

    /**
     * 字符
     */
    char value;

    /**
     * 是否结束
     */
    boolean isEnd;

    /**
     * 后续节点
     */
    Map<Character, TrieNode> map;

    public TrieNode(char value) {
        this.value = value;
        this.isEnd = false;
        this.map = new HashMap<>();
    }


    public char getValue() {
        return value;
    }

    public boolean isEnd() {
        return isEnd;
    }


    public Map<Character, TrieNode> getMap() {
        return map;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setMap(Map<Character, TrieNode> map) {
        this.map = map;
    }

}
