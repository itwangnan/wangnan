package search;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AStarTest {

    @Test
    public void test(){

        Grid grid = new Grid(64,36);
//        grid.setWalkableAt(5,13,false);
        grid.setWalkableAt(4,15,false);
        grid.setWalkableAt(3,14,false);
        grid.setWalkableAt(3,13,false);
        grid.setWalkableAt(3,12,false);
        grid.setWalkableAt(3,11,false);
//        grid.setWalkableAt(3,13,false);
        //M585 405L615 405L645 405L675 405L705 405L735 375
        Map<String, Object> opt = new HashMap<>();
        opt.put("allowDiagonal",true);
        opt.put("dontCrossCorners",true);
        opt.put("heuristic",Heuristic.MANHATTAN);
        opt.put("weight",1);
        opt.put("timeLimit",10D);
//        opt.put("diagonalMovement",DiagonalMovement.ONLY_WHEN_NO_OBSTACLES);


//        AStarFinder aStarFinder = new AStarFinder(opt);
//        DijkstraFinder finder = new DijkstraFinder(opt);
        IDAStarFinder finder = new IDAStarFinder(opt);
//        BiAStarFinder biAStarFinder = new BiAStarFinder(opt);

//        List<Node> path = aStarFinder.findPath(0, 12, 6, 13, grid);
        List<Node> path = finder.findPath(0, 12, 6, 13, grid);
        for (Node node : path) {
            System.out.println(node.x +"___"+node.y);
        }
    }

    public String buildSvgPath(List<Node> path,int nodeSize) {
        StringBuilder sb = new StringBuilder();
        // 计算第一个点的位置
        sb.append("M").append(path.get(0).x * nodeSize + nodeSize / 2).append(" ")
                .append(path.get(0).y * nodeSize + nodeSize / 2);

        // 遍历路径中的其他点
        for (int i = 1; i < path.size(); ++i) {
            sb.append("L").append(path.get(i).x * nodeSize + nodeSize / 2).append(" ")
                    .append(path.get(i).y * nodeSize + nodeSize / 2);
        }

        return sb.toString();
    }
}
