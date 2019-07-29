package cn.zull.netty.mock.client.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * clientMock的上下文信息,根据channelId来区分连接
 *
 * @author zurun
 * @date 2019-07-19 20:59:16
 */
@Slf4j
@Component
public class ClientContext {
    /**
     * <channelId,channel>
     * mock需要支持100万连接,此map不需要扩容,因此loadFactor设置为1即可
     * 2^20=1 048 576
     */
    private final Map<String, Channel> client = new ConcurrentHashMap<>(2 ^ 20, 1);

    private final AtomicInteger write = new AtomicInteger(0);
    private final AtomicInteger read = new AtomicInteger(0);
    private volatile long lastTime = System.currentTimeMillis();


    // ------ 流量统计 -----

    /**
     * @param isWrite true为发送请求,false为读
     */
    public void add(boolean isWrite) {
        long now = System.currentTimeMillis();
        if (now - lastTime > 1000) {
            lastTime = now;
            write.set(1);
            read.set(1);
        } else {
            if (isWrite) {
                write.getAndIncrement();
            } else {
                read.getAndIncrement();
            }
        }
    }

    public JSONObject getTps() {
        JSONObject json = new JSONObject();
        long now = System.currentTimeMillis();
        if (now - lastTime > 1100) {
            json.put("time", now);
            json.put("write", 0);
            json.put("read", 0);
        } else {
            json.put("time", lastTime);
            json.put("write", write.get());
            json.put("read", read.get());
        }
        return json;
    }


    /**
     * 建立链接
     *
     * @param channel
     */
    public void connected(Channel channel) {
        final String channelId = channel.id().asShortText();
        client.put(channelId, channel);
    }


    /**
     * 删除连接信息
     *
     * @param channelId
     * @return
     */
    public boolean release(String channelId) {
        client.remove(channelId);
        return true;
    }

    public boolean release(Channel channel) {
        return release(channel.id().asShortText());
    }

    /**
     * 根据channelId获取channel信息
     *
     * @param channelId
     * @return
     */
    public Channel getChannel(String channelId) {
        return client.get(channelId);
    }


    // ------------   管理功能  -----------

    /**
     * 建立链接的
     *
     * @return
     */
    public int connectionSize() {
        return client.size();
    }

}
