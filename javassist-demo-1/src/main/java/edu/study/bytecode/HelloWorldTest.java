package edu.study.bytecode;

import edu.study.bytecode.generator.HelloWorldGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 测试javassist生成HelloWorld
 */
public class HelloWorldTest{

    public static void main(String[] args) throws Exception {
        HelloWorldGenerator helloWorldGenerator = new HelloWorldGenerator();
        Class<?> clazz = helloWorldGenerator.generate();
        Method main = clazz.getDeclaredMethod("main", String[].class);
        main.invoke(clazz.newInstance(), new String[1]);
    }

}
