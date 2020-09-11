package edu.study.bytecode.generate;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@SuppressWarnings("all")
public class SumOfTwoNumbersGenerator {

    public static byte[] generate() {

        //创建类
        ClassWriter classWriter = new ClassWriter(0);
        //设置类头信息
        classWriter.visit(
                Opcodes.V1_8,//版本号
                Opcodes.ACC_PUBLIC, //修饰符
                "edu/study/bytecode/AsmSumOfTowNumbers",//全类名
                null, //签名
                "java/lang/Object", //父类全类名
                null //实现接口
        );

        //添加构造函数
        MethodVisitor emptyConstructor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        //调用父类构造函数
        emptyConstructor.visitVarInsn(Opcodes.ALOAD, 0);
        emptyConstructor.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",false
                );
        emptyConstructor.visitInsn(Opcodes.RETURN);
        emptyConstructor.visitMaxs(1,1);
        emptyConstructor.visitEnd();

        //添加方法
        MethodVisitor addMethod = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                "add",
                "(II)I",
                null,
                null
        );
        //添加方法指令
        //将变量表的元素压到操作数栈
        addMethod.visitVarInsn(Opcodes.ILOAD, 0);
        addMethod.visitVarInsn(Opcodes.ILOAD, 1);
        //执行IADD指令
        addMethod.visitInsn(Opcodes.IADD);
        //返回
        addMethod.visitInsn(Opcodes.IRETURN);
        addMethod.visitMaxs(2,2);
        //方法结束
        addMethod.visitEnd();
        //类结束
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

}
