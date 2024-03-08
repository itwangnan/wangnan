package decision_tree;

import java.math.BigDecimal;
import java.util.function.Function;

public class GiniRes {

    /**
     * 基尼值
     */
    private BigDecimal giniNum;

    /**
     * 切分点
     */
    private Integer a;

    /**
     * 对应特征
     */
    private Function<Loan,Integer> fieldFun;


    public GiniRes(BigDecimal giniNum, Integer a, Function<Loan, Integer> fieldFun) {
        this.giniNum = giniNum;
        this.a = a;
        this.fieldFun = fieldFun;
    }

    public BigDecimal getGiniNum() {
        return giniNum;
    }

    public Integer getA() {
        return a;
    }

    public Function<Loan, Integer> getFieldFun() {
        return fieldFun;
    }


}

