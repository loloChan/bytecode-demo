package edu.study.bytecode.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MethodAnnotation {

    /**
     * 方法描述
     * @return
     */
    String methodDesc() default "";

    /**
     * 方法名
     * @return
     */
    String methodName() default "";

}
