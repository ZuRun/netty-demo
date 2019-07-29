package cn.zull.netty.mock.gateway.netty.client;

import cn.zull.netty.mock.gateway.netty.HttpContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zurun
 * @date 2019/7/29 19:51:51
 */
@Slf4j
@Configuration
public class NettyClientConfiguration {

    @Autowired
    HttpContext httpContext;

    @Bean
    public Bootstrap clientBootStrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)

//                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline()
//                                .addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
//                        .addLast(new HttpRequestEncoder())//客户端对发送的httpRequest进行编码
//                       .addLast(new HttpResponseDecoder())//客户端需要对服务端返回的httpresopnse解码
                                .addLast(new HttpClientCodec())//HttpClientCodec()包含了上面两种
                                //聚合
                                .addLast(new HttpObjectAggregator(1024 * 10 * 1024))

                                //解压
                                .addLast(new HttpContentDecompressor())

//                                .addLast("decoder", new HttpResponseDecoder())
//                                .addLast("encoder", new HttpRequestEncoder())
                                .addLast(new NettyHttpClientHandler(httpContext));
                    }
                });
        return bootstrap;
    }
}
