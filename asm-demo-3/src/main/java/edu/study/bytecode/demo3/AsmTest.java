package edu.study.bytecode.demo3;

import edu.study.bytecode.demo3.advice.ProfilingClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class AsmTest extends ClassLoader {

    public static void main(String[] args) throws Exception {

        ClassReader classReader = new ClassReader(Operation.class.getName());
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ProfilingClassVisitor(classWriter);

        classReader.accept(cv, ClassReader.EXPAND_FRAMES);

        byte[] bytes = classWriter.toByteArray();

        FileOutputStream out = new FileOutputStream("C:\\Users\\loloChan\\Desktop\\asm\\Operation.class");
        out.write(bytes);
        out.flush();
        out.close();

        AsmTest test = new AsmTest();
        Class<?> clazz = test.defineClass(Operation.class.getName(), bytes, 0, bytes.length);
        Method method = clazz.getMethod("divide", int.class, int.class);
        Object result = method.invoke(clazz.newInstance(), 10, 0);
        //System.out.println(result);

    }

}
