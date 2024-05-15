
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/27
 */
@Slf4j
public class GlobalInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        log.info("全局拦截器1 class:{} method:{} args:{} before", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
        inv.invoke();
        log.info("全局拦截器1 class:{} method:{} args:{} after", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
    }

}
