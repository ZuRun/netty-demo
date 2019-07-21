package cn.zull.netty.mock.client.netty;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zurun
 * @date 2019-07-19 21:03:38
 */
@Slf4j
public class MockClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 建立的链接
     */
    public static final AtomicInteger CONNECTIONS = new AtomicInteger();

    private final Executor serviceExecutor;
    private final ClientContext clientContext;

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("connections: " + CONNECTIONS.get());
        }, 0, 2, TimeUnit.SECONDS);
    }

    public MockClientHandler(Executor executor, ClientContext clientContext) {
        this.serviceExecutor = executor;
        this.clientContext = clientContext;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("[channelRegistered] ctx = {}", ctx);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        MDC.put("traceId", ctx.channel().id().asShortText());
        log.info("[连接建立] channelId:{} 连接数:{}", ctx.channel().id().asShortText(), CONNECTIONS.incrementAndGet());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // 业务
            serviceExecutor.execute(() -> {
                // 记录tps
                clientContext.add(false);
                final String channelId = ctx.channel().id().asShortText();
                MDC.put("traceId", channelId);

                log.info("[read] channelId:{} ip:{} msg:{}", channelId, ctx.channel().remoteAddress(), msg);
                if (!(msg instanceof String)) {
                    log.warn("消息体格式不合法，请检查.");
                    return;
                }


            });
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
     * 通道断开,自动重连
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final String channelId = ctx.channel().id().asShortText();
        MDC.put("traceId", channelId);

        log.warn("[通道断开] 剩余连接数:{} channelId:{}", CONNECTIONS.decrementAndGet(), channelId);
        clientContext.release(channelId);
        super.channelInactive(ctx);

        //TODO-zurun 从nettyClients中删除
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 心跳
        if (evt instanceof IdleStateEvent) {
            final String channelId = ctx.channel().id().asShortText();
            MDC.put("traceId", channelId);

            IdleStateEvent e = (IdleStateEvent) evt;

            switch (e.state()) {

                case READER_IDLE: {
                    log.warn("[读超时] 主动断开");
                    ctx.close();
                    break;
                }
                case WRITER_IDLE: {
                    log.info("[写超时] 主动发心跳");
                    ctx.writeAndFlush("-heart-\r\n");
                    break;
                }
                default: {
                    log.info("[ALL_IDLE]");
                    break;
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
