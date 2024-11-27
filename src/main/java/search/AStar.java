package search;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AStar {

    class Node{

        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private static int[] DIRECTION = {0, -1, -1, 0, 1, 1, -1, 1, 0};

    @Test
    public void test(){


        this.exec(new Node(0,0),new Node(2,2),new Node[]{new Node(1,1)});



    }

    private void exec(Node start, Node end, Node[] walls) {

        Map<Integer, Set<Integer>> wallMap = Arrays.stream(walls)
                .collect(Collectors.groupingBy(Node::getX,
                        Collectors.mapping(Node::getY,Collectors.toSet())));

        int[][] gs = new int[4][4];
        int cnt = 1;
        int x = start.x;
        int y = start.y;
        while (true){

            double min = Double.MAX_VALUE;
            int old_x = x;
            int old_y = y;
            for (int i = 1; i < DIRECTION.length; i++) {

                int newX = old_x + DIRECTION[i - 1];
                int newY = old_y + DIRECTION[i];

                if (newX < 0 || newY < 0 || newX > 2 || newY > 2){
                    continue;
                }
                Set<Integer> ySet = wallMap.get(newX);
                if (ySet != null && ySet.contains(newY)){
                    continue;
                }
                int g = gs[newX][newY];
                if (g == 0){
                    gs[newX][newY] = cnt;
                    g = cnt;
                }

                //计算代价
                double h = calculateDistance(end.x, end.y, newX, newY);
                //
                double v = g + h;
                if (min > v){
                    x = newX;
                    y = newY;
                    min = v;
                }

            }
            System.out.println(String.format("x:%s,y:%s",x,y));
            if (x == end.x && y == end.y){
                break;
            }
            cnt++;
        }








    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
