package edu.study.bytecode.demo2;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * javaagent测试类
 */
public class ApiTest {

    public static void main(String[] args){
        ApiTest apiTest = new ApiTest();
        String s = apiTest.queryUserInfo(10, 10, 10);
        System.out.println("执行结果：" + s);

    }

    /**
     * 测试方法
     */
    public String queryUserInfo(int a, int b,int c){
        return "你好，精神小伙！";
    }

    /*public String queryUserInfo2(int userId, int age) throws InterruptedException {
        long startTime = System.nanoTime();
        Object[] objs = {userId, age};
        String var = "你好，精神小伙！";
        long endTime = System.nanoTime();
        System.out.println("queryUserInfo2 执行时间：" + (endTime - startTime) + "  参数为：" + Arrays.toString(objs));
        return var;
    }*/
}
