 
package fun.asgc.neutrino.core.db.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
@Getter
@AllArgsConstructor
public enum DBType {
    MYSQL(1, "mysql"),
    SQL_SERVER(2, "sqserver"),
    ORACLE(3, "oracle"),
    SQL_LITE(4, "sqllite"),
    MONGO(5, "mongodb");
    private static final Map<Integer, DBType> typeMap = Stream.of(DBType.values()).collect(Collectors.toMap(DBType::getType, Function.identity()));
    private static final Map<String, DBType> nameMap = Stream.of(DBType.values()).collect(Collectors.toMap(DBType::getName, Function.identity()));

    private Integer type;
    private String name;

    public static DBType byType(Integer type) {
        return typeMap.get(type);
    }

    public static DBType byName(String name) {
        return nameMap.get(name);
    }
}
