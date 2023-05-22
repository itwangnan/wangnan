package kd_tree;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class KdTree {

    private KdTreeNode top;


    /**
     * 给定一个构建于一个样本集的 kd 树，下面的算法可以寻找距离某个点 p 最近的 k 个样本。
     * @param k
     * @param data
     * @return
     */
    public List<KdTreeNode> select(int k,Double[] data) throws InterruptedException {

        List<Integer> existIdList = new ArrayList<>();

        KdTreeNode root = this.top;
        LinkedBlockingQueue<KdTreeNode> queue = new LinkedBlockingQueue<>();
        queue.put(root);
        //0. 设 L 为一个有 k 个空位的列表，用于保存已搜寻到的最近点。
        List<KdTreeNode> res = new ArrayList<>(k);

        //(一).根据 p 的坐标值和每个节点的切分向下搜索（也就是说，如果树的节点是照x_r = a 进行切分，
        // 并且 p 的 r 坐标小于 a，则向左枝进行搜索；反之则走右枝）。
        KdTreeNode branch = null;
        while (true) {
            if (queue.size() == 0){
                queue.put(branch);
            }

            while (queue.size() > 0) {
                KdTreeNode node = queue.poll();

                Double dataXr = data[node.getAxle()];

                if (node.getRight() == null && node.getLeft() == null) {
                    //最后一个节点
                    branch = node;
                    break;
                }

                if (dataXr >= node.getFeature()) {
                    if (node.getRight() != null) {
                        queue.put(node.getRight());
                    } else {
                        queue.put(node.getLeft());
                    }

                } else {
                    if (node.getLeft() != null) {
                        queue.put(node.getLeft());
                    } else {
                        queue.put(node.getRight());
                    }
                }

            }

            //(二).当达到一个底部节点时，将其标记为访问过。
            // 如果 L 里不足 k 个点，则将当前节点的特征坐标加入 L ；
            // 如果 L 不为空并且 当前节点的特征与p 的距离小于 L 里最长的距离，则用当前特征替换掉 L 中离 p 最远的点。
            existIdList.add(branch.getId());
//            branch.setSee(true);
            this.setRes(res, branch, data, k);


            //3.如果当前节点不是整棵树最顶端节点，执行 (a)；反之，输出 L，算法完成
            //a. 向上爬一个节点。如果当前（向上爬之后的）节点未曾被访问过，将其标记为被访问过，然后执行 (1) 和 (2)；
            // 如果当前节点被访问过，再次执行 (a)
            KdTreeNode father = branch.getFather();
            while (true) {
                if (branch.getLevel() == 0) {
                    return res;
                }
//                while (father.isSee()) {
//                    father = branch.getFather();
//                }
//                if (father == null) {
//                    return res;
//                }

                while (existIdList.contains(father.getId())){
                    father = father.getFather();
                    if (father.getLevel() == 0){
                        System.out.println("");
                    }
                }
                branch = father;
                existIdList.add(father.getId());

//                father.setSee(true);
                //(1). 如果此时 L 里不足 k 个点，则将节点特征加入 L；
                // 如果 L 中已满 k 个点，且当前节点与 p 的距离小于 L 里最长的距离，则用节点特征替换掉 L 中离最远的点。
                this.setRes(res, branch, data, k);

                //(2). 计算 p 和当前节点切分线的距离。
                // 如果该距离大于等于 L 中距离 p 最远的距离并且 L 中已有 k 个点，则在切分线另一边不会有更近的点，执行
                double one = Math.abs(data[branch.getAxle()] - branch.getFeature());
                Double two = res.stream().map(x -> this.differ(data, x.getData())).max(Double::compareTo).get();

                if (one >= two && res.size() == k) {
                    return res;
                } else {
                    // (三)；如果该距离小于 L 中最远的距离或者 L 中不足 k 个点，则切分线另一边可能有更近的点，
                    // 因此在当前节点的另一个枝从 (一) 开始执行。
                    if (father.getLevel() == 0){
                        return res;
                    }

                    if (father.getLeft() != null && !existIdList.contains(father.getLeft().getId())) {
                        branch = father.getLeft();
                        break;
                    } else if (father.getRight() != null && !existIdList.contains(father.getRight().getId())) {
                        branch = father.getRight();
                        break;
                    } else {
                        father = father.getFather();
                    }
                }
            }
        }

    }

    private void setRes(List<KdTreeNode> res, KdTreeNode branch,Double[] data,int k) {
        if (res.size() != k){
            res.add(branch);
        }else {
            double one = this.differ(data, branch.getData());

            int index = 0;
            double max = 0;
            for (int i = 0; i < res.size(); i++) {
                double differ = this.differ(data, res.get(i).getData());
                if (differ > max){
                    index = i;
                    max = differ;
                }
            }

            if (one < max){
                //替换
                res.set(index,branch);
            }
        }


    }

    private double differ(Double[] data1, Double[] data2) {
        BigDecimal res = BigDecimal.ZERO;
        for (int i = 0; i < data1.length; i++) {
            BigDecimal hypot = BigDecimal.valueOf(data1[i]).subtract(BigDecimal.valueOf(data2[i]));
            hypot = hypot.multiply(hypot);
            res = res.add(hypot);
        }

        return sqrt(res,2).doubleValue();
    }

    public static BigDecimal sqrt(BigDecimal value, int scale){
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal deviation = value;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
            cnt++;
        }
        deviation = deviation.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return deviation;
    }

    public void print() throws InterruptedException {
        LinkedBlockingQueue<KdTreeNode> queue = new LinkedBlockingQueue<>();
        queue.put(this.top);
        while (queue.size() > 0){
            KdTreeNode node = queue.poll();
            System.out.println(this.getStr(node)+"---"+node.getId());
            if (node.getLeft() != null){
                queue.put(node.getLeft());
            }
            if (node.getRight() != null){
                queue.put(node.getRight());
            }
        }
    }

    private String getStr(KdTreeNode node) {
        StringBuilder sb = new StringBuilder();
        if (node.getLevel() != 0){
            for (int i = 0; i < node.getLevel(); i++) {
                sb.append("-");
            }
        }
        sb.append(Arrays.toString(node.getData()));
        return sb.toString();
    }

    public static KdTree convert(List<Double[]> list, int dataLen) throws InterruptedException {

        KdTree tree = new KdTree();

        int id = 0;

//        int index = 0;
        int level = 0;

        List<Flag> xiList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Double[] data = list.get(i);
            Double xi = data[0];
            xiList.add(new Flag(xi, data));

        }


        LinkedBlockingQueue<FlagStack> queue = new LinkedBlockingQueue<>();
        do {
            int size = list.size();
            KdTreeNode node = null;
            if (queue.size() > 0){
                FlagStack flagStack = queue.poll();

                node = flagStack.getNode();

                int axle = (node.getLevel() + 1) % dataLen;
                node.setAxle(axle);
                node.setLevel(node.getLevel()+1);
                level = node.getLevel();

                xiList = flagStack.getFlag();
                for (Flag flag : xiList) {
                    Double[] data = flag.getData();
                    Double xi = data[axle];
                    flag.setXi(xi);
                }
                size = xiList.size();
            }
            int median = size / 2;
            if (size % 2 == 0){
                median--;
            }
            if (size == median || median < 0){
                continue;
            }

            xiList = xiList.stream().sorted(Comparator.comparing(Flag::getXi)).collect(Collectors.toList());

            Flag flag = xiList.get(median);
            //左边
            KdTreeNode left = null;
            KdTreeNode right = null;
            if (median != 0){
                left = new KdTreeNode();
                left.setFather(node);
                left.setLevel(level);
                List<Flag> leftFlag = xiList.subList(0, median);
                queue.put(new FlagStack(left,leftFlag));
            }
            //右边
            if (median + 1 != size){
                right = new KdTreeNode();
                right.setFather(node);
                right.setLevel(level);
                List<Flag> rightFlag = xiList.subList(median + 1, size);
                queue.put(new FlagStack(right,rightFlag));
            }

            if (node == null){
                node = new KdTreeNode(flag.getData(), flag.getXi(), 0, left, right,level);
                tree.setTop(node);
                left.setFather(node);
                right.setFather(node);
            }else {
                node.setData(flag.getData());
                node.setFeature(flag.getXi());
                node.setLeft(left);
                node.setRight(right);
            }
            node.setId(id);
            id++;

        } while (queue.size() > 0);


        return tree;
    }

    public KdTreeNode getTop() {
        return top;
    }

    public void setTop(KdTreeNode top) {
        this.top = top;
    }


    static class Flag{

        private Double xi;

        private Double[] data;

        public Flag(Double xi, Double[] data) {
            this.xi = xi;
            this.data = data;
        }

        public Double getXi() {
            return xi;
        }

        public void setXi(Double xi) {
            this.xi = xi;
        }

        public Double[] getData() {
            return data;
        }
    }

    static class FlagStack{

        private KdTreeNode node;

        private List<Flag> flag;

        public FlagStack(KdTreeNode node, List<Flag> flag) {
            this.node = node;
            this.flag = flag;
        }

        public KdTreeNode getNode() {
            return node;
        }

        public void setNode(KdTreeNode node) {
            this.node = node;
        }

        public List<Flag> getFlag() {
            return flag;
        }

        public void setFlag(List<Flag> flag) {
            this.flag = flag;
        }
    }
}
