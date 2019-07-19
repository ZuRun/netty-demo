package cn.zull.netty.mock.common.global;


import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.constants.IMessage;

/**
 * @author zurun
 * @date 2018/9/17 21:41:11
 */
public class AssertException extends IflytekRuntimeException {
    public AssertException(String errMsg) {
        super(ErrorCode.ASSERT.ILLEGAL_ARGUMENT, errMsg);
    }

    public AssertException(IMessage errCode) {
        super(errCode);
    }

    public AssertException(IMessage errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
