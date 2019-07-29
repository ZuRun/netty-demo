package cn.zull.netty.mock.gateway.netty.client;

import cn.zull.netty.mock.common.util.SpringApplicationContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zurun
 * @date 2019/7/29 20:03:38
 */
@Slf4j
@Component
public class HttpClientFactory {


    public ChannelFuture send(DefaultFullHttpRequest defaultFullHttpRequest) {
        ChannelFuture channelFuture = newClient("127.0.0.1", 8080);
        channelFuture.channel().writeAndFlush(defaultFullHttpRequest);
        return channelFuture;
    }

    /**
     * new一个新客户端
     *
     * @param ip
     * @param port
     * @return
     */
    private ChannelFuture newClient(String ip, int port) {
        try {
            ChannelFuture serverChannelFuture = SpringApplicationContext
                    .getBean("clientBootStrap", Bootstrap.class)
                    .connect(ip, port).sync();
            return serverChannelFuture;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("[连接服务端异常] ip:{} port:{}", ip, port);
            throw new RuntimeException("连接服务端异常");
        }
    }
}
