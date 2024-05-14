
package fun.asgc.neutrino.core.aop;

import org.junit.Test;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
public class Test1 {

	@Test
	public void dogCall() {
		Dog dog = Aop.get(Dog.class);
		dog.call();
	}

	@Test
	public void dogSay() {
		Dog dog = Aop.get(Dog.class);
		System.out.println(dog.say("hello"));
	}
}
