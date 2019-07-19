package cn.zull.netty.mock.common.model;


import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.constants.IMessage;

/**
 * @author zurun
 * @date 2018/3/11 13:14:18
 */
public class Result extends BaseResult {


    public boolean isSuccess() {
        return 0 == getCode();
    }

    public Result addResult(Object obj) {
        this.setData(obj);
        return this;
    }

    public static Result ok() {
        return generate();
    }

    public static Result ok(String message) {
        Result result = ok();
        result.setMessage(message);
        return result;
    }

    public static Result fail(String message) {
        return fail(ErrorCode.common.DEFAULT_EXCEPTION_CODE.getErrCode(), message);
    }

    public static Result fail(IMessage errorCode) {
        return fail(errorCode.getErrCode(), errorCode.getErrMsg());
    }

    public static Result fail(int code, String message) {
        Result result = generate();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public Result addData(Object data) {
        this.setData(data);
        return this;
    }

    private static Result generate() {
        Result result = new Result();
        return result;
    }
}
