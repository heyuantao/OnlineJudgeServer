package cn.heyuantao.onlinejudgeserver.core;

import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.mail.MailException;

import javax.swing.*;
import java.util.List;

/**
 * @author he_yu
 * 判题状态
 * 新添加的题目进入等待状态，判题时状态为判题中
 */

@Slf4j
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

    /**
     * 题目是否下发给了判题机，如果下发则返回False
     * @return
     */
    public Boolean isInPendingStatus(){
        if(this==JudgeStatus.PD){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    /**
     * 最后结果是否正确
     * @return
     */
    public Boolean isSuccessStatus(){
        if(this==JudgeStatus.AC){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    /**
     * 根据对应的值来获取相应的枚举类型,如果未找到，则返回null
     */
    public static JudgeStatus getJudgeStatusByValue(Integer value){
        List<JudgeStatus> judgeStatusList = EnumUtils.getEnumList(JudgeStatus.class);
        for(JudgeStatus item:judgeStatusList){
            if(item.getValue().equals(value)){
                return item;
            }
        }

        String errorMessage = String.format("Can not find JudgeStatus with value %d",value);
        log.error(errorMessage);
        throw new MessageException(errorMessage);
    }
}
