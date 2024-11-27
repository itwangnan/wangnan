package search;

public enum DiagonalMovement{

    //允许斜线走
    ALWAYS(1),
    //不允许斜线走
    NEVER(2),
    //如果最多只有一个障碍物，则允许对角线移动：当两个相邻的对角线方向上，最多只有一个障碍物时，允许对角线移动
    IF_AT_MOST_ONE_OBSTACLE(3),
    //仅在没有障碍物时允许对角线移动：只有当对角线方向上完全没有障碍物时，才允许对角线移动。
    ONLY_WHEN_NO_OBSTACLES(4)
    ;

    private Integer code;

    DiagonalMovement(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
