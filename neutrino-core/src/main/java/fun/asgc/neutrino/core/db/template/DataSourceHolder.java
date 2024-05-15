
package fun.asgc.neutrino.core.db.template;

import fun.asgc.neutrino.core.db.dao.DBType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源持有者
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
public class DataSourceHolder {
    /**
     * 数据源
     */
    private DataSource dataSource;
    /**
     * 是否保持连接
     * 默认执行完SQL操作就归还连接
     * 在开启事务的情况下，事务操作完毕才能归还
     */
    private ThreadLocal<Boolean> keepConnectionCache = new ThreadLocal<>();
    /**
     * 数据库类型
     */
    private DBType dbType;

    public DataSourceHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSourceHolder(DataSource dataSource, DBType dbType) {
        this.dataSource = dataSource;
        this.dbType = dbType;
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void close(Connection conn) throws SQLException {
        conn.close();
    }

    public void tryClose(Connection conn) throws SQLException {
        Boolean keepConnection = keepConnectionCache.get();
        if (null == keepConnection || !keepConnection) {
            conn.close();
        }
    }

    public void setKeepConnection(Boolean keepConnection) {
        this.keepConnectionCache.set(keepConnection);
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }
}
