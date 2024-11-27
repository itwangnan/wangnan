package search;

public class Node {

    int x;
    int y;
    boolean walkable = true;

    //f = g + h
    double f;
    double g;
    double h;
    //是否检查过，0没检查，1起始点检查，2终点检查
    short opened;
    //是否计算过的值
    boolean closed;

    boolean tested;

    int retainCount;

    Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node(int x, int y, boolean walkable) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWalkable() {
        return walkable;
    }
}
