package edu.study.bytecode.demo2.process;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * ClassReader事件处理器
 */
public class ProfilingClassVisitor extends ClassVisitor {

    /**
     * 全类名
     */
    private String className;

    public ProfilingClassVisitor(ClassVisitor classVisitor,String className) {
        super(ASM5, classVisitor);
        this.className = className;
    }

    /**
     * 重写visitMethod方法
     * @param access
     * @param name
     * @param descriptor
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access,name,descriptor,signature,exceptions);
        if ("<init>".equals(name) || "main".equals(name)) {
            return methodVisitor;
        }
        return new ProfilingMethoidVisitor(methodVisitor,access,name,descriptor,className);
        //return super.visitMethod(access, name, descriptor, signature, exceptions);
    }


}
