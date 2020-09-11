package edu.study.bytecode;

public class HelloTest extends ClassLoader{

    public static void main(String[] args) throws Exception {

        HelloGenerator generator = new HelloGenerator();
        generator.generate();

        /*HelloTest helloTest = new HelloTest();
        Class<?> clazz = helloTest.defineClass("edu/study/bytecode/HelloWorld", bytes,
                0, bytes.length);*/
    }

}
