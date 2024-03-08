package str.ac;

import java.util.HashMap;
import java.util.Map;

/**
 * Ac的节点
 */
public class AcNode {

    /**
     * 字符
     */
    private char value;

    /**
     * 是否结束
     */
    private boolean isEnd;


    /**
     * 失效指针
     */
    private AcNode fail;


    private int level;
    /**
     * 后续节点
     */
    private Map<Character,AcNode> map;

    public AcNode(char value,int level) {
        this.value = value;
        this.isEnd = false;
        this.map = new HashMap<>();
        this.fail = null;
        this.level = level;
    }


    public char getValue() {
        return value;
    }

    public boolean isEnd() {
        return isEnd;
    }


    public AcNode getFail() {
        return fail;
    }

    public Map<Character, AcNode> getMap() {
        return map;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setFail(AcNode fail) {
        this.fail = fail;
    }

    public void setMap(Map<Character, AcNode> map) {
        this.map = map;
    }

    public int getLevel() {
        return level;
    }
}
