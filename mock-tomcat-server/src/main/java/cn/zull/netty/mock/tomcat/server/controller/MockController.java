package cn.zull.netty.mock.tomcat.server.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zurun
 * @date 2019/7/29 19:38:10
 */
@Slf4j
@RestController
@RequestMapping("mock")
public class MockController {
    @Autowired
    HttpServletRequest httpRequest;

    @GetMapping("get")
    public JSONObject get() {
        log.info("[get] traceId:{}", httpRequest.getHeader("traceId"));
        return ok("get请求返回!");
    }

    @PostMapping("post")
    public JSONObject post(String body) {
        log.info("[post] traceId:{} body:{}", httpRequest.getHeader("traceId"), body);
        return ok("post请求返回!");
    }

    @PostMapping("error")
    public HttpEntity error(String body) {
        log.info("[error] traceId:{} body:{}", httpRequest.getHeader("traceId"), body);
        HttpHeaders headers = new HttpHeaders();
        headers.set("t-head", "测试返回请求头");

        return new ResponseEntity<>(ok("500返回"), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private JSONObject ok(String msg) {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("msg", msg);
        return json;
    }
}
