package edu.study.bytecode.monitor;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ASM5;

@SuppressWarnings("all")
public class MyMethodMonitor extends ClassLoader {

    public static void main(String[] args) throws Exception{

        //ClassReader负责解释.class文件，并通过事件通知调用ClassVisitor对应的事件方法。
        ClassReader classReader = new ClassReader(MyMethod.class.getName());
        //ClassWriter是ClassVisitor的一个抽象实现，可以通过toByteArray()将修改过的二进制字节码输出。
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ProfilingClassVisitor(classWriter);
        //accept()方法开始对class文件进行解析
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);

        byte[] bytes = classWriter.toByteArray();

        FileOutputStream out = new FileOutputStream("C:\\Users\\loloChan\\Desktop\\asm\\MyMethod.class");
        out.write(bytes);
        out.flush();
        out.close();

        MyMethodMonitor myMethodMonitor = new MyMethodMonitor();
        Class<?> clazz = myMethodMonitor.defineClass("edu.study.bytecode.monitor.MyMethod",
                bytes,
                0,
                bytes.length);
        if (clazz == null) {
            return;
        }
        //获取方法
        Method method = clazz.getMethod("queryUserInfo", String.class);
        //执行方法
        if (null == method) {
            return;
        }
        Object result = method.invoke(clazz.newInstance(), "user1");
        System.out.println("函数执行成功：" + result);

    }

    /**
     * 继承ClassVisitor，重写visitMethod方法。
     * 在ClassReader对class文件进行解析时，解析到方法表时会触发ClassVisitor的visitMethod方法
     */
    static class ProfilingClassVisitor extends ClassVisitor {

        public ProfilingClassVisitor(ClassVisitor classVisitor) {
            super(ASM5,classVisitor);
        }

        //重写visitMethod方法

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor,
                                         String signature, String[] exceptions) {

            //若不是需要增强的方法，跳过
            if (!"queryUserInfo".equals(name)) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
            //需要增强的方法
            MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
            MethodVisitor mv = new ProfilingMethodVisitor(methodVisitor, access, name, descriptor);
            return mv;
        }
    }

    /**
     * 通过AdviceAdapter对传进来的方法进行增强
     */
    static class ProfilingMethodVisitor extends AdviceAdapter {

        private String methodName = "";
        protected ProfilingMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(ASM5, methodVisitor, access, name, descriptor);
            this.methodName = name;
        }

        /**
         * 方法入口处添加指令
         */
        @Override
        protected void onMethodEnter() {
            //方法入口调用System.nanoTime()
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "java/lang/System",
                    "nanoTime",
                    "()J",
                    false);
            //将返回的栈顶元素存入局部变量表下标为2
            mv.visitVarInsn(Opcodes.LSTORE, 2);
            mv.visitVarInsn(Opcodes.ALOAD,1);
        }

        /**
         * 方法出口添加指令
         * 方法出口处添加如下代码：
         * long endTime = System.nanoTime()
         * StringBuilder sb = new StringBuilder()
         * long result = endTime - startTime
         * sb.append(""方法执行耗时(纳秒)-> methodname :" + result)
         * System.out.println(sb.toString())
         * @param opcode
         */
        @Override
        protected void onMethodExit(int opcode) {
            if ((IRETURN <= opcode && opcode <= RETURN) || opcode == ATHROW) {
                //endTime
                mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                        "java/lang/System",
                        "nanoTime",
                        "()J",
                        false);
                //将startTime压入栈顶
                mv.visitVarInsn(Opcodes.LLOAD,2);
                //执行相减
                mv.visitInsn(Opcodes.LSUB);
                //将栈顶元素压入临时变量表
                mv.visitVarInsn(Opcodes.LSTORE, 2);
                //创建StringBuilder
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                //复制一个到栈顶
                mv.visitInsn(DUP);
                //执行构造函数
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                        "java/lang/StringBuilder",
                        "<init>",
                        "()V",
                        false);
                //将StringBuilder对象存入变量表
                //ldc字符串常量
                mv.visitLdcInsn("方法执行耗时(纳秒)-> " + methodName + " : ");
                //执行append方法
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false);
                //将结果压入操作数栈顶
                mv.visitVarInsn(Opcodes.LLOAD, 2);
                //继续append到StringBuilder中
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(J)Ljava/lang/StringBuilder;",
                        false);
                //ldc常量 " ms"
                mv.visitLdcInsn(" ns");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false);
                //执行sb.toString()
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "toString",
                        "()Ljava/lang/String;",
                        false);
                mv.visitVarInsn(Opcodes.ASTORE, 2);
                //获取System.out
                mv.visitFieldInsn(Opcodes.GETSTATIC,
                        "java/lang/System",
                        "out",
                        "Ljava/io/PrintStream;");
                mv.visitVarInsn(Opcodes.ALOAD,2);
                //执行：out.println(String)
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        "(Ljava/lang/String;)V",
                        false);
            }
        }
    }

}
