package cn.heyuantao.onlinejudgeserver.debug;

import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author he_yu
 */
@Slf4j
@Service
public class DebugService {

    @Autowired
    DebugDataSetUtils debugDataSetUtils;

    /**
     * 通过WebClient向问题接口提交一个题目
     */
    public void postProblemToAPI() throws IOException {
        String addProblemUrl = String.format("http://127.0.0.1:8080/api/v1/onlinejudgeserver/problem/");
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
}
