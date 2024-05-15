 
package fun.asgc.neutrino.core.db.template;

import fun.asgc.neutrino.core.base.Convert;
import fun.asgc.neutrino.core.util.StringUtil;

/**
 * 默认的列名转换器转换器
 * 代码 -> DB
 * @author: chenjunlin
 * @date: 2022/6/27
 */
public class DefaultColumnNameConvert implements Convert<String, String> {
    private static final DefaultColumnNameConvert instance = new DefaultColumnNameConvert();
    private static final char SEPARATOR = '_';

    private DefaultColumnNameConvert() {

    }

    public static DefaultColumnNameConvert getInstance() {
        return instance;
    }

    @Override
    public String from(String target) {
        if (StringUtil.isEmpty(target)) {
            return "";
        }
        boolean flag = false;
        StringBuilder sb = new StringBuilder(target.length());
        for (char c : target.toCharArray()) {
            if (SEPARATOR == c) {
                flag = true;
            } else {
                if (flag) {
                    sb.append(Character.toUpperCase(c));
                    flag = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String to(String source) {
        if (StringUtil.isEmpty(source)) {
            return "";
        }
        boolean flag = true;
        StringBuilder sb = new StringBuilder(source.length());
        for (char c : source.toCharArray()) {
            if (flag) {
                sb.append(Character.toLowerCase(c));
                flag = false;
            } else {
                if (Character.isUpperCase(c)) {
                    sb.append(SEPARATOR);
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
