package edu.study.bytecode.demo3.advice;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * 给整个方法添加try-catch代码块
 */
public class ProfilingMethodVisitor extends AdviceAdapter {

    private Label from = new Label();
    private Label to = new Label();
    private Label target = new Label();

    protected ProfilingMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(ASM6, methodVisitor, access, name, descriptor);
    }

    /**
     * 异常开始位置位于visitCode()之后
     */
    @Override
    protected void onMethodEnter() {
        //try 块开始
        mv.visitLabel(from);
        mv.visitTryCatchBlock(from,to,target,"java/lang/Exception");
    }

    @Override
    public void push(Type value) {
        super.push(value);
    }

    /**
     * 在调用visitMaxs之前生成异常表
     * @param maxStack
     * @param maxLocals
     */
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        //try块结束
        mv.visitLabel(to);
        //catch块开始
        mv.visitLabel(target);

        int local = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(ASTORE,local);

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ALOAD, local);
        //调用System.out.println方法
        mv.visitMethodInsn(INVOKEVIRTUAL,"java/lang/Exception","getMessage",
                "()Ljava/lang/String;",false);
        mv.visitMethodInsn(INVOKEVIRTUAL,"java/io/PrintStream",
                "println","(Ljava/lang/String;)V",false);
        //抛出异常
        mv.visitVarInsn(ALOAD,local);
        mv.visitInsn(ATHROW);
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }
}
