package edu.study.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * 创建一个包含clazzAnnotation和MethodAnnotation注解的Hello.class类
 */
public class HelloGenerator {

    public Class<?> generate() throws Exception{

        ClassPool classPool = ClassPool.getDefault();
        //classPool.importPackage("edu.study.bytecode.annotation.ClazzAnnotation;");
        //classPool.importPackage("edu.study.bytecode.annotation.MethodAnnotation;");

        CtClass ctClass = classPool.makeClass("edu.study.bytecode.HelloWorld");

        //创建queryInfo方法
        CtMethod queryInfoMethod = new CtMethod(CtClass.voidType, "queryInfo",
                new CtClass[]{classPool.get("java/lang/String")}, ctClass);
        queryInfoMethod.setModifiers(Modifier.PUBLIC);
        MethodInfo methodInfo = queryInfoMethod.getMethodInfo();
        ConstPool constPool = methodInfo.getConstPool();

        //创建类Annotation
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool,
                AnnotationsAttribute.visibleTag);
        Annotation clazzAnnotation = new Annotation("edu/study/bytecode/annotation/ClazzAnnotation",
                constPool);
        //添加注解成员信息
        clazzAnnotation.addMemberValue("clazzDesc", new StringMemberValue("你好明天！", constPool));
        clazzAnnotation.addMemberValue("alias", new StringMemberValue("hello", constPool));
        clazzAnnotation.addMemberValue("timeOut", new LongMemberValue(500L, constPool));
        annotationsAttribute.setAnnotation(clazzAnnotation);
        ctClass.getClassFile().addAttribute(annotationsAttribute);

        //创建方法注解
        AnnotationsAttribute methodAnnotationsAttribute = new AnnotationsAttribute(constPool,
                AnnotationsAttribute.visibleTag);
        Annotation methodAnnotation = new Annotation("edu/study/bytecode/annotation/MethodAnnotation",
                constPool);
        methodAnnotation.addMemberValue("methodName", new StringMemberValue("queryInfo", constPool));
        methodAnnotation.addMemberValue("methodDesc", new StringMemberValue("查询信息", constPool));
        methodAnnotationsAttribute.setAnnotation(methodAnnotation);
        methodInfo.addAttribute(methodAnnotationsAttribute);

        //编写字节码
        Bytecode bytecode = new Bytecode(constPool);
        bytecode.addGetstatic("java/lang/System", "out", "Ljava/io/PrintStream;");
        bytecode.addLoad(1, classPool.get("java/lang/String"));
        bytecode.addInvokevirtual("java/io/PrintStream", //方法所在类
                "println", //方法名
                "(Ljava/lang/String;)V" //方法描述符
        );
        bytecode.addReturn(CtClass.voidType);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());

        ctClass.addMethod(queryInfoMethod);

        ctClass.writeFile("C:\\Users\\loloChan\\Desktop\\bytecode\\javassist");

        return ctClass.toClass();
    }

}
