package fun.asgc.neutrino.core.base;

import com.alibaba.fastjson.JSONObject;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.core.util.Assert;
import fun.asgc.neutrino.core.util.CollectionUtil;
import fun.asgc.neutrino.core.util.StringUtil;
import fun.asgc.neutrino.core.util.TypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 * 一个默认的分发器实现，用于根据传入的数据类型,将任务分发给相应的处理器

 * 使用示例：
 * 首先，需要创建一些处理器类，并实现 Handler<Context, Data> 接口。
 * 然后，创建一个匹配器函数，根据传入的数据从中提取类型信息。
 * 最后，创建一个 DefaultDispatcher 实例，传入名称、处理器列表和匹配器函数，然后就可以使用 dispatch 方法进行任务分发了
 */



@Slf4j
public class DefaultDispatcher<Context, Data> implements Dispatcher<Context, Data> {
    /**
     * 调度器名称
     */
    private String name;
    /**
     * 处理器映射
     */
    private Map<String, Handler<Context,Data>> handlerMap;
    /**
     * 匹配器
     */
    private Function<Data,String> matcher;

    /** 构造函数接收三个参数：name、handlerList 和 matcher
     * name 是分发器的名称，不能为空。
     * handlerList 是处理器列表，它是一个泛型列表，其中的元素是实现了 Handler<Context, Data> 接口的类的实例。
     * matcher 是一个函数接口，用于从数据中提取出类型信息的功能函数。
      */
    public DefaultDispatcher(String name, List<? extends Handler<Context,Data>> handlerList, Function<Data,String> matcher) {
        Assert.notNull(name, "名称不能为空!");
        Assert.notNull(matcher, "匹配器不能为空!");
        if (CollectionUtil.isEmpty(handlerList)) {
            log.error("{} 处理器列表为空.", name);
            return;
        }
        this.name = name;
        this.handlerMap = new HashMap<>();
        this.matcher = matcher;

        for (Handler handler : handlerList) {
            Match match = handler.getClass().getAnnotation(Match.class);
            if (null == match) {
                log.warn("类: {} 缺失Match注解", handler.getClass().getName());
                continue;
            }
            if (StringUtil.isEmpty(match.type())) {
                log.warn("类: {} match注解缺失type参数！", handler.getClass().getName());
                continue;
            }
            if (handlerMap.containsKey(match.type())) {
                log.warn("类: {} match注解type值{} 存在重复!", handler.getClass().getName(), match.type());
                continue;
            }
            handlerMap.put(match.type(), handler);
        }
        log.info("{} 处理器初始化完成", name);
    }


    /**
     * 用于根据传入的数据类型，将任务分发给相应的处理器
     * 首先，它通过调用 matcher.apply(data) 方法来获取数据的类型信息。
     * 然后，它根据获取到的类型信息从 handlerMap 中获取对应的处理器。
     * 最后，它调用获取到的处理器的 handle 方法来处理数据
     */
    @Override
    public void dispatch(Context context, Data data) {
        if (CollectionUtil.isEmpty(handlerMap)) {
            return;
        }
        String type = matcher.apply(data);
        if (null == type) {
            log.warn("获取匹配类型失败 data:{]", JSONObject.toJSONString(data));
            return;
        }
        Handler<Context,Data> handler = handlerMap.get(type);
        if (null == handler) {
            log.warn("找不到匹配的处理器 type:{}", type);
            return;
        }
        String name = handler.name();
        if (StringUtil.isEmpty(name)) {
            name = TypeUtil.getSimpleName(handler.getClass());
        }
        log.debug("处理器[{}]执行.", name);
        handler.handle(context, data);
    }
}
