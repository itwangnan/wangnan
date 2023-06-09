package decision_tree;

import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DecideTree {

    private DecideTreeNode top;

    private List<Loan> testList;

    public DecideTreeNode getTop() {
        return top;
    }

    public void setTop(DecideTreeNode top) {
        this.top = top;
    }

    public List<Loan> getTestList() {
        return testList;
    }

    public void setTestList(List<Loan> testList) {
        this.testList = testList;
    }

    public DecideTree() {
    }

    public DecideTree(DecideTreeNode top) {
        this.top = top;
    }

    /**
     * 生成 CART 决策树。
     * map 训练数据,threshold 阈值
     * @return
     */
    public static DecideTree convertTree(List<Loan> list, BigDecimal threshold) throws Exception {
        List<Loan> testList = new ArrayList<>();
//        List<Loan> trainList = new ArrayList<>();
        //第一次进来的时候将数据集划分
        DecideTree tree = new DecideTree();
        int num = 4;
        Iterator<Loan> it = list.iterator();
        while (it.hasNext()){
            Loan next = it.next();
            if (num > 0){
                testList.add(next);
                it.remove();
                num--;
            }

        }

        DecideTreeNode top = convertNode(list,threshold,null,0);
        tree.setTop(top);
        tree.setTestList(testList);
        return tree;
    }


    private static DecideTreeNode convertNode(List<Loan> list, BigDecimal threshold,DecideTreeNode father,int id) {
        Map<Integer, Integer> map = getCategorySet(list);
        int categorySize = map.size();
        DecideTreeNode node = new DecideTreeNode();
        node.setMap(map);
        Integer nt = map.values().stream().reduce(0, (x, y) -> x + y);
        node.setNt(nt);
        node.setFather(father);
        node.setId(id);
        node.setRes(getMaxCategory(map));
        if (categorySize <= 1){
            //叶子节点直接返回
            node.setType(1);
            node.setT(1);
            return node;
        }
        /**
         * (1) 设结点的训练数据集为D,计算现有特征对该数据集的基尼指数。此时，对
         * 每一个特征 A， 对其可能取的每个值 α，根据样本点对 A= α 的测试为"是"或"否" 将 D 分割成 D1 和 D2 两部分，利用Gini 计算 A= α 时的基尼指数。
         */

        //每个字段 每个值尝试
        GiniRes gini1 = Gini(list, Loan::getAgeType, categorySize);
        GiniRes gini2 = Gini(list, Loan::getWork, categorySize);
        GiniRes gini3 = Gini(list, Loan::getHouse, categorySize);
        GiniRes gini4 = Gini(list, Loan::getCredit, categorySize);

        List<GiniRes> sortGiniList = Arrays.asList(gini1, gini2, gini3, gini4).stream().sorted(Comparator.comparing(GiniRes::getGiniNum)).collect(Collectors.toList());

        GiniRes minGini = sortGiniList.get(0);

        /**
         * (2) 在所有可能的特征 A 以及它们所有可能的切分点 a 中，选择基尼指数最小的特征及其对应的切分点作为最优特征与最优切分点。
         *
         * 依最优特征与最优切分点，从现结点生成两个子结点，将训练数据集依特征分配到两个子结点中去。
         */
        //根据minGini 切分训练数据
        Function<Loan, Integer> fieldFun = minGini.getFieldFun();
        Integer a = minGini.getA();

        node.setAxle(a);
        node.setFieldFun(fieldFun);

        /**
         * 对两个子结点递归地调用(1)， (2) ，直至满足停止条件。
         */
        int leftT = 0;
        int rightT = 0;
        List<Loan> left = list.stream().filter(x -> (fieldFun.apply(x)).compareTo(a) == 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(left)){
            DecideTreeNode leftNode = convertNode(left, threshold,node,id + 1);
            node.setLeft(leftNode);
            leftT = leftNode.getT();
        }

        List<Loan> right = list.stream().filter(x -> (fieldFun.apply(x)).compareTo(a) != 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(right)){
            DecideTreeNode rightNode = convertNode(right, threshold,node,id + 2);
            node.setRight(rightNode);
            rightT = rightNode.getT();
        }


        node.setT(leftT + rightT);

        return node;
    }

    private static Integer getMaxCategory(Map<Integer, Integer> map) {

        int flag = 0;
        Integer res = null;
        for (Integer category : map.keySet()) {
            Integer count = map.get(category);
            if (count > flag){
                flag = count;
                res = category;
            }

        }


        return res;
    }

    private static Map<Integer, Integer> getCategorySet(List<Loan> list) {
        Map<Integer, Integer> map = new HashMap<>();

        for (Loan loan : list) {
            Integer result = loan.getResult();

            Integer count = map.get(result);
            if (count == null){
                map.put(result,1);
            }else {
                count++;
                map.put(result,count);
            }

        }

        return map;
    }

    /**
     * 如果样本集合 D 根据特征 A 是否取某一可能值a被分割成 D1和D2两部分
     * 则在特征 A 的条件下，集合 D 的基尼指数定义为
     * @return
     */
    private static GiniRes Gini(List<Loan> list, Function<Loan,Integer> fieldFun, Integer k){

        int size = list.size();

        Set<Integer> aSet = list.stream().map(x -> fieldFun.apply(x)).collect(Collectors.toSet());


        //寻找最小的基尼指数 并返回对应的数据
        BigDecimal sum = null;
        Integer aRes = null;

        boolean booleanFlag = false;
        if (aSet.size() <= 2){
            booleanFlag = true;
        }

        for (Integer a : aSet) {
            BigDecimal DCount = BigDecimal.valueOf(size);

            List<Loan> aList = list.stream().filter(x -> (fieldFun.apply(x)).compareTo(a) == 0).collect(Collectors.toList());
            BigDecimal D1Count = BigDecimal.valueOf(aList.size());
            BigDecimal D2Count = DCount.subtract(D1Count);

            Map<Integer,Long> d1Map = aList.stream().collect(Collectors.groupingBy(Loan::getResult,
                    Collectors.counting()));
            Map<Integer,Long> d2Map = list.stream().filter(x -> (fieldFun.apply(x)).compareTo(a) != 0).collect(Collectors.groupingBy(Loan::getResult,
                    Collectors.counting()));

            BigDecimal d1 = D1Count.divide(DCount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(Gini(D1Count, k,d1Map)).setScale(4, BigDecimal.ROUND_HALF_UP);

            BigDecimal d2 = D2Count.divide(DCount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(Gini(D2Count, k,d2Map)).setScale(4, BigDecimal.ROUND_HALF_UP);


            if (sum == null){
                sum = d1.add(d2);
                aRes = a;
            }else {
                BigDecimal flag = d1.add(d2);
                if (flag.compareTo(sum) < 0){
                    sum = flag;
                    aRes = a;
                }
            }
//            System.out.println(d1.add(d2));
            if (booleanFlag){
                break;
            }
        }


        return new GiniRes(sum,aRes,fieldFun);
    }

    private static BigDecimal Gini(BigDecimal dCount,Integer k,Map<Integer,Long> categoryMap) {
        if (categoryMap.size() == 0 || k == 1){
            return BigDecimal.ZERO;
        }
        else if (k == 2){
            //二类分类问题 则概率分布的基尼指数为 2p (1-p)
            Long num = categoryMap.values().stream().findFirst().get();
            BigDecimal p = BigDecimal.valueOf(num).divide(dCount, 4, BigDecimal.ROUND_HALF_UP);
            BigDecimal sum = BigDecimal.valueOf(2).multiply(p.multiply(BigDecimal.ONE.subtract(p)));

            return sum;
        }else {
            //每个分类下的概率Pk (1-Pk)
            BigDecimal sum = BigDecimal.ZERO;
            categoryMap.forEach((x,y) -> {
                BigDecimal p = BigDecimal.valueOf(y).divide(dCount, 4, BigDecimal.ROUND_HALF_UP);
                sum.add(p.multiply(BigDecimal.ONE.subtract(p)));
            });
            return sum;
        }
    }

    /**
     * 剪枝
     * @return
     */
    public DecideTree prune(int paramA){

        List<DecideTree> t_List = new ArrayList<>();
        // 输入: CART 算法生成的决策树 T_0;




//        DecideTree T_k = null;
//        List<Double> a_k_List = new ArrayList<>();
        DecideTree tree = this;
        while (true){

            //(1)设 k = 0, T = T_0。
//            int k = 0;

            //(2)设 α = +∞。
            double a = Double.MAX_VALUE;

            List<DecideTreeNode> innerNodeList = new ArrayList<>();
            //寻找node的所有叶节点
            Stack<DecideTreeNode> stack = new Stack<>();
            stack.push(tree.top);
            while (!stack.empty()){

                DecideTreeNode pop = stack.pop();

                if (pop.getType() == 0){
                    innerNodeList.add(pop);
                }

                if (pop.getRight() != null){
                    stack.push(pop.getRight());
                }
                if (pop.getLeft() != null){
                    stack.push(pop.getLeft());
                }
            }

            Integer id = null;
            //(3) 自下而上地对各内部结点 t 计算C(T_t),|T_t|(子树的叶节点数)以及
            for (DecideTreeNode innerNode : innerNodeList) {
                Double gt = (loss(innerNode,paramA,true) - loss(innerNode,paramA,false)) / (innerNode.getT() - 1);

                if (a <= gt){
                    a = gt;
                    id = innerNode.getId();
                }
                a = Math.min(a,gt);
            }

            //(4) 对 g(t) = α 的内部结点t进行剪枝，井对叶结点t以多数表决法决定其类，得到树 T。
            DecideTree treeT = copyAndPrune(tree, id);

            // (5)设 k=k+1, α_k=α , T_k=T。
//            k++;
            tree = treeT;

            t_List.add(treeT);


            //(6) 如果 Tk 不是由根结点及两个叶结点构成的树，则回到步骤 (2);
            boolean flag = this.isStop(tree.top);
            if (!flag){
                // 否则令 Tk =Tn
                break;
            }


        }


        //(7) 采用交叉验证法在子树序列布 T0 ，T1， ...，T_n中选取最优子树T_α。
        //这里我直接用测试集验证，误差率最小的 就当做最优化子树
        DecideTree bestTree = this.crossValidation(t_List);




        // 输出: 最优决策树T_α
        return bestTree;
    }


    /**
     * 交叉验证
     * @param t_list
     * @return
     */
    private DecideTree crossValidation(List<DecideTree> t_list) {

        List<Loan> testList = this.getTestList();
        DecideTree res = null;
        int flag = Integer.MAX_VALUE;
        for (DecideTree tree : t_list) {
            int errorCount = 0;
            for (Loan loan : testList) {
                Integer prediction = tree.prediction(loan);
                Integer reality = loan.getResult();
                if (!prediction.equals(reality)){
                    errorCount++;
                }
            }
            if (flag > errorCount){
                flag = errorCount;
                res = tree;
            }
        }

        return res;
    }

    /**
     * 拷贝树 并且删减
     * @param oldTree
     * @param id
     * @return
     */
    private DecideTree copyAndPrune(DecideTree oldTree, Integer id) {

        DecideTreeNode top = oldTree.top;

        HashMap<DecideTreeNode, DecideTreeNode> map = new HashMap<>();
        Queue<DecideTreeNode> queue = new LinkedList<>();
        queue.offer(top);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                // 复制当前节点并记录clone关系
                DecideTreeNode cur = queue.poll();
                DecideTreeNode curCopy = map.getOrDefault(cur, DecideTest.copy(cur,null));
                map.put(cur, curCopy);

                //删减
                if (curCopy.getId().equals(id)){
                    curCopy.setType(1);
                    curCopy.setLeft(null);
                    curCopy.setRight(null);
                }

                // 遍历孩子节点
                if (cur.getLeft() != null){
                    DecideTreeNode left = cur.getLeft();
                    queue.offer(left);
                    DecideTreeNode childCopy = map.getOrDefault(left, DecideTest.copy(left,curCopy));
                    curCopy.setLeft(childCopy);
                    map.put(left, childCopy);
                }
                if (cur.getRight() != null){
                    DecideTreeNode right = cur.getRight();
                    queue.offer(right);
                    DecideTreeNode childCopy = map.getOrDefault(right, DecideTest.copy(right,curCopy));
                    curCopy.setRight(childCopy);
                    map.put(right, childCopy);
                }

            }
        }
        return new DecideTree(map.get(top));
    }

    /**
     * 是否  是由根结点及两个叶结点构成的树
     * @param node
     * @return
     */
    private boolean isStop(DecideTreeNode node) {
        int num = 0;
        Stack<DecideTreeNode> stack = new Stack<>();
        stack.push(node);
        while (!stack.empty()){

            DecideTreeNode pop = stack.pop();

            if (pop.getType() == 0){
                num++;
                if (num > 1){
                    return false;
                }
            }

            if (pop.getRight() != null){
                stack.push(pop.getRight());
            }
            if (pop.getLeft() != null){
                stack.push(pop.getLeft());
            }
        }
        return true;
    }

    /**
     * 损失函数
     */
    private double loss(DecideTreeNode node,int a,boolean flag){
        if (!flag){
            return (- this.computeHT(node)) + a;
        }

        // |T|=数量
        int t = node.getT();

        List<DecideTreeNode> leafNodeList = new ArrayList<>();
        //寻找node的所有叶节点
        Stack<DecideTreeNode> stack = new Stack<>();
        stack.push(node);
        while (!stack.empty()){

            DecideTreeNode pop = stack.pop();

            if (pop.getType() == 1){
                leafNodeList.add(pop);
                continue;
            }

            if (pop.getRight() != null){
                stack.push(pop.getRight());
            }
            if (pop.getLeft() != null){
                stack.push(pop.getLeft());
            }

        }

        double c_t = 0d;
        for (DecideTreeNode leafNode : leafNodeList) {
            c_t += this.computeHT(leafNode);
        }

        c_t = -c_t + a * t;

        return c_t;

    }

    private double computeHT(DecideTreeNode leafNode) {
        Map<Integer, Integer> map = leafNode.getMap();
        Integer n_t = leafNode.getNt();
        if (n_t == 0){
            System.out.println("");
        }
        double h_t = 0d;
        for (Integer category : map.keySet()) {

            Integer n_tk = map.get(category);

            double d = n_tk / n_t;

            h_t += (d * Math.log(d));

        }
        return h_t;
    }


    /**
     * 预测数据
     * @return
     */
    public Integer prediction(Loan data){
        DecideTreeNode node = this.top;
        Integer res = node.getRes();
        while (node.getType() != 1){
            int axle = node.getAxle();
            Integer a = node.getFieldFun().apply(data);

            if (axle == a){
                node = node.getLeft();
            }else {
                node = node.getRight();
            }
            res = node.getRes();
        }

//        if (map.size() == 1){
//            for (Integer key : map.keySet()) {
//                return key;
//            }
//        }
//
//        Integer sum = map.values().stream().reduce(0, (x, y) -> (x + y));
//        Random rd = new Random();
//        int num = rd.nextInt(sum - 1);
//
//        int flag = 0;
//        for (Integer key : map.keySet()) {
//            Integer value = map.get(key);
//            if (flag <= num && num < (flag+value)){
//                return key;
//            }
//            flag = flag+value;
//        }

        return res;
    }




}
