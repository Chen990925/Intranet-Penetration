 
package fun.asgc.neutrino.core.db.template;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author: chenjunlin
 * @date: 2022/6/27
 */
@Slf4j
public abstract class PreparedStatementJdbcCallback<T> implements JdbcCallback<T> {

    @Override
    public T execute() {
        PreparedStatement pstm = null;
        Object[] params = this.getParams();
        Connection conn = getConnection();

        T res = null;
        try {
            log.debug("sql:" + this.getSql());
            StringBuffer sb = new StringBuffer();
            for(Object o : params){
                sb.append(o.toString()).append(",");
            }

            if(sb.length() > 0 && sb.charAt(sb.length() - 1) == ','){
                sb.deleteCharAt(sb.length() - 1);
            }
            log.debug("params:" + sb.toString());
            pstm = conn.prepareStatement(this.getSql());
            for(int i = 0;i < params.length;i++){
                pstm.setObject(i + 1, params[i]);
            }
            res = this.execute(pstm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * 获取参数
     * @return
     */
    abstract Object[] getParams();

    /**
     * 获取sql语句
     * @return
     */
    abstract String getSql();

    /**
     * 执行
     * @param ps
     * @return
     */
    abstract T execute(PreparedStatement ps);

    /**
     * 获取数据库连接
     * @return
     */
    abstract Connection getConnection();
}
