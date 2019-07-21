package cn.zull.netty.mock.client.netty;


import cn.zull.netty.mock.common.util.SpringApplicationContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zurun
 * @date 2019/7/10 19:56:58
 */
@Slf4j
@Component
public class MockClientFactory {
    @Autowired
    ClientContext clientContext;

    /**
     * new一个新客户端
     *
     * @param ip
     * @param port
     * @return
     */
    public ChannelFuture newClient(String ip, int port) {
        try {
            ChannelFuture serverChannelFuture = SpringApplicationContext
                    .getBean("clientBootStrap", Bootstrap.class)
                    .connect(ip, port).sync();
            clientContext.connected(serverChannelFuture.channel());
            return serverChannelFuture;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("[连接服务端异常] ip:{} port:{}", ip, port);
            throw new RuntimeException("连接服务端异常");
        }
    }


}