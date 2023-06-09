package decision_tree;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

/**
 * CART
 */
public class DecideTreeNode implements Serializable {

    /**
     * 结果
     */
    private Integer res;

    /**
     */
    private Integer id;


    /**
     * 结果:0 不通过、1 通过  数量
     */
    private Map<Integer,Integer> map;

    /**
     * 节点总样本量
     */
    private Integer nt;

    /**
     * 特征
     */
    private Function<Loan, Integer> fieldFun;

    /**
     * 普通节点0，叶子节点1
     */
    private int type = 0;

    /**
     * 切分轴
     */
    private int axle;

    /**
     * 子节点数
     */
    private int t;

    /**
     * 上
     */
    private DecideTreeNode father;

    /**
     * 左
     */
    private DecideTreeNode left;

    /**
     * 右
     */
    private DecideTreeNode right;

    public Map<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }

    public Function<Loan, Integer> getFieldFun() {
        return fieldFun;
    }

    public void setFieldFun(Function<Loan, Integer> fieldFun) {
        this.fieldFun = fieldFun;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DecideTreeNode getFather() {
        return father;
    }

    public void setFather(DecideTreeNode father) {
        this.father = father;
    }

    public DecideTreeNode getLeft() {
        return left;
    }

    public void setLeft(DecideTreeNode left) {
        this.left = left;
    }

    public DecideTreeNode getRight() {
        return right;
    }

    public void setRight(DecideTreeNode right) {
        this.right = right;
    }

    public int getAxle() {
        return axle;
    }

    public void setAxle(int axle) {
        this.axle = axle;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public Integer getNt() {
        return nt;
    }

    public void setNt(Integer nt) {
        this.nt = nt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRes() {
        return res;
    }

    public void setRes(Integer res) {
        this.res = res;
    }
}
