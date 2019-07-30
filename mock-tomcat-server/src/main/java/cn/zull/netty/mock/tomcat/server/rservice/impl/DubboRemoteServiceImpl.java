package cn.zull.netty.mock.tomcat.server.rservice.impl;

import cn.zull.netty.mock.tomcat.server.rservice.DubboRemoteService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zurun
 * @date 2019/7/30 11:35:11
 */
@Slf4j
@Service(version = "1.0", retries = -1, timeout = 950)
public class DubboRemoteServiceImpl implements DubboRemoteService {

    @Override
    public String testDubbo(JSONObject json) {
        log.info("[test-dubbo] json:{}", json);
        return "收到dubbo请求";
    }
}
