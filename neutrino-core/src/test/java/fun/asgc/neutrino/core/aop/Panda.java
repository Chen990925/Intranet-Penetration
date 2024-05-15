
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.interceptor.InnerGlobalInterceptor;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/27
 */
public class Panda {

    public Panda() {
        System.out.println("熊猫出生");
    }

    public void eat() {
        System.out.println("熊猫吃竹子");
    }

    @Intercept(exclude = InnerGlobalInterceptor.class)
    public void play(String project) {
        System.out.println("熊猫在玩" + project);
    }

    public Integer division(int x, int y) {
        return x / y;
    }

    public void say(String msg) {
        System.out.println("熊猫说：" + msg);
    }

    public String request(String a, String b) {
        return String.format("参数 a:%s b:%s 处理结果", a, b);
    }
}
