package search;

import java.util.ArrayList;
import java.util.List;

public class Util {


    /**
     * Backtrace according to the parent records and return the path.
     * (including both start and end nodes)
     * @param node End node
     * @return the path
     */
    public static List<int[]> backtrace(Node node) {
        List<int[]> path = new ArrayList<>();
        while (node != null) {
            path.add(new int[]{node.x, node.y});
            node = node.parent;
        }
        // Reverse path for correct order
        java.util.Collections.reverse(path);
        return path;
    }

    /**
     * Backtrace from start and end node, and return the path.
     * (including both start and end nodes)
     * @param nodeA Start node
     * @param nodeB End node
     * @return the bidirectional path
     */
    public static List<int[]> biBacktrace(Node nodeA, Node nodeB) {
        List<int[]> pathA = backtrace(nodeA);
        List<int[]> pathB = backtrace(nodeB);
        java.util.Collections.reverse(pathB);
        pathA.addAll(pathB);
        return pathA;
    }

    /**
     * Compute the length of the path.
     * @param path The path
     * @return The length of the path
     */
    public static double pathLength(List<int[]> path) {
        double sum = 0;
        for (int i = 1; i < path.size(); i++) {
            int[] a = path.get(i - 1);
            int[] b = path.get(i);
            int dx = a[0] - b[0];
            int dy = a[1] - b[1];
            sum += Math.sqrt(dx * dx + dy * dy);
        }
        return sum;
    }

    /**
     * Given the start and end coordinates, return all the coordinates lying
     * on the line formed by these coordinates, based on Bresenham's algorithm.
     * @param x0 Start x coordinate
     * @param y0 Start y coordinate
     * @param x1 End x coordinate
     * @param y1 End y coordinate
     * @return The coordinates on the line
     */
    public static List<int[]> interpolate(int x0, int y0, int x1, int y1) {
        List<int[]> line = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            line.add(new int[]{x0, y0});
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return line;
    }

    /**
     * Given a compressed path, return a new path that has all the segments
     * in it interpolated.
     * @param path The path
     * @return expanded path
     */
    public static List<int[]> expandPath(List<int[]> path) {
        List<int[]> expanded = new ArrayList<>();
        if (path.size() < 2) return expanded;

        for (int i = 0; i < path.size() - 1; i++) {
            int[] coord0 = path.get(i);
            int[] coord1 = path.get(i + 1);
            List<int[]> interpolated = interpolate(coord0[0], coord0[1], coord1[0], coord1[1]);
            expanded.addAll(interpolated.subList(0, interpolated.size() - 1));
        }
        expanded.add(path.get(path.size() - 1));
        return expanded;
    }

    /**
     * Smoothen the given path. The original path will not be modified; a new path will be returned.
     * @param grid The grid
     * @param path The path
     * @return smoothened path
     */
    public static List<int[]> smoothenPath(Grid grid, List<int[]> path) {
        List<int[]> newPath = new ArrayList<>();
        int sx = path.get(0)[0], sy = path.get(0)[1];
        newPath.add(new int[]{sx, sy});

        for (int i = 2; i < path.size(); i++) {
            int ex = path.get(i)[0], ey = path.get(i)[1];
            List<int[]> line = interpolate(sx, sy, ex, ey);

            boolean blocked = false;
            for (int j = 1; j < line.size(); j++) {
                int[] testCoord = line.get(j);
                if (!grid.isWalkableAt(testCoord[0], testCoord[1])) {
                    blocked = true;
                    break;
                }
            }

            if (blocked) {
                int[] lastValidCoord = path.get(i - 1);
                newPath.add(lastValidCoord);
                sx = lastValidCoord[0];
                sy = lastValidCoord[1];
            }
        }
        newPath.add(path.get(path.size() - 1));
        return newPath;
    }

    /**
     * Compress a path, remove redundant nodes without altering the shape
     * The original path is not modified
     * @param path The path
     * @return The compressed path
     */
    public static List<int[]> compressPath(List<int[]> path) {
        if (path.size() < 3) return path;

        List<int[]> compressed = new ArrayList<>();
        int sx = path.get(0)[0], sy = path.get(0)[1];
        int px = path.get(1)[0], py = path.get(1)[1];
        int dx = px - sx, dy = py - sy;
        double sq = Math.sqrt(dx * dx + dy * dy);
        dx /= sq;
        dy /= sq;

        compressed.add(new int[]{sx, sy});

        for (int i = 2; i < path.size(); i++) {
            int lx = px, ly = py;
            int ldx = dx, ldy = dy;
            px = path.get(i)[0];
            py = path.get(i)[1];
            dx = px - lx;
            dy = py - ly;
            sq = Math.sqrt(dx * dx + dy * dy);
            dx /= sq;
            dy /= sq;

            if (dx != ldx || dy != ldy) {
                compressed.add(new int[]{lx, ly});
            }
        }
        compressed.add(new int[]{px, py});
        return compressed;
    }

}
