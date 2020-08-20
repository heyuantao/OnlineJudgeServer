package cn.heyuantao.onlinejudgeserver.debug;

import cn.heyuantao.onlinejudgeserver.annotation.Debug;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author he_yu
 */
@Slf4j
@Service
public class DebugCron {

    @Autowired
    DebugDataSetUtils debugDataSetUtils;

    @Autowired
    DebugService debugService;

    @Debug(value = "true")
    @Scheduled(fixedRate = 30*1000)
    public void postProblemSchedule() throws IOException {
        String errorMessage = String.format("This api should not be call in Production, Post post to add problem api !");
        log.error(errorMessage);
        //周期性执行任务
        debugService.postProblemToAPI();
    }

}
