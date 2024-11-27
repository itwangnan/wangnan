package search;

import java.util.*;

public class DStarFinder {
    private boolean allowDiagonal;
    private boolean dontCrossCorners;
    protected Heuristic heuristic;
    private int weight;
    private DiagonalMovement diagonalMovement;
    private Grid grid;
    private int endX;
    private int endY;
    double SQRT2 = Math.sqrt(2);

    public DStarFinder(Map<String, Object> opt) {
        this.allowDiagonal = (boolean) opt.getOrDefault("allowDiagonal", false);
        this.dontCrossCorners = (boolean) opt.getOrDefault("dontCrossCorners", false);
        this.heuristic = (Heuristic) opt.getOrDefault("heuristic", Heuristic.MANHATTAN);
        this.weight = (int) opt.getOrDefault("weight", 1);
        this.diagonalMovement = (DiagonalMovement) opt.get("diagonalMovement");
        this.grid = (Grid) opt.get("grid");
        this.endX = (int) opt.getOrDefault("endX",0);
        this.endY = (int) opt.getOrDefault("endY",0);

        if (this.diagonalMovement == null) {
            if (!this.allowDiagonal) {
                this.diagonalMovement = DiagonalMovement.NEVER;
            } else {
                if (this.dontCrossCorners) {
                    this.diagonalMovement = DiagonalMovement.ONLY_WHEN_NO_OBSTACLES;
                } else {
                    this.diagonalMovement = DiagonalMovement.IF_AT_MOST_ONE_OBSTACLE;
                }
            }
        }

        // When diagonal movement is allowed, the manhattan heuristic is not admissible.
        // It should be octile instead.
        if (opt.get("heuristic") == null){
            if (this.diagonalMovement == DiagonalMovement.NEVER) {
                this.heuristic = Heuristic.MANHATTAN;
            } else {
                this.heuristic = Heuristic.OCTILE;
            }
        }

    }


    public void incr(int x,int y,boolean walkable){
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));

        Node curNode = grid.getNodeAt(x, y);

        grid.setWalkableAt(x, y,walkable);


        openList.add(curNode);






        // While the open list is not empty
        while (!openList.isEmpty()) {
            // 弹出具有最小“f”值的节点的位置。
            Node node = openList.poll();
            node.closed = true;

            // 获取当前节点的邻居
            List<Node> neighbors = grid.getNeighbors(node, diagonalMovement);
            for (Node neighbor : neighbors) {
                // 获取当前节点与邻居节点之间的距离（代价）
                double g = node.g + ((neighbor.x - node.x == 0 || neighbor.y - node.y == 0) ? 1 : SQRT2);
                double h = weight * heuristic.cal(Math.abs(x - endX), Math.abs(y - endY));
                double f = g + h;

                //1.若该相邻节点从未被拓展过，即该相邻节点还未进OpenList
                //2.若该相邻节点的父节点是当前点，且h（y）≠ h （x）+ c (x,y)
                if (!neighbor.closed || (neighbor.parent == node && neighbor.f != f)){
                    //1和2 则将该相邻节点的父节点设为当前点，并将该相邻节点的h值设为h （x）+ c (x,y)
                    neighbor.parent = node;
                    neighbor.g = g;
                    neighbor.h = h;
                    neighbor.f = f;
                    openList.add(neighbor);
                } else if (neighbor.parent != node && neighbor.f > f){
                    //3.若该相邻节点的父节点不是当前点，且h（y）> h （x）+ c (x,y)
                    openList.add(neighbor);
                }
                //④若该相邻节点的父节点不是当前点，且h（x）> h （y）+ c (x,y),
                // 且该相邻点y在closedlist中，且h（y）> k,则将y重新放到OpenList中，怎么理解呢？
                // 我们每次从OpenList取点时，取得是k最小的，既然y已经在closedlist中，说明y比当前点先拓展，即k（y）<= k(x)，而现在h（y）> k（x），说明y受到了障碍物的影响，使得h（y）变大了，因此需要将y放到OpenList进行考察



//                // 检查邻居是否还未被检查过，或者是否可以从当前节点以较小的成本到达
//                //这里就类似Dijkstra，记录走过代价最低的路径
//                if (neighbor.opened == 0 || ng < neighbor.g) {
//                    //走到目前格子的代价
//                    neighbor.g = ng;
//                    //计算距离函数
//                    neighbor.h = neighbor.h == 0 ? weight * heuristic.cal(Math.abs(x - endX), Math.abs(y - endY)) : neighbor.h;
//
//                    neighbor.f = neighbor.g + neighbor.h;
//                    //标记前置节点
//                    neighbor.parent = node;
//
//                    if (neighbor.opened == 0) {
//                        neighbor.opened = 1;
//                    } else {
//                        //可以用较小的成本到达邻居。由于其 f 值已更新，我们必须更新其在开放列表中的位置
//                        openList.remove(neighbor);
//                    }
//                    openList.add(neighbor);
//                }
            }
        }


    }

    public List<Node> findPath(int startX, int startY) {
        //小堆存储所有后续可处理点位
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));

        Node startNode = grid.getNodeAt(startX, startY);
        Node endNode = grid.getNodeAt(endX, endY);
        //距离函数
        Heuristic heuristic = this.heuristic;
        //路径查找配置
        DiagonalMovement diagonalMovement = this.diagonalMovement;
        //
        int weight = this.weight;

        // 将起始节点的 `g` 和 `f` 值设置为 0
        startNode.g = 0;
        startNode.f = 0;

        // 将起始节点推入开放列表
        openList.add(startNode);
        startNode.opened = 1;

        // While the open list is not empty
        while (!openList.isEmpty()) {
            // 弹出具有最小“f”值的节点的位置。
            Node node = openList.poll();
            node.closed = true;

            // 如果到达终点，则构建路径并返回
            if (node.equals(endNode)) {
                return backtrace(endNode);
            }

            // 获取当前节点的邻居
            List<Node> neighbors = grid.getNeighbors(node, diagonalMovement);
            for (Node neighbor : neighbors) {
                //走过的格子
                if (neighbor.closed) {
                    continue;
                }

                int x = neighbor.x;
                int y = neighbor.y;

                // 获取当前节点与邻居节点之间的距离（代价）
                double ng = node.g + ((x - node.x == 0 || y - node.y == 0) ? 1 : SQRT2);

                // 检查邻居是否还未被检查过，或者是否可以从当前节点以较小的成本到达
                //这里就类似Dijkstra，记录走过代价最低的路径
                if (neighbor.opened == 0 || ng < neighbor.g) {
                    //走到目前格子的代价
                    neighbor.g = ng;
                    //计算距离函数
                    neighbor.h = neighbor.h == 0 ? weight * heuristic.cal(Math.abs(x - endX), Math.abs(y - endY)) : neighbor.h;

                    neighbor.f = neighbor.g + neighbor.h;
                    //标记前置节点
                    neighbor.parent = node;

                    if (neighbor.opened == 0) {
                        neighbor.opened = 1;
                    } else {
                        //可以用较小的成本到达邻居。由于其 f 值已更新，我们必须更新其在开放列表中的位置
                        openList.remove(neighbor);
                    }
                    openList.add(neighbor);
                }
            }
        }

        // Fail to find the path
        return new ArrayList<>();
    }

    // Backtrace method to construct the path
    private List<Node> backtrace(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
}
