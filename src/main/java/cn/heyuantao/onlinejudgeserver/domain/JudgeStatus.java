package cn.heyuantao.onlinejudgeserver.domain;

/**
 * @author he_yu
 * 判题状态
 * 新添加的题目进入等待状态，判题时状态为判题中
 */

public enum JudgeStatus {

    /**
     * 该状态PD意思为Pending，即该API已经接受该题目，但题目还没下发给判题机
     */
    PD("待处理",-1),

    /**
     * 内置的几种状态，与判题机的设置一致，
     * 如果判题机在下面几种状态中则说明对应的题目已经发给发给了判题机
     */
    WT0("等待",0), WT1("等待重判",1), CI("编译中",2), RI("运行并判断中",3),
    AC("成功",4),
    PE("格式错误",5), WA("答案错误",6),
    TL("时间超限",7),ML("内存超限",8),OL("输出超限",9),
    RE("运行错误",10),CE("编译错误",11),
    CO("编译成功",12),TR("运行完成",13);

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

    public Boolean isInPendingStatus(){
        if(this==JudgeStatus.PD){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}
