package edu.study.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.util.HotSwapper;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class DemoTest {

    public static void main(String[] args){

        RandomCreator creator = new RandomCreator();

        new Thread(() -> {
            while (true) {
                //随机生成0~9的随机数
                int randomNum = creator.createRandomNum();
                System.out.println("create a random number : " + randomNum);
                //休眠1s
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //使用javassist进行热替换随机生成0~99的随机数
        // java - agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
        // 监听 8000 端口,在启动参数里设置
        try {
            HotSwapper hotSwapper = new HotSwapper(8000);

            ClassPool classPool = ClassPool.getDefault();
            classPool.importPackage("java.util.Random");
            CtClass ctClass = classPool.get(RandomCreator.class.getName());

            //获取方法
            CtMethod createRandomNum = ctClass.getDeclaredMethod("createRandomNum");
            //重写方法
            StringBuilder methodBody = new StringBuilder();
            methodBody.append("{\r\n");
            methodBody.append("int result = new Random().nextInt(100);\r\n");
            methodBody.append("return result;\r\n");
            methodBody.append("}\r\n");
            createRandomNum.setBody(methodBody.toString());

            //重新加载类
            System.out.println("重新加载RandomCreator类,产生0~99的随机数");
            hotSwapper.reload(RandomCreator.class.getName(), ctClass.toBytecode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
