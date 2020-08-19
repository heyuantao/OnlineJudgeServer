package cn.heyuantao.onlinejudgeserver.run;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import com.sun.scenario.effect.impl.prism.PrReflectionPeer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class FullTest {

    @Test
    public void getStatus() throws UnsupportedEncodingException {
        String solutionIdRequest = String.format("http://127.0.0.1:8080/api/v1/onlinejudgeserver/problem/status/%s/","x111111x");
        //System.out.println(solutionIdRequest);
        try{
            Mono<String> resp = WebClient.create().get().uri(solutionIdRequest).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);
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
        ProblemGenerator problemGenerator = new ProblemGenerator();
        ProblemRequestDTO oneProblem = problemGenerator.getOneProblem();
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



}
