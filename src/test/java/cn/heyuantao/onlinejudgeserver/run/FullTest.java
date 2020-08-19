package cn.heyuantao.onlinejudgeserver.run;

import cn.heyuantao.onlinejudgeserver.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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


}