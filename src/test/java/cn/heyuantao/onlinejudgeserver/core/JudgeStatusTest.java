package cn.heyuantao.onlinejudgeserver.core;

import org.apache.commons.lang3.EnumUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class JudgeStatusTest {

    @Test
    void getJudgeStatusByValue() {
        JudgeStatus judgeStatus = null;

        judgeStatus = JudgeStatus.getJudgeStatusByValue(0);
        System.out.println(judgeStatus);

        judgeStatus = JudgeStatus.getJudgeStatusByValue(-1);
        System.out.println(judgeStatus);

        judgeStatus = JudgeStatus.getJudgeStatusByValue(12);
        System.out.println(judgeStatus);

/*        judgeStatus = JudgeStatus.getJudgeStatusByValue(20);
        System.out.println(judgeStatus);*/

        List<JudgeStatus> judgeStatusList = EnumUtils.getEnumList(JudgeStatus.class);
        System.out.println(judgeStatusList);
    }

    @Test
    void isInPendingStatus() {
        JudgeStatus judgeStatus = null;
        Boolean status = null;

        judgeStatus = JudgeStatus.AC;
        System.out.println(judgeStatus);
        System.out.println(judgeStatus.getName());
        System.out.println(judgeStatus.getValue());
        status = judgeStatus.isInPendingStatus();
        Assert.assertEquals(status,Boolean.FALSE);
        System.out.println(status);


        judgeStatus = JudgeStatus.PD;
        System.out.println(judgeStatus);
        System.out.println(judgeStatus.getName());
        System.out.println(judgeStatus.getValue());
        status = judgeStatus.isInPendingStatus();
        Assert.assertEquals(status,Boolean.TRUE);
        System.out.println(status);

    }
}