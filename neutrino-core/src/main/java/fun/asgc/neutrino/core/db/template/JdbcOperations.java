 
package fun.asgc.neutrino.core.db.template;

import fun.asgc.neutrino.core.util.ReflectUtil;
import fun.asgc.neutrino.core.util.TypeUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: chenjunlin
 * @date: 2022/6/27
 */
public class JdbcOperations {
    private static final JdbcOperations instance = new JdbcOperations();

    private JdbcOperations() {

    }

    public static JdbcOperations getInstance() {
        return instance;
    }

    /**
     * 通用执行方法
     * @param callback
     * @param <T>
     * @return
     */
    public <T> T execute(JdbcCallback<T> callback) {
        T res = null;
        try {
            res = callback.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    /**
     * 执行更新操作
     * @param conn
     * @param sql
     * @param params
     * @return
     */
    public int executeUpdate(final Connection conn , final String sql, final Object[] params) {
        return this.execute(new PreparedStatementJdbcCallback<Integer>(){
            @Override
            public Integer execute(PreparedStatement ps) {
                int res = -1;
                try{
                    res =  ps.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                return res;
            }
            @Override
            public Object[] getParams() {
                return params;
            }
            @Override
            public String getSql() {
                return sql;
            }
            @Override
            public Connection getConnection(){
                return conn;
            }
        });
    }

    /**
     * 执行单条查询操作
     * @param conn
     * @param clazz
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T executeQuery(final Connection conn,final Class<T> clazz,final String sql,final Object[] params) {
        return this.execute(new PreparedStatementJdbcCallback<T>() {

            @Override
            public T execute(PreparedStatement ps) {
                T obj = null;

                try{
                    ResultSet resultSet = ps.executeQuery();
                    if(resultSet.next()){
                        if(TypeUtil.isNormalBasicType(clazz)){
                            Object value = resultSet.getObject(1);
                            obj = TypeUtil.conversion(value, clazz);
                        }else if(TypeUtil.isMap(clazz)){
                            Map<String,Object> map = new HashMap<String,Object>();
                            obj = (T)map;

                            ResultSetMetaData rsmd = resultSet.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            for(int i = 1;i <= columnCount;i++){
                                String name = rsmd.getColumnName(i);
                                Object value = resultSet.getObject(i);
                                map.put(DbCache.fromColumnName(name), value);
                            }
                        }else{
                            obj = clazz.newInstance();
                            ResultSetMetaData rsmd = resultSet.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            for(int i = 1;i <= columnCount;i++){
                                String name = rsmd.getColumnName(i);
                                Object value = resultSet.getObject(i);
                                List<Field> fieldList = DbCache.getField(clazz, name);
                                ReflectUtil.setFieldValue(fieldList, obj, value);
                            }
                        }
                    }
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
                return obj;
            }
            @Override
            public Connection getConnection() {
                return conn;
            }
            @Override
            public Object[] getParams() {
                return params;
            }
            @Override
            public String getSql() {
                return sql;
            }
        });
    }

    /**
     * 执行多条查询操作
     * @param conn
     * @param clazz
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> executeQueryForList(final Connection conn, final Class<T> clazz, final String sql, final Object[] params) {
        return this.execute(new PreparedStatementJdbcCallback<List<T>>() {
            @Override
            public List<T> execute(PreparedStatement ps) {
                List<T> res = new ArrayList<T>();
                try{
                    ResultSet resultSet = ps.executeQuery();
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    while(resultSet.next()){
                        T obj = null;

                        if(TypeUtil.isNormalBasicType(clazz)){
                            Object value = resultSet.getObject(1);
                            obj = TypeUtil.conversion(value, clazz);
                        }else if(TypeUtil.isMap(clazz)){
                            Map<String,Object> map = new HashMap<String,Object>();
                            obj = (T)map;

                            for(int i = 1;i <= columnCount;i++){
                                String name = rsmd.getColumnName(i);
                                Object value = resultSet.getObject(i);
                                map.put(DbCache.fromColumnName(name), value);
                            }
                        }else{
                            System.out.println(clazz);
                            obj = clazz.newInstance();

                            for(int i = 1;i <= columnCount;i++){
                                String name = rsmd.getColumnName(i);
                                Object value = resultSet.getObject(i);
                                List<Field> fieldList = DbCache.getField(clazz, name);
                                ReflectUtil.setFieldValue(fieldList, obj, value);
                            }
                        }

                        res.add(obj);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

                return res;
            }

            @Override
            public Connection getConnection() {

                return conn;
            }

            @Override
            public Object[] getParams() {

                return params;
            }

            @Override
            public String getSql() {

                return sql;
            }

        });
    }
}
