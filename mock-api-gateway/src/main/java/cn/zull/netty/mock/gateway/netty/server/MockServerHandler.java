package cn.zull.netty.mock.gateway.netty.server;

import cn.zull.netty.mock.gateway.netty.HttpContext;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zurun
 * @date 2019/7/10 17:05:07
 */
@Slf4j
@ChannelHandler.Sharable
public class MockServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 链接数
     */
    public static final AtomicInteger CONNECTIONS = new AtomicInteger();
    private final HttpContext httpContext;

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("connections: " + CONNECTIONS.get());
        }, 0, 2, TimeUnit.SECONDS);
    }

    public MockServerHandler(HttpContext httpContext) {
        this.httpContext = httpContext;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            final String channelId = ctx.channel().id().asShortText();
            log.info("[read] channelId:{} channel:{}", channelId, ctx.channel());
            if (msg instanceof HttpRequest && msg instanceof HttpContent) {
//                HttpContent content = (HttpContent) msg;
//                HttpRequest httpRequest = (HttpRequest) msg;
//                String uri = httpRequest.uri();
//                HttpHeaders headers = httpRequest.headers();
//                HttpMethod method = httpRequest.method();
//                content.content().toString(Charset.defaultCharset());

//                HttpResponse response =httpRequest.
//                CmptRequest cmptRequest = CmptRequestUtil.convert(ctx, msg);
//                CmptResult cmptResult = this.gatewayExecutor.execute(cmptRequest);
//                FullHttpResponse response = encapsulateResponse(cmptResult);
//                ctx.write(response);
//                ctx.flush();

                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                response.headers().set("Connection", "close");

//            ctx.close();
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);


//                httpContext.execute(ctx, httpRequest, content);
            } else {
                ctx.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.channelRead(ctx, msg);
    }

    private void response(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("msg", "ok");
        // 1.设置响应
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(json.toString(), CharsetUtil.UTF_8));

        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, httpRequest.headers().get(HttpHeaderNames.CONTENT_TYPE));

        // 2.发送
        // 注意必须在使用完之后，close channel
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
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
        log.info("[通道断开] channelId:{} 剩余连接数:{}", ctx.channel().id(), CONNECTIONS.decrementAndGet());

        super.channelInactive(ctx);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 心跳
        if (evt instanceof IdleStateEvent) {
            final String channelId = ctx.channel().id().asShortText();

            IdleStateEvent e = (IdleStateEvent) evt;

            switch (e.state()) {

                case READER_IDLE: {
                    log.warn("[读超时] 主动断开");
                    ctx.close();
                    break;
                }
                case WRITER_IDLE: {
                    log.warn("[写超时] 主动断开");
                    ctx.close();
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
