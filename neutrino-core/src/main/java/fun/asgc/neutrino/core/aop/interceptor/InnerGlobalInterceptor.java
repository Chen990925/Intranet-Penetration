 
package fun.asgc.neutrino.core.aop.interceptor;

import fun.asgc.neutrino.core.aop.Invocation;
import fun.asgc.neutrino.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部的全局拦截器
 * @author:  chenjunlin
 * @date:  chenjunlin
 */
@Slf4j
public class InnerGlobalInterceptor implements Interceptor {
    private static final List<Filter> filterList = new ArrayList<>();
    private static final List<ResultAdvice> resultAdviceList = new ArrayList<>();
    private static final List<ExceptionHandler> exceptionHandlerList = new ArrayList<>();

    @Override
    public void intercept(Invocation inv) {
        try {
            log.debug("内置顶层拦截器 class:{} method:{} args:{} before", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
            if (CollectionUtil.notEmpty(filterList)) {
                for (Filter filter : filterList) {
                    if (filter.filtration(inv.getTargetClass(), inv.getTargetMethod(), inv.getArgs())) {
                        return;
                    }
                }
            }
            inv.invoke();
            Object result = inv.getReturnValue();
            log.info("内置顶层拦截器 class:{} method:{} args:{} result:{} after", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs(), result);

            if (CollectionUtil.notEmpty(resultAdviceList)) {
                for (ResultAdvice advice : resultAdviceList) {
                    result = advice.advice(inv.getTargetClass(), inv.getTargetMethod(), result);
                }
            }
            inv.setReturnValue(result);

            log.info("内置顶层拦截器 class:{} method:{} args:{} result:{} finished.", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs(), result);
        } catch (Exception e) {
            log.info("内置顶层拦截器 class:{} method:{} args:{} exception.", inv.getTargetClass().getName(), inv.getTargetMethod().getName(), inv.getArgs());
            if (CollectionUtil.notEmpty(exceptionHandlerList)) {
                for (ExceptionHandler handler : exceptionHandlerList) {
                    if (handler.support(e)) {
                        Object result = handler.handle(e);
                        inv.setReturnValue(result);
                        return;
                    }
                }
            }
        }
    }

    public static synchronized void registerFilter(Filter filter) {
        filterList.add(filter);
    }

    public static synchronized void registerResultAdvice(ResultAdvice resultAdvice) {
        resultAdviceList.add(resultAdvice);
    }

    public static synchronized void registerExceptionHandler(ExceptionHandler exceptionHandler) {
        exceptionHandlerList.add(exceptionHandler);
    }
}
