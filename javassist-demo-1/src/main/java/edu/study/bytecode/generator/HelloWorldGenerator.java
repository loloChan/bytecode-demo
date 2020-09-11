package edu.study.bytecode.generator;

import javassist.*;

/**
 * 使用javassist生成一个HelloWorld.class输出hello world
 */
@SuppressWarnings("all")
public class HelloWorldGenerator {

    public Class<?> generate() throws Exception {

        //通过classPool创建类
        ClassPool classPool = ClassPool.getDefault();

        //创建classPool.makeClass创建类
        CtClass ctClass = classPool.makeClass("edu.study.bytecode.HelloWorld");

        //添加构造函数
        CtConstructor ctConstructor = new CtConstructor(
                new CtClass[]{}, //参数类型
                ctClass //所属class
        );
        //设置函数体
        ctConstructor.setBody("{}");
        //添加到ctClass中
        ctClass.addConstructor(ctConstructor);

        //创建方法
        CtMethod ctMethod1 = new CtMethod(
                CtClass.voidType, //返回类型
                "main", //方法名
                new CtClass[]{classPool.get(String[].class.getName())}, //参数类型
                ctClass //所属class
        );
        //设置修饰符
        ctMethod1.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
        StringBuilder methodBody = new StringBuilder();
        methodBody.append("{\r\n");
        methodBody.append("System.out.println(");
        methodBody.append("\"javassist generate hello world!\"");
        methodBody.append(");");
        methodBody.append("}\r\n");
        ctMethod1.setBody(methodBody.toString());
        //添加到ctClass中
        ctClass.addMethod(ctMethod1);

        //输出到文件夹
        ctClass.writeFile("C:\\Users\\loloChan\\Desktop\\bytecode\\javassist");

        return ctClass.toClass();
    }

}
