package edu.study.bytecode.demo2;

import edu.study.bytecode.demo2.process.ProfilingClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 该类用于输出生成或增强的class文件到磁盘中，并测试是否可正常运行。
 */
public class TestVisitor extends ClassLoader{

    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        ClassReader classReader = new ClassReader(ApiTest.class.getName());
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ProfilingClassVisitor(classWriter,"");

        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        byte[] bytes = classWriter.toByteArray();

        FileOutputStream out = new FileOutputStream("C:\\Users\\loloChan\\Desktop\\asm\\ApiTest.class");
        out.write(bytes);
        out.flush();
        out.close();
        TestVisitor testVisitor = new TestVisitor();
        Class<?> clazz = testVisitor.defineClass(ApiTest.class.getName(), bytes, 0, bytes.length);

        Method method = clazz.getMethod("queryUserInfo", int.class, int.class,int.class);

        method.invoke(clazz.newInstance(), 10, 10,10);
    }
}
