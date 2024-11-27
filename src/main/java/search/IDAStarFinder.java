package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DFS + 启发式搜索，好处是不用保存A*算法遍历过的每个节点节省内存，坏处是重复节点还会重复遍历
 *
 */
public class IDAStarFinder {

    private boolean allowDiagonal;
    private boolean dontCrossCorners;
    private DiagonalMovement diagonalMovement;
    private Heuristic heuristic;
    private double weight;
    private boolean trackRecursion;
    private double timeLimit;
    private long startTime;

    public IDAStarFinder(Map<String, Object> opt) {
        this.allowDiagonal = (boolean) opt.getOrDefault("allowDiagonal", false);
        this.dontCrossCorners = (boolean) opt.getOrDefault("dontCrossCorners", false);
        this.heuristic = (Heuristic) opt.getOrDefault("heuristic", Heuristic.MANHATTAN);
        this.weight = (int) opt.getOrDefault("weight", 1);
        this.diagonalMovement = (DiagonalMovement) opt.get("diagonalMovement");
        //是否打印栈路径
        this.trackRecursion = (boolean)opt.getOrDefault("trackRecursion",false);
        //
        this.timeLimit = (double) opt.getOrDefault("timeLimit",Double.MAX_VALUE);

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

        // When diagonal movement is allowed, we use octile heuristic instead of manhattan.
        if (opt.get("heuristic") == null){
            if (this.diagonalMovement == DiagonalMovement.NEVER) {
                this.heuristic = Heuristic.MANHATTAN;
            } else {
                this.heuristic = Heuristic.OCTILE;
            }
        }
    }

//    public List<Node> findPath(int startX, int startY, int endX, int endY, Grid grid) {
//        final long startTime = System.currentTimeMillis();
//        final Node start = grid.getNodeAt(startX, startY);
//        final Node end = grid.getNodeAt(endX, endY);
//        double cutoff = heuristic.cal(Math.abs(start.x - end.x), Math.abs(start.y - end.y));
//
//        // Define search helper methods
//        SearchHelper helper = new SearchHelper(grid, start, end, startTime, cutoff);
//
//        // Start searching
//        for (int j = 0; true; ++j) {
//            List<Node> route = helper.search(start, 0, cutoff, 0);
//
//            // Path not found or time limit exceeded
//            if (route == null) {
//                return null;
//            }
//
//            if (route.size() > 0) {
//                return route;
//            }
//
//            // Increase cutoff depth for next iteration
//            cutoff = helper.getCutoff();
//        }
//    }


    // IDA* search
    private Double search(Node node,
                          double g,
                          double cutoff,
                          List<Node> route,
                          int depth,
                          Grid grid,
                          Node end) {
        // 查看是否在规定时间内找到答案
        if (this.timeLimit > 0 && (System.currentTimeMillis() - startTime) / 1000.0 > this.timeLimit) {
            return Double.MAX_VALUE;
        }

        //当前节点到结束节点的f
        double f = g + heuristic.cal(Math.abs(node.x - end.x), Math.abs(node.y - end.y)) * this.weight;

        // If we have exceeded the cutoff, return the f value
        //如果f大于cutoff，肯定就代表有更好的路
        if (f > cutoff) {
            //直接返回f
            return f;
        }

        // If we reached the end node, add to route and return the node
        if (node.equals(end)) {
            route.add(node);
            return null;
        }

        double min = Double.MAX_VALUE;
        //找到其他相邻节点，注意这里没有设置标记，也就是会遍历重复点
        List<Node> neighbors = grid.getNeighbors(node, this.diagonalMovement);

        for (Node neighbor : neighbors) {
//            if (this.trackRecursion) {
//                neighbor.retainCount = neighbor.retainCount + 1;
//                if (!neighbor.tested) {
//                    neighbor.tested = true;
//                }
//            }

            //看是否超时或f>cutoff了。
            Double t = search(neighbor, g + cost(node, neighbor), cutoff, route, depth + 1, grid, end);

            if (t == null) {
                route.add(node);
                return t;
            }

//            if (this.trackRecursion && (--neighbor.retainCount) == 0) {
//                neighbor.tested = false;
//            }

            if (t < min) {
                min = t;
            }
        }

        return min;
    }

    // Main findPath function
    public List<Node> findPath(int startX, int startY, int endX, int endY, Grid grid) {
        startTime = System.currentTimeMillis();
        Node start = grid.getNodeAt(startX, startY);
        Node end = grid.getNodeAt(endX, endY);

        //开始以初始点h为主
        double cutoff = heuristic.cal(Math.abs(start.x - end.x), Math.abs(start.y - end.y));
        List<Node> route = new ArrayList<>();
        Double t;

        while (true){
            route.clear();

            //
            t = search(start, 0, cutoff, route, 0, grid, end);

            // If we found a node, return the route
            if (t == null) {
                return route;
            }
            //超时
            // If time limit exceeded or no path found
            if (t == Double.MAX_VALUE) {
                return new ArrayList<>();
            }

            // Try again with a deeper cutoff
            cutoff = t;

        }
    }

    // Search helper class
//    private class SearchHelper {
//        private final Grid grid;
//        private final Node start;
//        private final Node end;
//        private final long startTime;
//        private double cutoff;
//
//        public SearchHelper(Grid grid, Node start, Node end, long startTime, double cutoff) {
//            this.grid = grid;
//            this.start = start;
//            this.end = end;
//            this.startTime = startTime;
//            this.cutoff = cutoff;
//        }
//
//        public List<Node> search(Node node, double g, double cutoff, int depth) {
//            if (System.currentTimeMillis() - startTime > timeLimit * 1000) {
//                return null; // Time limit exceeded
//            }
//
//            double f = g + weight * heuristic.cal(Math.abs(node.x - end.x), Math.abs(node.y - end.y));
//
//            // We have searched too deep for this iteration
//            if (f > cutoff) {
//                return null;
//            }
//
//            if (node.equals(end)) {
//                return List.of(node);
//            }
//
//            List<Node> neighbors = grid.getNeighbors(node, diagonalMovement);
////            double min = Double.MAX_VALUE;
//            for (Node neighbor : neighbors) {
//                List<Node> result = search(neighbor, g + cost(node, neighbor), cutoff, depth + 1);
//                if (result != null) {
//                    result.add(0, node);
//                    return result;
//                }
//            }
//
//            return null;  // No valid path found
//        }
//
        private double cost(Node a, Node b) {
            return (a.getX() == b.getX() || a.getY() == b.getY()) ? 1 : Math.sqrt(2);
        }
//
//        public double getCutoff() {
//            return cutoff;
//        }
//    }
}
