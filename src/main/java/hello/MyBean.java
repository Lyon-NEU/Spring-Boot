package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MyBean{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MyBean(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    public List<User> getList(){
        String sql = "select id,name from users";
        return (List<User>) jdbcTemplate.query(sql, new RowMapper<User>(){

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setID(rs.getInt("id"));
                user.setName(rs.getString("name"));
                return user;
            }

        });
    }
    //根据姓名来查询，返回列表
    public List<User> getUser(String name){
        String sql = "select * from users where name='"+name+"'";
        return (List<User>) jdbcTemplate.query(sql, new RowMapper<User>(){

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setID(rs.getInt("id"));
                user.setName(rs.getString("name"));
                return user;
            }

        });
    }
    //根据ID查询，返回惟一结果 
    public User getUserID(int id){
        return jdbcTemplate.queryForObject("select * from users where id= ?",new Object[]{id},User.class);
    }
    //新建用户
    // public String save(User user){
    //     return jdbcTemplate.update("insert into users(id,name) values(?,?)",user.getID(),user.getName());
    // }
}