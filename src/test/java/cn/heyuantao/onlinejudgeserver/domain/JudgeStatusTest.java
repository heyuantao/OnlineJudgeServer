package cn.heyuantao.onlinejudgeserver.domain;

import cn.heyuantao.onlinejudgeserver.core.JudgeStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class JudgeStatusTest {

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