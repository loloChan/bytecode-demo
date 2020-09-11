package edu.study.bytecode;

import java.io.PrintStream;
import java.util.Arrays;

public class Test1 {

    public static void main(String[] args) {
    }

    public String queryUserInfo(int a, int b) {
        long startTime = System.nanoTime();
        Object[] objs = new Object[2];
        objs[0] = a;
        objs[1] = b;
        long endTime = System.nanoTime();
        System.out.println("queryUserInfo2 执行时间：" + (endTime - startTime) + "  参数为：" + Arrays.toString(objs));
        return "你好，精神小伙！";

    }

    public int sum() {
        //basic block 0 start
        int result = 0;
        int i = 1;
        for (; i <= 100; i++) { //basic block 0 end
            //basic block 1 start
            result = result + i;
            //basic block 1 end
        }
        //basic block 2 start
        return result;
        //basic block 2 end
    }

}
