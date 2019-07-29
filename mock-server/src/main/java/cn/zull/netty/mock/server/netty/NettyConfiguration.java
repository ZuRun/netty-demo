package cn.zull.netty.mock.server.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final int bossCount = 2;
    private final int workerCount = 4;

    @Value("${cu.zull.netty.mock.server.nPort}")
    private int nPort;
    @Value("${cu.zull.netty.mock.server.beginPort}")
    private int beginPort;

    /**
     * 服务端bootstrap
     *
     * @return
     */
    @Bean("serverBootstrap")
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 接受与发送消息使用string类型
//                        p.addLast("lineBasedFrameDecoder", new LineBasedFrameDecoder(Integer.MAX_VALUE));
                        p.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));

                        p.addLast("decoder", new StringDecoder());
                        p.addLast("encoder", new StringEncoder());
                        p.addLast(new MockServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        /**
         *  绑定100个端口号
         */
        for (int i = 0; i < nPort; i++) {
            int port = beginPort + i;
            bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
                System.out.println("bind success in port: " + port);
            });
        }

        return bootstrap;
    }


    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Autowired
    @Qualifier("serverBootstrap")
    ServerBootstrap serverBootstrap;

//    @PostConstruct
//    public void start() throws InterruptedException {
//        serverBootstrap
//        channelFuture = serverBootstrap.bind(8521).sync();
//    }

    @PreDestroy
    public void stop() throws Exception {
//        channelFuture.channel().closeFuture().sync();
        System.out.println("---stop server");

    }
}
