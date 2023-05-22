package kd_tree;


public class KdTreeNode {

    private Integer id;


    private Double[] data;

    /**
     * 特征坐标
     */
    private Double feature;

    /**
     * 切分轴
     */
    private int axle;


    private int level;


//    private boolean isSee;


    private KdTreeNode father;

    /**
     * 左
     */
    private KdTreeNode left;

    /**
     * 右
     */
    private KdTreeNode right;

    public KdTreeNode() {
    }

    public KdTreeNode(Double[] data, Double feature, int axle, KdTreeNode left, KdTreeNode right,int level) {
        this.data = data;
        this.feature = feature;
        this.axle = axle;
        this.left = left;
        this.right = right;
        this.level = level;
    }

    public Double[] getData() {
        return data;
    }

    public void setData(Double[] data) {
        this.data = data;
    }

    public Double getFeature() {
        return feature;
    }

    public void setFeature(Double feature) {
        this.feature = feature;
    }

    public int getAxle() {
        return axle;
    }

    public void setAxle(int axle) {
        this.axle = axle;
    }

    public KdTreeNode getLeft() {
        return left;
    }

    public void setLeft(KdTreeNode left) {
        this.left = left;
    }

    public KdTreeNode getRight() {
        return right;
    }

    public void setRight(KdTreeNode right) {
        this.right = right;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

//    public boolean isSee() {
//        return isSee;
//    }
//
//    public void setSee(boolean see) {
//        isSee = see;
//    }

    public KdTreeNode getFather() {
        return father;
    }

    public void setFather(KdTreeNode father) {
        this.father = father;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
