package edu.study.bytecode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MethodTag管理类
 */
public final class MethodTagManager {

    private MethodTagManager() {
    }

    private static final Map<Integer, MethodTag> methodTags = new ConcurrentHashMap<>();

    private static AtomicInteger idBuilder = new AtomicInteger(1);

    /**
     * 生成methodId
     * @param methodTag {@link MethodTag}
     * @return
     */
    public static int generateMethodId(MethodTag methodTag) {
        int methodId = idBuilder.getAndIncrement();
        methodTags.put(methodId, methodTag);
        return methodId;
    }


    /**
     * 通过methodId获取MethodTag
     * @param methodId
     * @return
     */
    public static MethodTag getMethodTagById(int methodId) {
        return methodTags.get(methodId);
    }
}
