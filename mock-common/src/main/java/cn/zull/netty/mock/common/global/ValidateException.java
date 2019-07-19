package cn.zull.netty.mock.common.global;


import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.constants.IMessage;

/**
 * @author zurun
 * @date 2018/10/12 15:31:33
 */
public class ValidateException extends BusinessException {
    public ValidateException(String errMsg) {
        super(ErrorCode.ASSERT.ILLEGAL_ARGUMENT, errMsg);
    }

    public ValidateException(IMessage errCode) {
        super(errCode);
    }

    public ValidateException(IMessage errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
