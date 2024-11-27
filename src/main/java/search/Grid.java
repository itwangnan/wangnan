package search;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int width;
    private int height;
    private Node[][] nodes;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.nodes = buildNodes(width, height, null);
    }

    public Grid(int[][] matrix) {
        this.height = matrix.length;
        this.width = matrix[0].length;
        this.nodes = buildNodes(width, height, matrix);
    }

    /**
     * Build and return the nodes for the grid.
     * @param width  The number of columns.
     * @param height The number of rows.
     * @param matrix A matrix that represents the walkable status of the nodes (0 or false for walkable).
     * @return The nodes for the grid.
     */
    private Node[][] buildNodes(int width, int height, int[][] matrix) {
        Node[][] nodes = new Node[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                nodes[i][j] = new Node(j, i);
            }
        }

        if (matrix == null) {
            return nodes; // Default all nodes as walkable.
        }

        if (matrix.length != height || matrix[0].length != width) {
            throw new IllegalArgumentException("Matrix size does not fit");
        }

        // Mark nodes as walkable or not based on the matrix
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (matrix[i][j] != 0) {
                    nodes[i][j].setWalkable(false); // Not walkable
                }
            }
        }

        return nodes;
    }

    public Node getNodeAt(int x, int y) {
        return this.nodes[y][x];
    }

    /**
     * Determine if the node at the given position is walkable.
     * @param x The x coordinate of the node.
     * @param y The y coordinate of the node.
     * @return Whether the position is walkable.
     */
    public boolean isWalkableAt(int x, int y) {
        return isInside(x, y) && this.nodes[y][x].isWalkable();
    }

    /**
     * Determine whether the position is inside the grid.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Whether the position is inside the grid.
     */
    public boolean isInside(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    /**
     * Set the walkability of a node at a specific position.
     * @param x        The x coordinate of the node.
     * @param y        The y coordinate of the node.
     * @param walkable Whether the node is walkable.
     */
    public void setWalkableAt(int x, int y, boolean walkable) {
        this.nodes[y][x].setWalkable(walkable);
    }

    /**
     * Get the neighbors of a given node.
     * @param node             The node for which neighbors are to be found.
     * @param diagonalMovement The type of diagonal movement allowed.
     * @return A list of neighboring nodes.
     */
    public List<Node> getNeighbors(Node node, DiagonalMovement diagonalMovement) {
        int x = node.getX();
        int y = node.getY();
        List<Node> neighbors = new ArrayList<>();
        boolean s0 = false, d0 = false;
        boolean s1 = false, d1 = false;
        boolean s2 = false, d2 = false;
        boolean s3 = false, d3 = false;

        // Check for cardinal directions first
        if (isWalkableAt(x, y - 1)) {
            neighbors.add(nodes[y - 1][x]);
            s0 = true;
        }
        if (isWalkableAt(x + 1, y)) {
            neighbors.add(nodes[y][x + 1]);
            s1 = true;
        }
        if (isWalkableAt(x, y + 1)) {
            neighbors.add(nodes[y + 1][x]);
            s2 = true;
        }
        if (isWalkableAt(x - 1, y)) {
            neighbors.add(nodes[y][x - 1]);
            s3 = true;
        }

        // Handle diagonal movement
        if (diagonalMovement == DiagonalMovement.NEVER) {
            return neighbors;
        }

        if (diagonalMovement == DiagonalMovement.ONLY_WHEN_NO_OBSTACLES) {
            d0 = s3 && s0;
            d1 = s0 && s1;
            d2 = s1 && s2;
            d3 = s2 && s3;
        } else if (diagonalMovement == DiagonalMovement.IF_AT_MOST_ONE_OBSTACLE) {
            d0 = s3 || s0;
            d1 = s0 || s1;
            d2 = s1 || s2;
            d3 = s2 || s3;
        } else if (diagonalMovement == DiagonalMovement.ALWAYS) {
            d0 = true;
            d1 = true;
            d2 = true;
            d3 = true;
        } else {
            throw new IllegalArgumentException("Incorrect value of diagonalMovement");
        }

        // Diagonal directions
        if (d0 && isWalkableAt(x - 1, y - 1)) {
            neighbors.add(nodes[y - 1][x - 1]);
        }
        if (d1 && isWalkableAt(x + 1, y - 1)) {
            neighbors.add(nodes[y - 1][x + 1]);
        }
        if (d2 && isWalkableAt(x + 1, y + 1)) {
            neighbors.add(nodes[y + 1][x + 1]);
        }
        if (d3 && isWalkableAt(x - 1, y + 1)) {
            neighbors.add(nodes[y + 1][x - 1]);
        }

        return neighbors;
    }

    /**
     * Clone the grid.
     * @return A new grid with the same configuration as the current grid.
     */
    public Grid clone() {
        Grid newGrid = new Grid(this.width, this.height);
        Node[][] newNodes = new Node[this.height][this.width];

        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                newNodes[i][j] = new Node(j, i, this.nodes[i][j].isWalkable());
            }
        }

        newGrid.nodes = newNodes;
        return newGrid;
    }

}
