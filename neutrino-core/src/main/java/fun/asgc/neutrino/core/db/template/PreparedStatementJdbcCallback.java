 
package fun.asgc.neutrino.core.db.template;

import fun.asgc.neutrino.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/15
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
            if (ArrayUtil.notEmpty(params)) {
                for(Object o : params){
                    sb.append(o.toString()).append(",");
                }

                if(sb.length() > 0 && sb.charAt(sb.length() - 1) == ','){
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            log.debug("params:" + sb.toString());
            pstm = conn.prepareStatement(this.getSql());
            if (ArrayUtil.notEmpty(params)) {
                for(int i = 0;i < params.length;i++){
                    pstm.setObject(i + 1, params[i]);
                }
            }
            res = this.execute(pstm);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
