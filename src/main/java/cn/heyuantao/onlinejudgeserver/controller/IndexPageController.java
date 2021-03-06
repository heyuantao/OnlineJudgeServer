package cn.heyuantao.onlinejudgeserver.controller;


import cn.heyuantao.onlinejudgeserver.core.SoftwareInformation;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author he_yu
 * 显示软件的首页面和状态信息
 */
@Api(tags={"软件基本信息"})
@Controller
@RequestMapping("/")
public class IndexPageController {

    @Resource
    SoftwareInformation softwareInformation;

    @GetMapping("/permissiontest")
    public ResponseEntity<String> test(){
        return new ResponseEntity("accept", HttpStatus.ACCEPTED);
    }

    @GetMapping("/version")
    public ResponseEntity<SoftwareInformation> version(){
        return new ResponseEntity(softwareInformation, HttpStatus.ACCEPTED);
    }
}
