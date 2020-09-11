package edu.study.bytecode;

import edu.study.bytecode.generate.ClazzGenerator;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Method;

public class DemoTest1 {
    public static void main(String[] args) throws Exception {
        ClazzGenerator generator = new ClazzGenerator();
        Class<?> clazz = generator.generate();

        Method method1 = clazz.getDeclaredMethod("calculateCircularArea", double.class);
        Method method2 = clazz.getDeclaredMethod("sumOfTwoNumbers", int.class, int.class);
        Method method3 = clazz.getDeclaredMethod("test");

        Object obj = clazz.newInstance();

        Object result1 = method1.invoke(obj, 5);
        Object result2 = method2.invoke(obj, 3, 5);
        method3.invoke(obj);
        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);

    }
}
