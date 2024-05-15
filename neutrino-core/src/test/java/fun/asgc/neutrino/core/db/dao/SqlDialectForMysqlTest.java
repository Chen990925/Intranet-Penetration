
package fun.asgc.neutrino.core.db.dao;

import fun.asgc.neutrino.core.db.annotation.Id;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.util.Date;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
public class SqlDialectForMysqlTest {

    @Test
    public void test1() {
        SqlDialect sqlDialect = new SqlDialectForMysql();
        User user = new User();
        user.setName("11'");
        user.setAge(20);
        user.setSex("男");
        user.setEmail("11@qq.com");
        user.setCreateTime(new Date());
        System.out.println(sqlDialect.add(user));
    }


    @Accessors(chain = true)
    @Data
    public static class User {
        @Id
        private Long id;
        private String name;
        private Integer age;
        private String email;
        private String sex;
        private Date createTime;
        private Date updateTime;
    }
}
