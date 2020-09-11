package edu.study.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试获取类和方法信息
 */
public class MethodInfoTest {

    public static void main (String[] args) throws Exception {

        ClassPool classPool = ClassPool.getDefault();

        CtClass ctClass = classPool.get(Calculator.class.getName());

        CtMethod[] methods = ctClass.getDeclaredMethods();

        if (null == methods) {
            return;
        }

        List<MethodTag> list = new ArrayList<>();
        for (CtMethod method : methods) {
            //方法信息
            MethodInfo methodInfo = method.getMethodInfo();
            MethodTag methodTag = new MethodTag();
            methodTag.setClassName(ctClass.getSimpleName());
            methodTag.setMethodName(method.getName());
            methodTag.setDescriptor(methodInfo.getDescriptor());
            methodTag.setResponseType(method.getReturnType().getName());
            //是否静态方法
            methodTag.setStatic((methodInfo.getAccessFlags() & Modifier.STATIC) == 0? false : true);
            //方法参数类型
            CtClass[] parameterTypes = method.getParameterTypes();
            int parameterCount = 0;
            List<String> parameterTypeList = new ArrayList<>();
            if (null != parameterTypes) {
                parameterCount = parameterTypes.length;
                for (CtClass parameterType : parameterTypes) {
                    parameterTypeList.add(parameterType.getName());
                }
            }
            methodTag.setParameterTypes(parameterTypeList);

            //方法入参名称
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute localVariableAttribute =
                    (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            List<String> variableNameList = new ArrayList<>();
            int i = 0;
            if (!methodTag.isStatic()) {
                parameterCount++;
                i++;
            }
            for (; i < parameterCount; i++) {
                variableNameList.add(localVariableAttribute.variableName(i));
            }
            methodTag.setParameterNames(variableNameList);
            list.add(methodTag);
        }

        for (MethodTag methodTag : list) {
            System.out.println(methodTag.toString());
        }

    }
}
