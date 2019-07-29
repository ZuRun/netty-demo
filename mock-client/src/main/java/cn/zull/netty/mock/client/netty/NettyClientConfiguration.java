package cn.zull.netty.mock.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author zurun
 * @date 2019-07-19 20:59:16
 */
@Slf4j
@Configuration
public class NettyClientConfiguration {
    @Value("1")
    private int workerCount;
    @Autowired
    @Qualifier("ServiceExecutor")
    Executor serviceExecutor;

    @Autowired
    ClientContext clientContext;

    @Bean
    public Bootstrap clientBootStrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline()
                                .addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
                                .addLast("decoder", new StringDecoder())
                                .addLast("encoder", new StringEncoder())
                                // 60s未写(心跳),600秒未读
                                .addLast(new IdleStateHandler(125, 60, 0, TimeUnit.SECONDS))
                                .addLast(new MockClientHandler(serviceExecutor, clientContext));
                    }
                });
        return bootstrap;
    }


    /**
     * 客户端EventLoopGroup,客户端bossGroup与workerGroup共用一个
     *
     * @return
     */
    @Bean(name = "clientGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup clientGroup() {
        return new NioEventLoopGroup();
    }
}
