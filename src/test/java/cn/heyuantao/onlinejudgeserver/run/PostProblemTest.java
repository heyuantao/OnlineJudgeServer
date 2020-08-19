package cn.heyuantao.onlinejudgeserver.run;

import cn.heyuantao.onlinejudgeserver.config.QueueConfig;
import cn.heyuantao.onlinejudgeserver.debug.DebugDataSetUtils;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostProblemTest {

    @Autowired
    DebugDataSetUtils debugDataSetUtils;

    @Autowired
    QueueConfig queueConfig;

    @Test
    public void getStatus() throws UnsupportedEncodingException {
        String solutionIdRequest = String.format("http://127.0.0.1:8080/api/v1/onlinejudgeserver/problem/status/%s/","c8cdb82bf3d0411083247379185492b2");
        try{
            Mono<String> resp = WebClient.builder().
                    defaultHeaders(httpHeaders -> {httpHeaders.add("Authorization","abc123");}).build().
                    get().uri(solutionIdRequest).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);

            System.out.println(resp.block());
        }catch (WebClientResponseException ex){
            HttpStatus status = ex.getStatusCode();
            String content = new String(ex.getResponseBodyAsByteArray(),"utf-8");
            System.out.println(status);
            System.out.println(content);
        }
    }

    @Test
    public void appendProblem() throws IOException {
        String addProblemUrl = String.format("http://127.0.0.1:8080/api/v1/onlinejudgeserver/problem/");
        DisplayProblemTest problemGenerator = new DisplayProblemTest();
        ProblemRequestDTO oneProblem = debugDataSetUtils.getSampleProblemDTO();
        System.out.print(oneProblem);

        try{
            Mono<String> resp = WebClient.builder().
                    defaultHeaders(httpHeaders -> {httpHeaders.add("Authorization","abc123");}).build().
                    post().uri(addProblemUrl).bodyValue(oneProblem).
                    accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);

            System.out.println(resp.block());
        }catch (WebClientResponseException ex){
            HttpStatus status = ex.getStatusCode();
            String content = new String(ex.getResponseBodyAsByteArray(),"utf-8");
            System.out.println(status);
            System.out.println(content);
        }
    }

    @Test
    public void displayConfigSettings(){
        Integer value = queueConfig.getPendingQueueSize();
        System.out.print(value);
    }
}
