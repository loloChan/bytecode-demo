package edu.study.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.Method;
import java.util.List;

public class MonitorDemo extends ClassLoader{

    public static void main(String[] args) throws Exception {

        byte[] bytes = monitor();

        MonitorDemo monitorDemo = new MonitorDemo();

        Class<?> clazz = monitorDemo.defineClass(Calculator.class.getName(), bytes, 0, bytes.length);

        Method sumOfTwoNumbers = clazz.getDeclaredMethod("sumOfTwoNumbers", Integer.class, Integer.class);
        sumOfTwoNumbers.invoke(clazz.newInstance(), 10, 10);


    }

    //若返回值类型或入参类型是基本类型，需要进行类型转换才能使用
    public static byte[] monitor() throws Exception {

        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage("java.util.List");
        classPool.importPackage("java.util.ArrayList");
        CtClass ctClass = classPool.get(Calculator.class.getName());
        String clazzName = ctClass.getName();
        CtMethod[] methods = ctClass.getDeclaredMethods();

        //对方法进行增强
        for (CtMethod method : methods) {

            //创建MethodTag用于输出
            MethodTag methodTag = MethodTag.build(method, clazzName);
            int methodId = MethodTagManager.generateMethodId(methodTag);

            //插入一条记录方法开始时间戳
            //定义属性
            method.addLocalVariable("startNano", CtClass.longType);
            method.insertBefore("startNano = System.nanoTime();");

            //非基本类型，通过classPool+全类名获取类型对应的CtClass
            method.addLocalVariable("parameterValues",classPool.get(List.class.getName()));
            //将入参添加到parameterValues中
            List<String> parameterNames = methodTag.getParameterNames();
            for (int i = parameterNames.size() - 1; i >= 0; i--) {
                String name = parameterNames.get(i);
                method.insertBefore("parameterValues.add("+ name +");");
            }
            method.insertBefore("parameterValues = new ArrayList();");

            //方法后增强
            method.insertAfter("edu.study.bytecode.MonitorUtil.point(startNano,"+methodId+",parameterValues,$_);",false);

            //添加try cache包围整个方法
            method.addCatch("edu.study.bytecode.MonitorUtil.point(" + methodId + ",$e);throw $e;",classPool.get("java.lang.Exception"));
        }

        ctClass.writeFile("C:\\Users\\loloChan\\Desktop\\bytecode\\javassist");

        return ctClass.toBytecode();

    }
}
