package cn.heyuantao.onlinejudgeserver.core;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UUIDGeneratorTest {

    @Test
    void getUIID() {
        String id = UUIDGenerator.generateSolutionKey();
        System.out.println(id);
    }
}