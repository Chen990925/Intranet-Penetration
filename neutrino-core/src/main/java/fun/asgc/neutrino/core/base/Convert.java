
package fun.asgc.neutrino.core.base;

import com.google.common.collect.Lists;
import fun.asgc.neutrino.core.util.CollectionUtil;

import java.util.List;

/**
 * 转换器
 * @author: chenjunlin
 * @date: 2024/5/15
 */
public interface Convert<S,T> {

    /**
     * 从目标内容转换为原内容
     * @param target
     * @return
     */
    S from(T target);

    /**
     * 从原内容转换为目标内容
     * @param source
     * @return
     */
    T to(S source);

    /**
     * 从目标内容转换为原内容
     * @param targetList
     * @return
     */
    default List<S> from(List<T> targetList) {
        List<S> list = Lists.newArrayList();
        if (CollectionUtil.isEmpty(targetList)) {
            return list;
        }
        for (T target : targetList) {
            list.add(from(target));
        }
        return list;
    }

    /**
     * 从原内容转换为目标内容
     * @param sourceList
     * @return
     */
    default List<T> to(List<S> sourceList) {
        List<T> list = Lists.newArrayList();
        if (CollectionUtil.isEmpty(sourceList)) {
            return list;
        }
        for (S source : sourceList) {
            list.add(to(source));
        }
        return list;
    }
}
