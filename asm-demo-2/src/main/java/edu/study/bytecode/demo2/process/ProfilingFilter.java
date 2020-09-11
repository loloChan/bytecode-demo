package edu.study.bytecode.demo2.process;

import java.util.HashSet;
import java.util.Set;

/**
 * profiling过滤器
 */
@SuppressWarnings("all")
public final class ProfilingFilter {

    /**
     * 是否不需要注入增强代码
     * @param className
     * @return
     */
    public static boolean isNotNeedInject(String className) {
        if (className == null ||
                className.isEmpty()){
            return true;
        }
        if (!className.startsWith("edu/study/bytecode")){
            return true;
        }
        return false;
    }

}
