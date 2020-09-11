package edu.study.bytecode.demo2;

import edu.study.bytecode.demo2.process.ProfilingTransformer;

import java.lang.instrument.Instrumentation;

/**
 * javaagent入口类
 */
public class PreMain {

    /**
     * JVM首先尝试调用这个方法
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ProfilingTransformer());
        //System.out.println("hhhh");
    }

    /**
     * 若没有实现上面的方法，则尝试调用此方法
     * @param agentArgs
     */
    public static void premain(String agentArgs) {

    }

}
