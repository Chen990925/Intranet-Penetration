
package fun.asgc.neutrino.core.aop;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
@Slf4j
public class DogInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        try {
            log.info("拦截器 class:{} method:{} args:{} before", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
            inv.invoke();
            log.info("拦截器 class:{} method:{} args:{} after", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
        } catch (Exception e) {
            log.info("拦截器 class:{} method:{} args:{} error", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
            e.printStackTrace();
        }
    }

}
