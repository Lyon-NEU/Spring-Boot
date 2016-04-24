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
}