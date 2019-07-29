package cn.zull.netty.mock.common.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @author zurun
 * @date 2018/10/11 16:31:39
 */
public class ValidateUtils {
    private static volatile Validator validator;

    public static <T> Set<ConstraintViolation<T>> validate(T t) {
        return validate(t, Default.class);
    }

    public static <T> Set<ConstraintViolation<T>> validate(T t, Class... var) {
        if (validator == null) {
            synchronized (ValidateUtils.class) {
                if (validator == null) {
                    validator = Validation.buildDefaultValidatorFactory().getValidator();
                }
            }
        }
        return validator.validate(t, var);
    }
}
