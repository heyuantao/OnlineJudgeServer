package cn.heyuantao.onlinejudgeserver.core;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

        judgeStatus = JudgeStatus.getJudgeStatusByValue(20);
        System.out.println(judgeStatus);
    }
}