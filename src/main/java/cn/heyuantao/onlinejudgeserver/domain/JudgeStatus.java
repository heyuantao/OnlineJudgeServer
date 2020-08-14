package cn.heyuantao.onlinejudgeserver.domain;

/**
 * @author he_yu
 * 判题状态
 * 新添加的题目进入等待状态，判题时状态为判题中
 */

public enum JudgeStatus {

    WT0("等待",0), WT1("判题中",1), CI("判题中",2), RI("判题中",3),
    AC("成功",4),
    PE("格式错误",5), WA("答案错误",5),
    TL("时间超限",6),ML("内存超限",7),OL("输入输出超限",7);

    private String name;
    private Integer value;

    JudgeStatus(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
