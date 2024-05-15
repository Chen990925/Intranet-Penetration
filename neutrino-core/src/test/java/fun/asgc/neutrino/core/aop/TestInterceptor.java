
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/24
 */
@Slf4j
public class TestInterceptor implements Interceptor {

    public TestInterceptor() {
        System.out.println("拦截器1实例化");
    }

    @Override
    public void intercept(Invocation inv) {
        try {
            log.info("拦截器1 class:{} method:{} args:{} before", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
            inv.invoke();
            log.info("拦截器1 class:{} method:{} args:{} after", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
        } catch (Exception e) {
            log.info("拦截器1 class:{} method:{} args:{} error", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
            e.printStackTrace();
        }
    }

}
