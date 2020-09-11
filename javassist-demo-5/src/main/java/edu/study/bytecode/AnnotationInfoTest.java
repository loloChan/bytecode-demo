package edu.study.bytecode;

import edu.study.bytecode.annotation.ClazzAnnotation;
import edu.study.bytecode.annotation.MethodAnnotation;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.*;

/**
 * 用javassist获取注解信息测试
 */
public class AnnotationInfoTest {

    public static void main(String[] args) throws Exception {

        ClassPool classPool = ClassPool.getDefault();

        CtClass ctClass = classPool.get(ApiTest.class.getName());

        //获取类上注解
        Object[] annotations = ctClass.getAnnotations();
        for (Object annotation : annotations) {
            if (annotation instanceof ClazzAnnotation) {
                ClazzAnnotation clazzAnnotation = (ClazzAnnotation) annotation;
                String alias = clazzAnnotation.alias();
                String clazzDesc = clazzAnnotation.clazzDesc();
                long timeOut = clazzAnnotation.timeOut();
                System.out.println("clazzAnnotation clazzDesc: " + clazzDesc);
                System.out.println("clazzAnnotation alias: " + alias);
                System.out.println("clazzAnnotation timeout: " + timeOut);
            }else {
                System.out.println("Annotation: " + annotation.getClass().getName());
            }
        }

        //获取方法上注解
        CtMethod queryInfo = ctClass.getDeclaredMethod("queryInfo");
        MethodAnnotation annotation = (MethodAnnotation) queryInfo.getAnnotation(MethodAnnotation.class);
        String methodDesc = annotation.methodDesc();
        String methodName = annotation.methodName();
        System.out.println("methodAnnotation methodDesc: " + methodDesc);
        System.out.println("methodAnnotation methodName: " + methodName);

        //读取方法指令码
        MethodInfo methodInfo = queryInfo.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        CodeIterator iterator = codeAttribute.iterator();
        while (iterator.hasNext()) {
            int index = iterator.next();
            int code = iterator.byteAt(index);
            System.out.println("指令码：" + index + " > " + Mnemonic.OPCODE[code]);
        }

        //通过指令修改方法
        ConstPool constPool = methodInfo.getConstPool();
        Bytecode bytecode = new Bytecode(constPool);
        bytecode.addLdc("update");
        bytecode.addReturn(classPool.get("java/lang/String"));
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());

        System.out.println("=============================");
        //修改后的方法字节码
        codeAttribute = methodInfo.getCodeAttribute();
        iterator = codeAttribute.iterator();
        while (iterator.hasNext()) {
            int index = iterator.next();
            int code = iterator.byteAt(index);
            System.out.println("指令码：" + index + " > " + Mnemonic.OPCODE[code]);
        }
    }

}
