 
package fun.asgc.neutrino.core.db.template;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/15
 */
public class JdbcTemplate {
    /**
     * 数据源持有者
     */
    private DataSourceHolder dataSourceHolder;
    /**
     * jdbc操作
     */
    private JdbcOperations jdbcOperations;

    public JdbcTemplate(DataSource dataSource) {
        this(new DataSourceHolder(dataSource));
    }

    public JdbcTemplate(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
        this.jdbcOperations = JdbcOperations.getInstance();
    }

    public int update(String sql, Object ...params){
        int res = -1;
        Connection conn = null;

        try {
            conn = dataSourceHolder.getConnection();
            res = jdbcOperations.executeUpdate(conn,sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dataSourceHolder.tryClose(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    public int update(SqlAndParams sqlAndParams) {
        return update(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public int updateByMap(String sql, Map<String,Object> params){
        return update(new SqlAndParams(sql, params));
    }

    public int updateByModel(String sql, Object model){
        return update(new SqlAndParams(sql, model));
    }

    public <T> T query(Class<T> clazz, String sql, Object ...params){
        T res = null;
        Connection conn = null;

        try {
            conn = dataSourceHolder.getConnection();
            res = jdbcOperations.executeQuery(conn, clazz,sql, params);
        } catch (SQLException e) {
            new RuntimeException(e);
        } finally {
            try {
                dataSourceHolder.tryClose(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    public <T> T query(Class<T> clazz, SqlAndParams sqlAndParams) {
        return query(clazz, sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public <T> T queryByMap(Class<T> clazz, String sql, Map<String,Object> params){
        return query(clazz, new SqlAndParams(sql, params));
    }

    public <T> T queryByModel(Class<T> clazz, String sql, Object model){
        return query(clazz, new SqlAndParams(sql, model));
    }

    public byte queryForByteByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForByte(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public byte queryForByteByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForByte(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public byte queryForByte(String sql, Object ...params){
        return query(byte.class, sql, params);
    }

    public short queryForShortByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForShort(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public short queryForShortByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForShort(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public short queryForShort(String sql, Object ...params){
        return query(short.class, sql, params);
    }

    public int queryForIntByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForInt(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public int queryForIntByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForInt(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public int queryForInt(String sql, Object ...params){
        return query(int.class, sql, params);
    }

    public Long queryForLongByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForLong(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public Long queryForLongByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForLong(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public Long queryForLong(String sql,Object ...params){
        return query(long.class, sql, params);
    }

    public float queryForFloatByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForFloat(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public float queryForFloatByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForFloat(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public float queryForFloat(String sql,Object ...params){
        return query(float.class, sql, params);
    }

    public double queryForDoubleByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForDouble(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public double queryForDoubleByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForDouble(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public double queryForDouble(String sql, Object ...params){
        return query(double.class, sql, params);
    }

    public char queryForCharByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForChar(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public char queryForCharByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForChar(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public char queryForChar(String sql, Object ...params){
        return query(char.class, sql, params);
    }

    public boolean queryForBooleanByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForBoolean(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public boolean queryForBooleanByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForBoolean(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public boolean queryForBoolean(String sql, Object ...params){
        return query(boolean.class, sql, params);
    }

    public String queryForStringByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForString(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public String queryForStringByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForString(sql, sqlAndParams.getParamArray());
    }

    public String queryForString(String sql, Object ...params){
        return query(String.class, sql, params);
    }

    public Map<String,Object> queryForMapByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForMap(sql, sqlAndParams.getParamArray());
    }

    public Map<String,Object> queryForMapByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForMap(sql, sqlAndParams.getParamArray());
    }

    public Map<String,Object> queryForMap(String sql, Object ...params){
        return (Map<String,Object>)query(HashMap.class, sql, params);
    }

    public <T> List<T> queryForList(Class<T> clazz, String sql, Object ...params){
        List<T> res = null;
        Connection conn = null;

        try {
            conn = dataSourceHolder.getConnection();
            res = jdbcOperations.executeQueryForList(conn, clazz, sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dataSourceHolder.tryClose(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    public <T> List<T> queryForList(Class<T> clazz, SqlAndParams sqlAndParams) {
        return queryForList(clazz, sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public <T> List<T> queryForListByMap(Class<T> clazz, String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForList(clazz, sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public <T> List<T> queryForListByModel(Class<T> clazz, String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForList(clazz, sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Byte> queryForListByteByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListByte(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Byte> queryForListByteByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListByte(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Byte> queryForListByte(String sql,Object ...params){
        return queryForList(byte.class, sql,params);
    }

    public List<Short> queryForListShortByMap(String sql, Map<String, Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListShort(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Short> queryForListShortByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListShort(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Short> queryForListShort(String sql, Object ...params){
        return queryForList(short.class, sql, params);
    }

    public List<Integer> queryForListIntByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListInt(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Integer> queryForListIntByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListInt(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Integer> queryForListInt(String sql, Object ...params){
        return queryForList(int.class, sql, params);
    }

    public List<Long> queryForListLongByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListLong(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Long> queryForListLongByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListLong(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Long> queryForListLong(String sql,Object ...params){
        return queryForList(long.class, sql, params);
    }

    public List<Float> queryForListFloatByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListFloat(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Float> queryForListFloatByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListFloat(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Float> queryForListFloat(String sql, Object ...params){
        return queryForList(float.class, sql, params);
    }

    public List<Double> queryForListDoubleByMap(String sql ,Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListDouble(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Double> queryForListDoubleByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListDouble(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Double> queryForListDouble(String sql, Object ...params){
        return queryForList(double.class, sql, params);
    }

    public List<Character> queryForListCharByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListChar(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Character> queryForListCharByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListChar(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Character> queryForListChar(String sql, Object ...params){
        return queryForList(char.class, sql, params);
    }

    public List<Boolean> queryForListBooleanByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListBoolean(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Boolean> queryForListBooleanByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListBoolean(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Boolean> queryForListBoolean(String sql, Object ...params){
        return queryForList(boolean.class, sql, params);
    }

    public List<String> queryForListStringByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListString(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<String> queryForListStringByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListString(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<String> queryForListString(String sql, Object ...params){
        return queryForList(String.class, sql, params);
    }

    public List<Map> queryForListMap(String sql, Object ...params){
        return queryForList(Map.class, sql, params);
    }

    public List<Map> queryForListMapByMap(String sql, Map<String,Object> params){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, params);
        return queryForListMap(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

    public List<Map> queryForListMapByModel(String sql, Object model){
        SqlAndParams sqlAndParams = new SqlAndParams(sql, model);
        return queryForListMap(sqlAndParams.getSql(), sqlAndParams.getParamArray());
    }

}
