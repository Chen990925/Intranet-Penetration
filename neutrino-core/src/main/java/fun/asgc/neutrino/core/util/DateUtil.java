 
package fun.asgc.neutrino.core.util;

import fun.asgc.neutrino.core.cache.Cache;
import fun.asgc.neutrino.core.cache.MemoryCache;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
public class DateUtil {
    private static final Cache<String, SimpleDateFormat> sdfCache = new MemoryCache<>();

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        return LockUtil.doubleCheckProcess(
                () -> !sdfCache.containsKey(format),
                format,
                () -> sdfCache.set(format, new SimpleDateFormat(format)),
                () -> sdfCache.get(format)
        );
    }

    /**
     * 日期格式化
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        try {
            return getSimpleDateFormat(format).format(date);
        } catch (Exception e) {
            // ignore
        }
        return "";
    }
}
