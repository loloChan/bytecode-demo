package edu.study.bytecode.demo2.aspect;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 切点增强工具类
 */
public class ProfilingAspect {

    /**
     * methodId生成器
     */
    private static AtomicInteger idBuilder = new AtomicInteger(0);

    /**
     * methodId与MethodTag的映射集
     */
    private static Map<Integer, MethodTag> methodTagMap = new ConcurrentHashMap<>();

    /**
     * 输出监控信息
     * @param startNanos 方法执行时间
     * @param methodId 方法Id（方法标识符）
     * @param requestParams 请求参数
     * @param responseVal 出参值
     */
    public static void printInfo(long startNanos, int methodId,
                                 Object[] requestParams, Object responseVal) {
        MethodTag method = methodTagMap.get(methodId);
        System.out.println("监控 - Begin");
        System.out.println("类名：" + method.getFullClassName());
        System.out.println("方法：" + method.getMethodName());
        System.out.println("入参类型：" + method.getParameterTypeList());
        System.out.println("入数[值]：" + JSON.toJSONString(requestParams));
        System.out.println("出参类型：" + method.getReturnParameterType());
        System.out.println("出参[值]：" + JSON.toJSONString(responseVal));
        System.out.println("耗时：" + (System.nanoTime() - startNanos) / 1000000 + "(ms)");
        System.out.println("监控 - End\r\n");
    }

    /**
     * 根据MethodTag生成methodId
     * @param methodTag {@link MethodTag} 方法标签
     * @return 方法id
     */
    public static int generateMethodId(MethodTag methodTag) {
        if (null == methodTag) {
            return -1;
        }
        int methodId = idBuilder.getAndIncrement();
        methodTagMap.put(methodId, methodTag);
        return methodId;
    }

    /**
     * 根据方法描述符，解析出方法入参类型
     * @param methodDesc 方法描述符
     * @return
     */
    @SuppressWarnings("all")
    public static List<String> compileMethodRequestParams(String methodDesc) {
        if (null == methodDesc || methodDesc.isEmpty()) {
            return Collections.emptyList();
        }
        /*
        * note：该正则存在缺陷，无法识别基础类型数组，例如：int[] 只会解析出 I。
        * 代修复。
        */
        final Matcher m = Pattern.compile("(L.*?;|\\[{0,2}L.*?;|[ZCBSIFJD]|\\[{0,2} [ZCBSIFJD]{1})")
                .matcher(methodDesc.substring(0, methodDesc.lastIndexOf(')') + 1));
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    /**
     * 通过methodId获取methodTag
     * @param methodId
     * @return
     */
    public static MethodTag getMethodTagById(int methodId) {
        if (methodTagMap.containsKey(methodId)) {
            return methodTagMap.get(methodId);
        }
        return null;
    }

}
