package cn.zull.netty.mock.gateway.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author zurun
 * @date 2019/7/10 19:20:39
 */
@Slf4j
@Configuration
public class NettyConfiguration {
    @Value("2")
    private int bossCount;
    @Value("4")
    private int workerCount;
    @Value("${mock.netty.server.port}")
    private int port;

    /**
     * 服务端bootstrap
     *
     * @return
     */
    @Bean("serverBootstrap")
    public ServerBootstrap serverBootstrap() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
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
                        p.addLast(new MockServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(port).sync();

        return bootstrap;
    }


    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @PreDestroy
    public void stop() throws Exception {
//        channelFuture.channel().closeFuture().sync();
        System.out.println("---stop server");

    }
}
