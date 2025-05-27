package decisiontree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DecideTest {

    public static void main(String[] args) throws Exception {

        List<Loan> list = new ArrayList<>();
        list.add(new Loan(0,0,0,0,0));
        list.add(new Loan(0,0,0,1,0));
        list.add(new Loan(0,1,0,1,1));
        list.add(new Loan(0,1,1,0,1));
        list.add(new Loan(0,0,0,0,0));
        list.add(new Loan(1,0,0,0,0));
        list.add(new Loan(1,0,0,1,0));

        list.add(new Loan(1,1,1,1,1));
        list.add(new Loan(1,0,1,2,1));
        list.add(new Loan(1,0,1,2,1));

        list.add(new Loan(2,0,1,2,1));
        list.add(new Loan(2,0,1,1,1));

        list.add(new Loan(2,1,0,1,1));
        list.add(new Loan(2,1,0,2,1));
        list.add(new Loan(2,0,0,0,0));


        //生成决策树
        DecideTree tree = DecideTree.convertTree(list,new BigDecimal(0.0001));

        //剪枝
        DecideTree prune = tree.prune(1);

        //数据预测
        Integer category = prune.prediction(new Loan(2,1,0,1,null));
        System.out.println(category);


    }


    public static DecideTreeNode copy(DecideTreeNode old,DecideTreeNode fatherNode){

        DecideTreeNode newNode = new DecideTreeNode();
        newNode.setNt(old.getNt());
        newNode.setRes(old.getRes());
        newNode.setT(old.getT());
        newNode.setType(old.getType());
        newNode.setMap(old.getMap());
        newNode.setAxle(old.getAxle());
        newNode.setFieldFun(old.getFieldFun());
        newNode.setId(old.getId());
        newNode.setFather(fatherNode);
        return newNode;

    }

}
