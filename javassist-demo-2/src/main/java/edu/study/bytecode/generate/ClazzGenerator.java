package edu.study.bytecode.generate;

import javassist.*;

/**
 * 目标：创建一个类，包含一个常量字段 ：π = 3.14
 * 包含两个方法：一个是计算面积，另一个是计算两数之和
 */
@SuppressWarnings("all")
public class ClazzGenerator {

    public Class<?> generate() throws Exception {

        ClassPool classPool = ClassPool.getDefault();
        //若需要使用到除java/lang包以外的类时，需要import
        //使用 classPool.importPackage(packageName) 引入
        StringBuilder methodBody = new StringBuilder();

        //创建一个Demo类
        CtClass ctClass = classPool.makeClass("edu.study.bytecode.Demo");
        ctClass.setModifiers(Modifier.PUBLIC);

        //创建构造函数
        CtConstructor constructor = new CtConstructor(new CtClass[]{}, ctClass);
        constructor.setBody("{}");
        ctClass.addConstructor(constructor);

        //创建一个字段 π
        CtField field = new CtField(CtClass.doubleType, "PI", ctClass);
        field.setModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL);
        ctClass.addField(field,"3.14");

        //添加求圆面积方法
        CtMethod circularAreaMethod = new CtMethod(CtClass.doubleType, //返回类型
                "calculateCircularArea", //方法名
                new CtClass[]{CtClass.doubleType}, //参数类型
                ctClass);
        circularAreaMethod.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
        methodBody.delete(0, methodBody.length());
        methodBody.append("{\r\n");
        methodBody.append("double result = ");
        //通过 $n 的方法取参数，非静态方法0的位置为this，否则，0的位置不存在
        // 也就是说，无论是静态方法或非静态方法，取传入的参数都是从下标1开始
        methodBody.append("PI * $1 * $1;");
        methodBody.append("\r\n");
        //注意，javassist不会自动装箱拆箱，若返回类型是Double，需要手动调用Double.valueOf()
        methodBody.append("return result;");
        methodBody.append("}\r\n");
        circularAreaMethod.setBody(methodBody.toString());

        ctClass.addMethod(circularAreaMethod);

        //求两数之和方法
        CtMethod sumOfTwoNumbersMethod = new CtMethod(CtClass.intType, "sumOfTwoNumbers",
                new CtClass[]{CtClass.intType, CtClass.intType}, ctClass);
        sumOfTwoNumbersMethod.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
        methodBody.delete(0, methodBody.length());
        methodBody.append("{\r\n");
        methodBody.append("return $1 + $2;");
        methodBody.append("}\r\n");
        sumOfTwoNumbersMethod.setBody(methodBody.toString());
        ctClass.addMethod(sumOfTwoNumbersMethod);

        CtMethod testMethod = new CtMethod(CtClass.voidType, "test", new CtClass[]{}, ctClass);
        testMethod.setModifiers(Modifier.PUBLIC);
        testMethod.setBody("{System.out.println($0);}");
        ctClass.addMethod(testMethod);

        //输出到文件夹
        ctClass.writeFile("C:\\Users\\loloChan\\Desktop\\bytecode\\javassist");

        return ctClass.toClass();
    }

}
