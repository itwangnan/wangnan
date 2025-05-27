package decisiontree;

public class Loan {

    /**
     * 年龄段: 0 青年，1 中年，2 老年
     */
    private Integer ageType;

    /**
     * 是否有工作 : 0 无工作，1 有工作
     */
    private Integer work;

    /**
     * 是否有房子 : 0 无房子，1 有房子
     */
    private Integer house;

    /**
     * 信贷情况:    0 一般、1 好、2 非常好
     */
    private Integer credit;


    /**
     * 结果:0 不通过、1 通过
     */
    private Integer result;

    public Loan(Integer ageType, Integer work, Integer house, Integer credit, Integer result) {
        this.ageType = ageType;
        this.work = work;
        this.house = house;
        this.credit = credit;
        this.result = result;
    }

    public Integer getAgeType() {
        return ageType;
    }

    public void setAgeType(Integer ageType) {
        this.ageType = ageType;
    }

    public Integer getWork() {
        return work;
    }

    public void setWork(Integer work) {
        this.work = work;
    }

    public Integer getHouse() {
        return house;
    }

    public void setHouse(Integer house) {
        this.house = house;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
