package edu.study.bytecode;

import edu.study.bytecode.generate.HelloWorldGenerator;
import edu.study.bytecode.generate.SumOfTwoNumbersGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class Main extends ClassLoader{
    public static void main(String[] args) throws Exception{

        Main m = new Main();
        byte[] bytes = SumOfTwoNumbersGenerator.generate();
        //assert "AsmSumOfTowNumbers".equals("AsmSumOfTowNumbers");

        Class<?> clazz = m.defineClassByName(
                "edu.study.bytecode.AsmSumOfTowNumbers",
                bytes,
                0,
                bytes.length
        );
        Method addMethod = clazz.getMethod("add", int.class, int.class);
        if (null == addMethod) {
            return;
        }
        Object o = addMethod.invoke(clazz.newInstance(), 10, 10);
        System.out.println("两数相加结果是：sum = " + o);

        /*输出到.class
        FileOutputStream out = new FileOutputStream("C:\\Users\\loloChan\\Desktop\\asm\\AsmSumOfTowNumbers.class");
        out.write(bytes);
        out.flush();
        out.close();*/
    }

    public Class<?> defineClassByName(String name,byte[] b,int off,int len) {
        Class<?> clazz = super.defineClass(
                name,
                b,
                off,
                len
        );
        return clazz;
    }


}
