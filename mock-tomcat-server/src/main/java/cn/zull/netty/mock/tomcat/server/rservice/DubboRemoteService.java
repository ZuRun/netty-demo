package cn.zull.netty.mock.tomcat.server.rservice;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zurun
 * @date 2019/7/30 11:55:41
 */
public interface DubboRemoteService {
    String testDubbo(JSONObject json);
}
