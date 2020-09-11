package edu.study.bytecode;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法标签
 */
public class MethodTag {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 入参类型
     */
    private List<String> parameterTypes;

    /**
     * 入参名
     */
    private List<String> parameterNames;

    /**
     * 返回类型
     */
    private String responseType;

    /**
     * 是否静态方法
     */
    private boolean isStatic;

    /**
     * 方法描述符
     */
    private String descriptor;

    /**
     * 创建MethodTag
     * @param method {@link CtMethod}
     * @return
     */
    public static MethodTag build(CtMethod method,String className) {
        try {
            //方法信息
            MethodInfo methodInfo = method.getMethodInfo();
            MethodTag methodTag = new MethodTag();
            methodTag.setClassName(className);
            methodTag.setMethodName(method.getName());
            methodTag.setDescriptor(methodInfo.getDescriptor());
            methodTag.setResponseType(method.getReturnType().getName());
            //是否静态方法
            methodTag.setStatic((methodInfo.getAccessFlags() & Modifier.STATIC) != 0);
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
            //参数名称
            for (; i < parameterCount; i++) {
                variableNameList.add(localVariableAttribute.variableName(i));
            }
            methodTag.setParameterNames(variableNameList);
            return methodTag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MethodTag(String className, String methodName, List<String> parameterTypes,
                     List<String> parameterNames, String responseType, boolean isStatic,
                     String descriptor) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterNames = parameterNames;
        this.responseType = responseType;
        this.isStatic = isStatic;
        this.descriptor = descriptor;
    }

    public MethodTag() {
    }

    @Override
    public String toString() {
        return "MethodTag{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + parameterTypes +
                ", parameterNames=" + parameterNames +
                ", responseType='" + responseType + '\'' +
                ", isStatic=" + isStatic +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
