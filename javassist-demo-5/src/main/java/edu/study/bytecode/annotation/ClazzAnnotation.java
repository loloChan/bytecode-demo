package edu.study.bytecode.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ClazzAnnotation {

    /**
     * 类描述
     * @return
     */
    String clazzDesc() default "";

    /**
     * alias
     * @return
     */
    String alias() default "";

    /**
     * 过期时间
     * @return
     */
    long timeOut() default 0;
}
