package search;

import java.util.Map;

/**
 *  Dijkstra就是没有h的AStar
 *  另外AStar如果不满足
 *  一致性 h(n) <= g(n') - g(n) + h(n') = c(n, n') + h(n') ，其中 c(n, n') 是从 n 到 n' 的实际代价
 *  可接受性 ,意味着它永远不会高估从当前节点到目标节点的实际代价，即 h(n) ≤ h*(n),其中 h*(n) 是从节点 n 到目标节点的实际代价。
 *  高估会导致去查找其他节点。
 *
 *  一致性+可接受性都满足则保证最短路径的条件。
 */
public class DijkstraFinder extends AStarFinder {

    public DijkstraFinder(Map<String, Object> opt) {
        super(opt);
        heuristic = (dx,dy) -> 0;
    }
}
