
package fun.asgc.neutrino.core.db.dao;

import com.alibaba.druid.pool.DruidDataSource;
import fun.asgc.neutrino.core.db.annotation.Column;
import fun.asgc.neutrino.core.db.annotation.Id;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 *
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
public class DaoTest {
    private DruidDataSource dataSource;
    private DBType dbType;

    {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/test1?useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("YWasgc@10520");
        dbType = DBType.MYSQL;
    }

    @Test
    public void add() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        User user = new User();
        user.setId(4L);
        user.setName("赵六");
        user.setAge(24);
        user.setEmail("zhaoliu@qq.com");
        user.setSex("女");
        user.setCreateTime22(new Date());
        userDao.add(user);
    }

    @Test
    public void findOneById() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        User user = userDao.findOneById(3);
        System.out.println(user);
    }

    @Test
    public void findOne1() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        User user = userDao.findOne(new User()
                .setId(1L), "id");
        System.out.println(user);
    }

    @Test
    public void findOne2() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        User user = userDao.findOne(new User()
                .setAge(23), "age");
        System.out.println(user);
    }

    @Test
    public void findOne3() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        User user = userDao.findOne(new User()
                .setAge(23)
                .setName("张三"), "age", "name");
        System.out.println(user);
    }

    @Test
    public void find1() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        List<User> userList = userDao.find();
        System.out.println(userList);
    }

    @Test
    public void find2() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        List<User> userList = userDao.find(new User().setAge(21), "age");
        System.out.println(userList);
    }

    @Test
    public void count1() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        System.out.println(userDao.count());
    }

    @Test
    public void count2() {
        Dao<User> userDao = new DefaultDaoImpl<>(dataSource, dbType, User.class);
        System.out.println(userDao.count(new User().setAge(21), "age"));
    }

    @ToString
    @Accessors(chain = true)
    @Data
    public static class User {
        @Id
        private Long id;
        private String name;
        private Integer age;
        private String email;
        private String sex;
        @Column("create_time")
        private Date createTime22;
        private Date updateTime;
    }
}
