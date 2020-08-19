package cn.heyuantao.onlinejudgeserver.debug;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author he_yu
 * 在生产环境中，该接口不应该被调用
 * 这个控制器接口不验证用户身份
 * 该接口用于调试，当本地调试时，新创建的Problem里面带有notifyUrl，这个url发送到这个控制器
 */
@Slf4j
@Api(tags={"Debug接口"})
@Controller
@RequestMapping("/api/v1/debug/")
public class DebugController {

    @GetMapping("/{problemId}")
    public ResponseEntity<String> debugForNotifyUrl(
            @RequestParam String problemId
    ){
        String errorMessage = String.format("This api should not be call in Production, Problem with %s has finished judged !",problemId);
        log.error(errorMessage);
        return new ResponseEntity("", HttpStatus.OK);
    }
}
