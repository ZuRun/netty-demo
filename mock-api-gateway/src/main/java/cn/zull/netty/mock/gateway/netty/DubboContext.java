package cn.zull.netty.mock.gateway.netty;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zurun
 * @date 2019/7/30 12:00:25
 */
@Slf4j
@Component
public class DubboContext {
    // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
    ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();

    {

// 弱类型接口名
        reference.setInterface("cn.zull.netty.mock.tomcat.server.rservice.DubboRemoteService");
        reference.setVersion("1.0");
// 声明为泛化接口
        reference.setGeneric(true);
    }

    public void dubbo() {


        // 用org.apache.dubbo.rpc.service.GenericService可以替代所有接口引用
        GenericService genericService = reference.get();

        JSONObject json = new JSONObject();
        json.put("s", 1);
        json.put("text", "text");
        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke("testDubbo", new String[]{"com.alibaba.fastjson.JSONObject"}, new Object[]{json});

        log.info("dubbo 返回: {}", result);
    }
}
