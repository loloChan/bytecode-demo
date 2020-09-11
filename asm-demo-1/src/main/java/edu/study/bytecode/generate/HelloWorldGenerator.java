package edu.study.bytecode.generate;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@SuppressWarnings("all")
public final class HelloWorldGenerator {

    public static byte[] generate() {

        ClassWriter classWriter = new ClassWriter(0);

        //设置对象头信息：版本号、修饰符(public、protect等)、全类名、签名、父类、实现的接口
        classWriter.visit(
                Opcodes.V1_8, //版本号
                Opcodes.ACC_PUBLIC, //修饰符
                "edu/study/bytecode/generate/AsmHelloWorld",//全类名
                null, //签名
                "java/lang/Object", //父类
                null //实现的接口
        );
        //添加方法
        //修饰符、方法名、描述符、签名、异常
        MethodVisitor mainMethod = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, //方法修饰符
                "main", //方法名
                "([Ljava/lang/String;)V", //方法描述符
                null, //签名
                null //异常
        );
        //添加方法指令
        //获取System.out静态字段属性
        mainMethod.visitFieldInsn(
                Opcodes.GETSTATIC,
                "java/lang/System", //field所属类
                "out", //field名称
                "Ljava/io/PrintStream;" //field的描述符
        );
        //加载常量 load constant
        mainMethod.visitLdcInsn("Hello World!");
        //调用PrintStream的方法
        mainMethod.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream", //方法所属类
                "println", //方法名
                "(Ljava/lang/String;)V", //方法描述符
                false //是否接口方法
        );
        //返回
        mainMethod.visitInsn(Opcodes.RETURN);

        //设置操作数栈深度和局部变量表大小
        mainMethod.visitMaxs(2, 1);

        //方法结束
        mainMethod.visitEnd();
        //类结束
        classWriter.visitEnd();
        //生成字节数据
        return classWriter.toByteArray();
    }

}
