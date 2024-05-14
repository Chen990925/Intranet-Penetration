
package fun.asgc.neutrino.core.aop;

import org.junit.Test;

/**
 *
 * @author: wen.y
 * @date: 2022/6/24
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

	@Test
	public void catClimb() {
		Cat cat = Aop.get(Cat.class);
		cat.climb();
	}

	@Test
	public void catCalc() {
		Cat cat = Aop.get(Cat.class);
		System.out.println(cat.calc(10, 6));
	}

}
