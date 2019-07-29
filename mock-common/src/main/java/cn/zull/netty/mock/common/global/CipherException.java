package cn.zull.netty.mock.common.global;

import cn.zull.netty.mock.common.constants.IMessage;

/**
 * @author zurun
 * @date 2018/5/12 12:10:21
 */
public class CipherException extends IflytekRuntimeException {

    public CipherException(IMessage errCode) {
        super(errCode);
    }
}
