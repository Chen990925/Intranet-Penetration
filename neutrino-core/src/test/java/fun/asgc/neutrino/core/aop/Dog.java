
package fun.asgc.neutrino.core.aop;

/**
 *
 * @author: chenjunlin
 * @date: 2022/6/23
 */
@Intercept({DogInterceptor.class})
public class Dog {
	public Dog() {
		System.out.println("狗出生");
	}
	public void call() {
		System.out.println("汪汪汪");
	}

	public String say(String msg) {
		return "狗说:" + msg;
	}
}
