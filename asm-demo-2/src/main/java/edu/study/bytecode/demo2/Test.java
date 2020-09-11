package edu.study.bytecode.demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        test_desc();
    }

    public void fun(int a,int b,int c) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {

        });

        Object[] objs = new Object[3];

        objs[0] = Integer.valueOf(a);
        objs[1] = Integer.valueOf(b);
        objs[2] = Integer.valueOf(c);

        System.out.println(objs);

    }

    public static void test_desc() {
        String desc = " (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;IJ[I[[Ljava/lang/Obj ect;Lorg/itstack/test/Req;)Ljava/lang/String;";
        Matcher m = Pattern.compile("(L.*?;|\\[{0,2}L.*?;|[ZCBSIFJD]|\\[{0,2} [ZCBSIFJD]{1})").matcher(desc.substring(0, desc.lastIndexOf(')') + 1));
        while (m.find()) {
            String block = m.group(1);
            System.out.println(block);
        }
    }

}
