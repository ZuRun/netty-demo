package cn.zull.netty.mock.gateway.netty.server;


import cn.zull.netty.mock.gateway.netty.HttpContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author zurun
 * @date 2019/7/10 19:20:39
 */
@Slf4j
@Configuration
public class NettyConfiguration {
    @Value("2")
    private int bossCount;
    @Value("8")
    private int workerCount;
    @Value("${mock.netty.server.port}")
    private int port;

    @Autowired
    HttpContext httpContext;

    /**
     * 服务端bootstrap
     *
     * @return
     */
    @Bean("serverBootstrap")
    public ServerBootstrap serverBootstrap() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        MockServerHandler bizHandler = new MockServerHandler(httpContext);
        bootstrap.group(bossGroup(), workerGroup())
                .channel(EpollServerSocketChannel.class)
//                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 接受与发送消息使用string类型
//                        p.addLast("lineBasedFrameDecoder", new LineBasedFrameDecoder(Integer.MAX_VALUE));
//                        p.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));

                        p.addLast("decoder", new HttpRequestDecoder());
                        p.addLast("encoder", new HttpResponseEncoder());
                        int maxContentLength = 2000;
                        // HTTP 消息的合并处理
                        p.addLast(new HttpObjectAggregator(maxContentLength * 1024));
                        p.addLast(new IdleStateHandler(3, 3, 0, TimeUnit.SECONDS));
                        p.addLast(bizHandler);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1280)
                // 多个线程绑定同一个地址和端口,提供吞吐量(否则bossGroup只生效一个)
                .option(EpollChannelOption.SO_REUSEPORT, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)

                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .bind(port).sync();
        bootstrap.bind(port).sync();
        return bootstrap;
    }


    //    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public EventLoopGroup bossGroup() {
        return new EpollEventLoopGroup(bossCount);
    }

    //    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public EventLoopGroup workerGroup() {
        return new EpollEventLoopGroup(workerCount);
    }

    @PreDestroy
    public void stop() throws Exception {
//        channelFuture.channel().closeFuture().sync();
        System.out.println("---stop server");

    }
}
