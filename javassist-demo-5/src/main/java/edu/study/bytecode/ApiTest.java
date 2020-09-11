package edu.study.bytecode;

import edu.study.bytecode.annotation.ClazzAnnotation;
import edu.study.bytecode.annotation.MethodAnnotation;

import java.math.BigDecimal;

@ClazzAnnotation(clazzDesc = "注册测试Api类",alias = "ApiTestAlias", timeOut = 500)
public class ApiTest {

    @MethodAnnotation(methodDesc = "查询信息", methodName = "queryInfo")
    public String queryInfo() {
        System.out.println("hello");
        return "hello";
    }

}
