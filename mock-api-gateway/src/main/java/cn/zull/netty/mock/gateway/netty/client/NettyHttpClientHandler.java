package cn.zull.netty.mock.gateway.netty.client;

import cn.zull.netty.mock.gateway.netty.HttpContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zurun
 * @date 2019/7/29 19:53:41
 */
@Slf4j
public class NettyHttpClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 建立的链接
     */
    public static final AtomicInteger CONNECTIONS = new AtomicInteger(0);
    private final HttpContext httpContext;

    public NettyHttpClientHandler(HttpContext httpContext) {
        this.httpContext = httpContext;
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
            final String channelId = ctx.channel().id().asShortText();
            MDC.put("traceId", channelId);

            log.info("[接受到返回消息] channelId:{} ip:{} msg:{}", channelId, ctx.channel().remoteAddress(), msg);
            if (msg instanceof HttpResponse && msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                HttpResponse httpResponse = (HttpResponse) msg;
                httpContext.resp(ctx.channel(), httpResponse, content);

            }
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
        super.channelInactive(ctx);

        //TODO-zurun 从nettyClients中删除
    }

}
