package cn.zull.netty.mock.gateway.netty;

import cn.zull.netty.mock.gateway.netty.client.HttpClientFactory;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zurun
 * @date 2019/7/29 20:07:36
 */
@Slf4j
@Component
public class HttpContext {
    @Autowired
    HttpClientFactory factory;
    private final Map<Channel, ChannelHandlerContext> mapping = new ConcurrentHashMap<>(1 << 10);

    public void execute(ChannelHandlerContext ctx, HttpRequest httpRequest, HttpContent content) {
        DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, httpRequest.method(), httpRequest.uri(), content.content());
        ChannelFuture channelFuture = factory.send(defaultFullHttpRequest);

        change(ctx, channelFuture.channel());
        response(ctx, httpRequest);
    }

    public void change(ChannelHandlerContext ctx, Channel respChannel) {
        mapping.put(respChannel, ctx);
    }

    public void resp(Channel reqChannel, HttpResponse httpRequest, HttpContent content) {
        ChannelHandlerContext ctx = mapping.get(reqChannel);
        // 1.设置响应
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, content.content());

        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, httpRequest.headers().get(HttpHeaderNames.CONTENT_TYPE));

        log.info("-=-返回--");
        // 2.发送
        // 注意必须在使用完之后，close channel
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);

    }

    private void response(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("msg", "ok");
//        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
        //                Unpooled.copiedBuffer(json.toString(), CharsetUtil.UTF_8));

    }
}
