 
package fun.asgc.neutrino.core.aop;

import com.alibaba.fastjson.JSONObject;
import fun.asgc.neutrino.core.aop.interceptor.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/27
 */
@Slf4j
public class Test2 {

    {
        // 注册全局拦截器
        InterceptorFactory.registerGlobalInterceptor(GlobalInterceptor.class);
        // 注册过滤器
        InnerGlobalInterceptor.registerFilter(new Filter() {
            @Override
            public boolean filtration(Class<?> targetClass, Method targetMethod, Object[] args) {
                if (targetClass == Panda.class && targetMethod.getName().equals("say")) {
                    return true;
                }
                return false;
            }
        });
        // 注册结果处理器
        InnerGlobalInterceptor.registerResultAdvice(new ResultAdvice() {
            @Override
            public Object advice(Class<?> targetClass, Method targetMethod, Object result) {
                if (targetClass == Panda.class && targetMethod.getName().equals("request")) {
                    JSONObject data = new JSONObject();
                    data.put("code", 0);
                    data.put("data", result);
                    return data.toJSONString();
                }
                return result;
            }
        });
        // 注册异常处理器
        InnerGlobalInterceptor.registerExceptionHandler(new ExceptionHandler() {
            @Override
            public boolean support(Exception e) {
                return e instanceof ArithmeticException;
            }

            @Override
            public Object handle(Exception e) {
                log.info("除数不能为0!");
                return null;
            }
        });
    }

    @Test
    public void eat() {
        Panda panda = Aop.get(Panda.class);
        panda.eat();
    }

    @Test
    public void play() {
        Panda panda = Aop.get(Panda.class);
        panda.play("滑板");
    }

    @Test
    public void division() {
        Panda panda = Aop.get(Panda.class);
        System.out.println(panda.division(10, 5));
        panda.division(10, 0);
    }

    @Test
    public void say() {
        Panda panda = Aop.get(Panda.class);
        panda.say("hello");
    }

    @Test
    public void request() {
        Panda panda = Aop.get(Panda.class);
        panda.request("xxx", "yyy");
    }
}
