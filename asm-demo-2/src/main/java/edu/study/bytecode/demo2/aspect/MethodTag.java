package edu.study.bytecode.demo2.aspect;

import java.util.List;

/**
 * 方法标志：用于存储方法的描述信息
 */
public class MethodTag {

    public MethodTag(int access, String fullClassName, String simpleClassName, String methodName,
                     String desc, List<String> parameterTypeList, String responseType) {
        this.access = access;
        this.fullClassName = fullClassName;
        this.simpleClassName = simpleClassName;
        this.methodName = methodName;
        this.desc = desc;
        this.parameterTypeList = parameterTypeList;
        this.returnParameterType = responseType;
    }

    public MethodTag() {
    }

    /**
     * 方法访问符
     */
    private int access;

    /**
     * 全类名
     */
    private String fullClassName;

    /**
     * 类名
     */
    private String simpleClassName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法描述符
     */
    private String desc;

    /**
     * 方法入参类型
     */
    private List<String> parameterTypeList;

    /**
     * 方法出参类型
     */
    private String returnParameterType;

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getParameterTypeList() {
        return parameterTypeList;
    }

    public void setParameterTypeList(List<String> parameterTypeList) {
        this.parameterTypeList = parameterTypeList;
    }

    public String getReturnParameterType() {
        return returnParameterType;
    }

    public void setReturnParameterType(String returnParameterType) {
        this.returnParameterType = returnParameterType;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }
}
