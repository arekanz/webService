/*package com.webService.service.userSection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class userServiceImplement extends JdbcDaoSupport implements userService{
	@Autowired 
	DataSource dataSource;
	
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	@Override
	public void insertUser(user User) {
		String sql = "INSERT INTO user " +
				"(login, password , email ) VALUES (?, ?, ?)" ;
		getJdbcTemplate().update(sql, new Object[]{
				User.getLogin(), User.getPassword(), User.getEmail()	});
	}
	@Override
	public void insertUsers(final List<user> Users) {
		String sql = "INSERT INTO user " + "(login, password , email, ) VALUES (?, ?, ?)";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				user User = Users.get(i);
				ps.setString(1, User.getLogin());
				ps.setString(2, User.getPassword());
				ps.setString(3, User.getEmail());
			}
			
			public int getBatchSize() {
				return Users.size();
			}
		});

	}
	@Override
	public List<user> getAllUsers(){
		String sql = "SELECT * FROM user";
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		
		List<user> result = new ArrayList<user>();
		for(Map<String, Object> row:rows){
			user User = new user();
			User.setId((int)row.get("id"));
			User.setLogin((String)row.get("login"));
			User.setPassword((String)row.get("password"));
			User.setEmail((String)row.get("email"));
			User.setActivated((int)row.get("activated"));
			result.add(User);
		}
		
		return result;
	}
	@Override
	public user getUserById(String id) {
		String sql = "SELECT * FROM user WHERE id = "+id;
		return (user)getJdbcTemplate().queryForObject(sql, new RowMapper<user>(){
			@Override
			public user mapRow(ResultSet rs, int rwNumber) throws SQLException {
				user User = new user();
				User.setId(rs.getInt("id"));
				User.setLogin(rs.getString("login"));
				User.setPassword(rs.getString("password"));
				User.setEmail(rs.getString("email"));
				User.setActivated(rs.getInt("activated"));
				return User;
			}
		});
	}
	@Override
	public user getUserByLogin(String login) {
		String sql = "SELECT * FROM user WHERE login = '"+login+"'";
		user reT = (user)getJdbcTemplate().queryForObject(sql, new RowMapper<user>(){
			@Override
			public user mapRow(ResultSet rs, int rwNumber) throws SQLException {
				user User = new user();
				User.setId(rs.getInt("id"));
				User.setLogin(rs.getString("login"));
				User.setPassword(rs.getString("password"));
				User.setEmail(rs.getString("email"));
				User.setActivated(rs.getInt("activated"));
				return User;
			}
		});
		if(reT!=null)
			return reT;
		return null;
	}
}*/