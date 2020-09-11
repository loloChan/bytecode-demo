package edu.study.bytecode;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 监控工具类
 */
public final class MonitorUtil {

    /**
     * 私有化类实例
     */
    private MonitorUtil() { }

    /**
     * 输出监控信息
     * @param startNanos 方法开始时间
     * @param methodId 方法标签id
     * @param parameterValues 入参值
     * @param returnValue 返回值
     */
    public static void point(long startNanos, int methodId,
                             List<Object> parameterValues,Object returnValue){
        MethodTag methodTag = MethodTagManager.getMethodTagById(methodId);
        System.out.println("监控 - Begin");
        System.out.println("方法：" + methodTag.getClassName() + "." + methodTag.getMethodName());
        System.out.println("入参：" + JSON.toJSONString(methodTag.getParameterNames()) +
                " 入参[类型]：" + JSON.toJSONString(methodTag.getParameterTypes()) +
                " 入数[值]：" + JSON.toJSONString(parameterValues));
        System.out.println("出参：" + methodTag.getResponseType() + " 出参[值]：" + JSON.toJSONString(returnValue));
        System.out.println("耗时：" + (System.nanoTime() - startNanos) / 1000000 + "(ms)");
        System.out.println("监控 - End\r\n");
    }

    /**
     * 输出监控异常信息
     * @param methodId
     * @param throwable {@link Throwable} 异常信息
     */
    public static void point(int methodId, Throwable throwable) {
        MethodTag methodTag = MethodTagManager.getMethodTagById(methodId);
        System.out.println("监控 - Begin");
        System.out.println("方法：" + methodTag.getClassName() + "." + methodTag.getMethodName());
        System.out.println("异常：" + throwable.getMessage());
        System.out.println("监控 - End\r\n");
    }

}
