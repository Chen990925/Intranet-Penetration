

package fun.asgc.neutrino.core.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: chenjunlin
 * @date: 2022/6/27
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Orderly<T> implements Comparable<Orderly> {
    private T data;
    private int order;

    @Override
    public int compareTo(Orderly o) {
        return getOrder() - o.getOrder();
    }
}
