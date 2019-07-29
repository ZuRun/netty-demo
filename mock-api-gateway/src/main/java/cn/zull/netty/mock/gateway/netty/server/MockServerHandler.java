package cn.zull.netty.mock.gateway.netty.server;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
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
            if (msg instanceof HttpRequest && msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                HttpRequest httpRequest = (HttpRequest) msg;
                String uri = httpRequest.uri();
                HttpHeaders headers = httpRequest.headers();
                HttpMethod method = httpRequest.method();
//                content.content().toString(Charset.defaultCharset());

//                HttpResponse response =httpRequest.
//                CmptRequest cmptRequest = CmptRequestUtil.convert(ctx, msg);
//                CmptResult cmptResult = this.gatewayExecutor.execute(cmptRequest);
//                FullHttpResponse response = encapsulateResponse(cmptResult);
//                ctx.write(response);
//                ctx.flush();

                response(ctx, httpRequest);
            }


            ctx.writeAndFlush(":" + channelId + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        log.warn("[通道断开] channelId:{} 剩余连接数:{}", ctx.channel().id(), CONNECTIONS.decrementAndGet());

        super.channelInactive(ctx);

    }
}
