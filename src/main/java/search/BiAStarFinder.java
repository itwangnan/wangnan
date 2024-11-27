package search;

import java.util.*;

public class BiAStarFinder {
    private boolean allowDiagonal;
    private boolean dontCrossCorners;
    private DiagonalMovement diagonalMovement;
    private Heuristic heuristic;
    private int weight;

    public BiAStarFinder(Map<String, Object> opt) {
        this.allowDiagonal = (boolean) opt.getOrDefault("allowDiagonal", false);
        this.dontCrossCorners = (boolean) opt.getOrDefault("dontCrossCorners", false);
        this.diagonalMovement = (DiagonalMovement) opt.get("diagonalMovement");
        this.heuristic = (Heuristic) opt.getOrDefault("heuristic", Heuristic.MANHATTAN);
        this.weight = (int) opt.getOrDefault("weight", 1);

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

    public List<Node> findPath(int startX, int startY, int endX, int endY, Grid grid) {
        //起点 小堆
        PriorityQueue<Node> startOpenList = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
        //终点 小堆
        PriorityQueue<Node> endOpenList = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
        Node startNode = grid.getNodeAt(startX, startY);
        Node endNode = grid.getNodeAt(endX, endY);

        double SQRT2 = Math.sqrt(2);
        // Set start node g and f values
        startNode.g = 0;
        startNode.f = 0;
        startOpenList.add(startNode);
        startNode.opened = 1;

        // Set end node g and f values
        endNode.g = 0;
        endNode.f = 0;
        endOpenList.add(endNode);
        endNode.opened = 2;

        // Start A* search
        while (!startOpenList.isEmpty() && !endOpenList.isEmpty()) {
            Node node;

            // Pop from start open list
            node = startOpenList.poll();
            node.closed = true;

            // Check neighbors for the start side
            List<Node> neighbors = grid.getNeighbors(node, diagonalMovement);
            for (Node neighbor : neighbors) {
                if (neighbor.closed) continue;
                if (neighbor.opened == 2) {
                    return biBacktrace(node, neighbor);
                }

                int x = neighbor.x;
                int y = neighbor.y;
                double ng = node.g + ((x - node.x == 0 || y - node.y == 0) ? 1 : SQRT2);

                if (neighbor.opened == 0 || ng < neighbor.g) {
                    neighbor.g = ng;
                    neighbor.h = neighbor.h != 0 ? neighbor.h : weight * heuristic.cal(Math.abs(neighbor.x - endX), Math.abs(neighbor.y - endY));
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = node;

                    if (neighbor.opened == 0) {
                        neighbor.opened = 1;
                    } else {
                        startOpenList.remove(neighbor);
                    }
                    startOpenList.add(neighbor);
                }
            }

            // Pop from end open list
            node = endOpenList.poll();
            node.closed = true;

            // Check neighbors for the end side
            neighbors = grid.getNeighbors(node, diagonalMovement);
            for (Node neighbor : neighbors) {
                if (neighbor.closed) continue;
                if (neighbor.opened == 1) {
                    return biBacktrace(neighbor, node);
                }

                double ng = node.g + (Math.abs(neighbor.x - node.x) == 0 || Math.abs(neighbor.y - node.y) == 0 ? 1 : Math.sqrt(2));
                if (neighbor.opened == 0 || ng < neighbor.g) {
                    neighbor.g = ng;
                    neighbor.h = neighbor.h != 0 ? neighbor.h : weight * heuristic.cal(Math.abs(neighbor.x - startX), Math.abs(neighbor.y - startY));
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = node;

                    if (neighbor.opened == 0) {
                        neighbor.opened = 2;
                    } else {
                        endOpenList.remove(neighbor);
                    }
                    endOpenList.add(neighbor);
                }
            }
        }

        return new ArrayList<>();  // No path found
    }

    private List<Node> biBacktrace(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node node = startNode;
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        node = endNode;
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        return path;
    }
}
