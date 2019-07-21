package cn.zull.netty.mock.server.netty;

import cn.zull.netty.mock.common.util.AesUtils;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zurun
 * @date 2019/7/10 17:05:07
 */
@Slf4j
public class MockServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 链接数
     */
    public static final AtomicInteger CONNECTIONS = new AtomicInteger();

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("connections: " + CONNECTIONS.get());
        }, 0, 2, TimeUnit.SECONDS);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("[channelRegistered] ctx = {}", ctx);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("[连接建立] channelId:{} 连接数:{}", ctx.channel().id(), CONNECTIONS.incrementAndGet());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            final String channelId = ctx.channel().id().asShortText();
            log.info("[read] channelId:{} ip:{} local:{} msg:{}", channelId, ctx.channel().remoteAddress(), ctx.channel().localAddress(), msg);
            if (!(msg instanceof String)) {
                log.error("消息体格式不合法，请检查.");
                return;
            }


            ctx.writeAndFlush(":" + channelId + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 通道断开
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("[通道断开] channelId:{} 剩余连接数:{}", ctx.channel().id(), CONNECTIONS.decrementAndGet());

        super.channelInactive(ctx);

    }
}
