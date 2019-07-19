package cn.zull.netty.mock.common.util;

import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.constants.IMessage;
import cn.zull.netty.mock.common.global.AssertException;
import cn.zull.netty.mock.common.global.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 断言,自行添加所需断言
 * 参考: {@link org.springframework.util.Assert}
 *
 * @author zurun
 * @date 2018/9/30 14:11:29
 */
@Slf4j
public class Assert {

    public static <T> void validate(T t) {

        Set<ConstraintViolation<T>> set = ValidateUtils.validate(t);
        if (set.size() > 0) {
            StringBuffer sb = new StringBuffer();
            set.forEach(tConstraintViolation -> {
                sb.append(tConstraintViolation.getMessage());
                sb.append(" | ");
            });
            throw new ValidateException(ErrorCode.ASSERT.ILLEGAL_ARGUMENT, sb.toString());
        }
    }

    public static void bothNotEmpty(String... str) {
        for (int i = 0, length = str.length; i < length; i++) {
            notEmpty(str[i]);
        }
    }

    public static void notEmpty(Object object) {
        notEmpty(object, ErrorCode.ASSERT.NOTEMPTY);
    }

    public static void notEmpty(Object object, FImessage<IMessage> iMessageFImessage, Supplier<String> messageSupplier) {

        if (object == null) {
            assertThrow(iMessageFImessage, messageSupplier);
        }
        if (object instanceof String) {
            String str = (String) object;
            if (StringUtils.isEmpty(str)) {
                assertThrow(iMessageFImessage, messageSupplier);
            }
            return;
        }
        //todo 其他类型 自行添加

    }


    @FunctionalInterface
    public interface FImessage<T> {
        T get();
    }

    public static void notEmpty(Object object, FImessage<IMessage> errorMsg) {
        notEmpty(object, errorMsg, () -> null);
    }

    public static void notEmpty(Object object, IMessage errorMsg) {
        notEmpty(object, () -> errorMsg, errorMsg::getErrMsg);
    }

    public static void notEmpty(Object object, Supplier<String> messageSupplier) {
        notEmpty(object, () -> ErrorCode.ASSERT.NOTEMPTY, messageSupplier);
    }

    public static void notEmpty(Object object, String message) {
        notEmpty(object, () -> message);
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        notNull(object, ErrorCode.ASSERT.NOTNULL, message);
    }

    public static void notNull(Object object, IMessage errorMsg) {
        notNull(object, errorMsg, errorMsg.getErrMsg());
    }

    public static void notNull(Object object, IMessage errorMsg, String message) {
        notNull(object, () -> errorMsg, () -> message);
    }

    public static void notNull(Object object, FImessage<IMessage> iMessageFImessage) {
        notNull(object, iMessageFImessage, () -> null);
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        notNull(object, () -> ErrorCode.ASSERT.NOTNULL, messageSupplier);
    }

    public static void notNull(Object object, FImessage<IMessage> iMessageSupplier, Supplier<String> messageSupplier) {
        if (object == null) {
            assertThrow(iMessageSupplier, messageSupplier);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new AssertException(ErrorCode.ASSERT.NOTTRUE, message);
        }
    }


    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals("assertEquals", expected, actual);
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        if (expected != null || actual != null) {
            if (expected == null || !expected.equals(actual)) {
                throw new AssertException(ErrorCode.ASSERT.NOTEQUALS, message);
            }
        }
    }

    private static void assertThrow(FImessage<IMessage> iMessageFImessage, Supplier<String> messageSupplier) {
        IMessage iMessage = iMessageFImessage.get();
        if (iMessage == null) {
            iMessage = ErrorCode.ASSERT.NOTEMPTY;
        }
        String message = messageSupplier.get();
        if (StringUtils.isEmpty(message)) {
            message = iMessage.getErrMsg();
        }
        log.warn("[断言失败] {}", message);
        throw new AssertException(iMessage, message);
    }

    public static void main(String[] args) {
        int i = 2;
        String[] s = "aa_s".split("_");
        assertEquals(i, s.length);
        System.out.println("----");
        String[] s2 = "aas".split("_");
        assertEquals(i, s2.length);
    }
}
