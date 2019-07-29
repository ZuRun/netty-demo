package cn.zull.netty.mock.common.global;


import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.constants.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目中所有异常均继承此异常,不要自定义检查异常
 *
 * @author zurun
 * @date 2018/6/15 16:17:05
 */
public class IflytekRuntimeException extends RuntimeException {
    /**
     * 异常码/错误码
     */
//    protected IMessage errCode;
    private Integer errCode;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected IflytekRuntimeException(Throwable cause, IMessage errCode) {
        super(cause);
        this.errCode = errCode.getErrCode();
    }

    protected IflytekRuntimeException(Integer errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    protected IflytekRuntimeException(String errMsg) {
        super(errMsg);
        errCode = ErrorCode.common.DEFAULT_EXCEPTION_CODE.getErrCode();
    }

    protected IflytekRuntimeException(IMessage errCode) {
        super(errCode.getErrMsg());
        this.errCode = errCode.getErrCode();
    }

    protected IflytekRuntimeException(IMessage errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode.getErrCode();
    }


    public Integer getErrCode() {
        return errCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
