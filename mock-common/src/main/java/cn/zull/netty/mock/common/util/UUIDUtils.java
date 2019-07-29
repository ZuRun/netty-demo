package cn.zull.netty.mock.common.util;

import java.util.UUID;

/**
 * @author zurun
 * @date 2018/10/7 23:52:34
 */
public class UUIDUtils {
    /**
     * 去横线的uuid
     *
     * @return
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
