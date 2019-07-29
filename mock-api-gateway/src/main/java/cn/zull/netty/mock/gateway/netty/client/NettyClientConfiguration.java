package cn.zull.netty.mock.gateway.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zurun
 * @date 2019/7/29 19:51:51
 */
@Slf4j
@Configuration
public class NettyClientConfiguration {
    @Bean
    public Bootstrap clientBootStrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline()
                                .addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
                                .addLast("decoder", new HttpResponseDecoder())
                                .addLast("encoder", new HttpRequestDecoder())
                                .addLast(new NettyHttpClientHandler());
                    }
                });
        return bootstrap;
    }
}
