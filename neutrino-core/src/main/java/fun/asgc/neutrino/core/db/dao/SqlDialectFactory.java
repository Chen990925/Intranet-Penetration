 
package fun.asgc.neutrino.core.db.dao;

import fun.asgc.neutrino.core.util.Assert;

/**
 * sql方言工厂 TODO 暂时只支持mysql
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
public class SqlDialectFactory {
    private static final SqlDialect mysql = new SqlDialectForMysql();

    public static SqlDialect getSqlDialect(DBType dbType) {
        Assert.notNull(dbType, "数据库类型不能为空!");
        switch (dbType) {
            case MYSQL: return mysql;
            default: {
                throw new RuntimeException("不支持的数据库类型!");
            }
        }
    }

}
