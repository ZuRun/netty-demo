package cn.zull.netty.mock.common.constants;

/**
 *
 * @author zurun
 * @date 2018/3/20 23:34:24
 */
public interface ErrorCode {

    enum common implements IMessage {
        /**
         *
         */
        REQUEST_NOT_FOUND(404, "未找到请求路径!"),
        DEFAULT_ERROR_CODE(980, "请求失败!"),
        DEFAULT_BUSINESS_EXCEPTION_CODE(990, "业务异常"),
        DEFAULT_EXCEPTION_CODE(991, "业务异常"),
        DEFAULT_FAIL_CODE(999, "默认异常"),


        ;

        private Integer errCode;
        private String errMsg;


        common(Integer errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        @Override
        public Integer getErrCode() {
            return this.errCode;
        }

        @Override
        public String getErrMsg() {
            return this.errMsg;
        }
    }

    enum distributedLock implements IMessage {
        /**
         *
         */
        DEFAULT_LOCK_FAIL(820, "分布式锁异常"),
        DISTRIBUTED_LOCK_INTERRUPTED_EXCEPTION(821, "分布式锁中断异常"),
        GET_LOCK_TIMEOUT(822, "获取锁超时失败"),
        ANNOATION_ARGS_ERROR(823, "分布式方法锁参数异常"),
        ;

        private Integer errCode;
        private String errMsg;


        distributedLock(Integer errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        @Override
        public Integer getErrCode() {
            return this.errCode;
        }

        @Override
        public String getErrMsg() {
            return this.errMsg;
        }
    }

    enum ASSERT implements IMessage {
        /**
         *
         */
        ILLEGAL_ARGUMENT(800, "非法参数错误"),
        NOTNULL(801, "[Assertion failed] - the object argument must be null"),
        NOTEMPTY(802, "[Assertion failed] - this object must not be empty"),
        NOTTRUE(803, "this state invariant must be true"),
        NOTEQUALS(804, "this state invariant must be true"),
        ;

        private Integer errCode;
        private String errMsg;


        ASSERT(Integer errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        @Override
        public Integer getErrCode() {
            return this.errCode;
        }

        @Override
        public String getErrMsg() {
            return this.errMsg;
        }
    }

    enum cipher implements IMessage {
        /**
         *
         */
        AES_ENCRYPT_FAIL(860, "AES加密失败"),
        AES_DECRYPT_FAIL(861, "AES解密失败"),
        CIPHER_INSTANCE(862, "初始化cipher失败"),
        AES_ERROR(91, "调用aes失败");

        private int errCode;
        private String errMsg;


        cipher(int errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        @Override
        public Integer getErrCode() {
            return this.errCode;
        }

        @Override
        public String getErrMsg() {
            return this.errMsg;
        }
    }

}
