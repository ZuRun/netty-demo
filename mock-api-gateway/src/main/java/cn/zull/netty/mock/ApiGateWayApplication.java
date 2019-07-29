package cn.zull.netty.mock;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author zurun
 * @date 2019/7/29 16:12:13
 */
@SpringBootApplication
public class ApiGateWayApplication {
    public static void main(String[] args) {
        // 不启动web服务
        // SpringApplication.run(ApiGateWayApplication.class, args)
        new SpringApplicationBuilder(ApiGateWayApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
