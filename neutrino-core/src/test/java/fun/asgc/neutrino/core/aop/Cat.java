
package fun.asgc.neutrino.core.aop;


public class Cat {
    public Cat() {
        System.out.println("猫出生");
    }

    public void climb() {
        System.out.println("猫在爬");
    }

    @Intercept(TestInterceptor2.class)
    public int calc(int x, int y) {
        System.out.println("猫在计算");
        return x + y;
    }
}
