package edu.study.bytecode;

import edu.study.bytecode.generate.LoopGenerator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class LoopGeneratorTest extends ClassLoader{

    public static void main(String[] args) throws Exception {

        LoopGeneratorTest test = new LoopGeneratorTest();

        byte[] bytes = LoopGenerator.generate();

        FileOutputStream out = new FileOutputStream("C:\\Users\\loloChan\\Desktop\\asm\\LoopAccumulator.class");
        out.write(bytes);
        out.flush();
        out.close();

        Class<?> clazz = test.defineClass("edu.study.bytecode.generate.LoopAccumulator", bytes, 0, bytes.length);

        if (null == clazz) {
            return;
        }

        Method sum = clazz.getMethod("sum");

        Object result = sum.invoke(clazz.newInstance());

        System.out.println("sum result = " + Integer.parseInt(result.toString()));

    }
}
