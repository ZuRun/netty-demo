package cn.zull.netty.mock.common.global;


import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;

/**
 * 全局异常处理
 * <p>
 * 不同类型的异常具体返回的http状态码,根据业务来调整,此项目可以都返回200,调用方根据errCode来判断是否成功
 *
 * @author zurun
 * @date 2018/6/20 15:32:27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 自定义业务异常
     *
     * @param e
     * @return 用于springBoot框架返回response的body
     * @ResponseStatus 返回的 HTTP 状态码为 500
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result> handlerException(BusinessException e) {
        log.warn("[全局捕获业务异常] eName:{} code:{} msg:{}", e.getClass().getSimpleName(), e.getErrCode(), e.getMessage());
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(e.getErrCode(), e.getMessage()), headers, HttpStatus.OK);

    }

    /**
     * 自定义非业务运行异常
     *
     * @param e
     * @return 用于springBoot框架返回response的body
     * @ResponseStatus 返回的 HTTP 状态码为 500
     */
    @ExceptionHandler(IflytekRuntimeException.class)
    public ResponseEntity<Result> handlerException(IflytekRuntimeException e) {
        e.printStackTrace();
        log.warn("[全局捕获非业务异常] eName:{} code:{} msg:{}", e.getClass().getSimpleName(), e.getErrCode(), e.getMessage());
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(e.getErrCode(), "服务端异常了"), headers, HttpStatus.OK);

    }

    /**
     * 接口参数校验错误
     *
     * @param e
     * @return 用于springBoot框架返回response的body
     * @ResponseStatus 返回的 HTTP 状态码为 500
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handlerException(MethodArgumentNotValidException e) {
        log.warn("[全局捕获参数校验异常] eName:{} msg:{}", e.getClass().getSimpleName(), e.getMessage());
        e.printStackTrace();
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(ErrorCode.ASSERT.ILLEGAL_ARGUMENT.getErrCode(), "请求参数非法"), headers, HttpStatus.OK);
    }

    /**
     * 异常,直接返回错误信息
     *
     * @param e
     * @return 用于springBoot框架返回response的body
     * @ResponseStatus 返回的 HTTP 状态码为 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handlerException(Exception e) {
        log.warn("[全局捕获未知异常] eName:{} msg:{}", e.getClass().getSimpleName(), e.getMessage());
        e.printStackTrace();
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(ErrorCode.common.DEFAULT_EXCEPTION_CODE.getErrCode(), "服务端未知异常"), headers, HttpStatus.OK);
    }

    /**
     * 404页面
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result> handlerException(NoHandlerFoundException e) {
        log.warn("[全局捕获404] eName:{} msg:{}", e.getClass().getSimpleName(), e.getMessage());
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(404, "未找到请求路径！"), headers, HttpStatus.NOT_FOUND);
    }

    /**
     * 请求method错误(post、get)
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result> handlerException(HttpRequestMethodNotSupportedException e) {
        log.warn("[全局捕获405] eName:{} msg:{}", e.getClass().getSimpleName(), e.getMessage());
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(405, "未找到请求路径！").addResult(e.getMessage()), headers, HttpStatus.NOT_FOUND);
    }

    /**
     * 请求错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Result> handlerException(ServletException e) {
        HttpHeaders headers = createHeaders();
        return new ResponseEntity<>(Result.fail(400, "请求错误！").addResult(e.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }


    /**
     * 生成header
     *
     * @return
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }
}
