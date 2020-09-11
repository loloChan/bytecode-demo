package edu.study.bytecode;

import java.util.Random;

public class RandomCreator {
    /**
     * 产生一个10以内随机数
     * @return
     */
    public int createRandomNum() {
        int result = new Random().nextInt(10);
        return result;
    }
}
