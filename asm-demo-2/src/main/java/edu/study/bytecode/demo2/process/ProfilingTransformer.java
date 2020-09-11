package edu.study.bytecode.demo2.process;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 类文件转换器
 */
@SuppressWarnings("all")
public class ProfilingTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        try {
            /**
             * note：需要把系统的类与第三方类库的类都过滤掉，只增强当前包中的类。不然会出现
             * Exception: java.lang.NoClassDefFoundError thrown from the UncaughtExceptionHandler in thread "main"
             * 暂时未知该异常产生的原因，猜测可能会类的加载有关。
             * 并且，监控类应该监控当前应用中的类，而不应该包含第三方类。
             */
            if (ProfilingFilter.isNotNeedInject(className)) {
                //不需要注入监控字节码
                return classfileBuffer;
            }
            System.out.println("PreMain: " + className);
            return getBytes(loader, className, classfileBuffer);
        } catch (Exception e) {
            System.out.println("ProfilingTransformer.transform : error");
            e.printStackTrace();
        }
        //System.out.println(className);
        return classfileBuffer;
    }

    /**
     * 重写类文件字节码
     * @param loader
     * @param className
     * @param classFileBuffer
     * @return
     */
    private byte[] getBytes(ClassLoader loader, String className, byte[] classFileBuffer) {
        ClassReader classReader = new ClassReader(classFileBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ProfilingClassVisitor(classWriter,className);
        classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
