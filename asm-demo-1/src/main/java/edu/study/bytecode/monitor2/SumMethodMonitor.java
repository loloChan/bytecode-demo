package edu.study.bytecode.monitor2;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ASM5;

public class SumMethodMonitor extends ClassLoader{

    public static void main(String[] args) throws Exception{

        //负责解析class文件
        ClassReader classReader = new ClassReader(SumMethod.class.getName());
        //写出byte数组
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        ClassVisitor cv = new SumMethodClassVisitor(classWriter);

        classReader.accept(cv, ClassReader.EXPAND_FRAMES);

        byte[] bytes = classWriter.toByteArray();

        /*FileOutputStream out = new FileOutputStream("C:\\Users\\loloChan\\Desktop\\asm\\SumMethod.class");
        out.write(bytes);
        out.flush();
        out.close();*/
        SumMethodMonitor monitor = new SumMethodMonitor();
        Class<?> clazz = monitor.defineClass("edu.study.bytecode.monitor2.SumMethod",
                bytes,
                0, bytes.length);
        if (null == clazz) {
            return;
        }
        Method addMethod = clazz.getMethod("add", int.class, int.class);
        Object result = addMethod.invoke(clazz.newInstance(), 10, 10);
        System.out.println("SumMethod.add result = " + result);

    }

    /**
     * ClassReader事件处理器
     */
    static class SumMethodClassVisitor extends ClassVisitor {
        public SumMethodClassVisitor( ClassVisitor classVisitor) {
            super(ASM5, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

            if (!"add".equals(name)) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new SumMethodVisitorAdvice(methodVisitor,access,name,descriptor);
        }
    }

    static class SumMethodVisitorAdvice extends AdviceAdapter{

        private String methodName = "";

        protected SumMethodVisitorAdvice(MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(ASM5, methodVisitor, access, name, descriptor);
            this.methodName = name;
        }

        /**
         * 在add方法头打印日志
         * MonitorLog.info(......)
         */
        @Override
        protected void onMethodEnter() {
            //将函数名压入操作数栈
            mv.visitLdcInsn(methodName);

            //创建int[]数组,大小为2
            mv.visitInsn(ICONST_2);
            mv.visitIntInsn(NEWARRAY, T_INT);
            mv.visitVarInsn(ASTORE, 3);

            //为数组下标1设置值
            mv.visitVarInsn(ALOAD, 3);
            //将下标压入操作数栈
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(IASTORE);

            //为数组下标2设置值
            mv.visitVarInsn(ALOAD, 3);
            mv.visitInsn(ICONST_1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitInsn(IASTORE);

            //将数组压入操作数栈
            mv.visitVarInsn(ALOAD, 3);
            //调用MonitorLog.info静态方法
            mv.visitMethodInsn(INVOKESTATIC,
                    "edu/study/bytecode/monitor2/MonitorLog",
                    "info",
                    "(Ljava/lang/String;[I)V",
                    false);
        }
    }


}
