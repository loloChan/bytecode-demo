package edu.study.bytecode.generate;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

/**
 * 生成一个for循环从1~100累加的方法
 */
public class LoopGenerator {

    /**
     * 生成类
     * @return
     */
    public static byte[] generate() {

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        //设置对象头信息：版本号、修饰符(public、protect等)、全类名、签名、父类、实现的接口
        classWriter.visit(
                V1_8,
                ACC_PUBLIC,
                "edu/study/bytecode/generate/LoopAccumulator",
                null,
                "java/lang/Object",
                null
        );

        //构造函数方法
        MethodVisitor constructor = classWriter.visitMethod(
                ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        //调用父类构造方法
        constructor.visitVarInsn(ALOAD, 0);
        constructor.visitMethodInsn(INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",false);
        constructor.visitInsn(RETURN);
        constructor.visitMaxs(1,1);
        constructor.visitEnd();

        //添加方法
        MethodVisitor methodVisitor = classWriter.visitMethod(
                ACC_PUBLIC,
                "sum",
                "()I",
                null,
                null
        );
        int resultIndex = 1;
        //int result = 0;
        methodVisitor.visitInsn(ICONST_0);
        methodVisitor.visitVarInsn(ISTORE, resultIndex);

        /*
            for(int i = 1; i <= 100; i++){
                result = result + i;
            }
        */
        //i = 1;
        int iIndex = 2;
        methodVisitor.visitInsn(ICONST_1);
        methodVisitor.visitVarInsn(ISTORE, iIndex);
        //若没有开启 ClassWriter.COMPUTE_FRAMES 自己计算StackMapFrame，则需要自己添加
        //此时，相对于上一个Basic Block 的 StackMapFrame 发生了变化，增加了两个local variables，操作数栈仍然为0
        //Basic Block 0 结束，记录此时的StackMapFrame状态
        //methodVisitor.visitFrame(F_APPEND,2,new Object[]{INTEGER,INTEGER},0,null);
        Label l1 = new Label();
        Label l0 = new Label();
        methodVisitor.visitLabel(l0);
        methodVisitor.visitVarInsn(ILOAD, iIndex);
        methodVisitor.visitVarInsn(BIPUSH,100);
        //当 i > 100时，跳到l1处
        methodVisitor.visitJumpInsn(IF_ICMPGT,l1);

        //累加操作
        methodVisitor.visitVarInsn(ILOAD,resultIndex);
        methodVisitor.visitVarInsn(ILOAD, iIndex);
        methodVisitor.visitInsn(IADD);
        methodVisitor.visitVarInsn(ISTORE, resultIndex);

        //i++
        //变量表下标iIndex的元素 +1
        methodVisitor.visitIincInsn(iIndex,1);
        //跳到l0
        methodVisitor.visitJumpInsn(GOTO,l0);

        methodVisitor.visitLabel(l1);
        //Basic Block 1 end,记录此时StackMapFrame的状态
        //与前一个StackMapFrame比，因为i的作用域已经结束，所以减少了1个local variable，操作数栈为空
        //methodVisitor.visitFrame(F_CHOP,1,new Object[]{INTEGER},0,null);
        //加载result
        methodVisitor.visitVarInsn(ILOAD, resultIndex);
        //返回
        methodVisitor.visitInsn(IRETURN);
        //若开启了ClassWriter.COMPUTE_MAXS 则需要调用visitMaxs方法触发计算，参数可以随便写。
        methodVisitor.visitMaxs(2,3);
        methodVisitor.visitEnd();

        return classWriter.toByteArray();
    }

}
