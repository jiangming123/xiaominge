package org.cetc.vqm.user.dao;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoImpl implements UserInfoMapper{

	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public UserInfo login(String username,String password) {
	
		@SuppressWarnings("unchecked")
		List<UserInfo> userList = jdbcTemplate.query("select * from user WHERE username = ? and password = ?",
				new Object[]{username,password},new BeanPropertyRowMapper(UserInfo.class));
        if(userList != null && userList.size() > 0){
			UserInfo user = userList.get(0);
			return user;
		}else {
			System.out.println("no user");
			return null;
		}
	}
	
	@Override
	public float ListAllUserCount()
	{
		return jdbcTemplate.queryForObject("select count(*) from user", float.class);
	}
	
	@Override
	public List<Map<String, Object>> ListAllUser(Integer pagenum) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForList("select * from user order by uid limit ? ,10",new Object[] {pagenum});
	}
	
	@Override
    public UserInfo getUser(String username) {
        @SuppressWarnings("unchecked")
		List<UserInfo> userList = jdbcTemplate.query("select * from user WHERE username = ?",new Object[] {username},new BeanPropertyRowMapper(UserInfo.class));
        if(userList != null && userList.size() > 0){
            UserInfo user = userList.get(0);
            System.out.println(user);
            return user;
        }else {
        	System.out.println("no user");
            return null;
        }
    }
	
	@Override
	public void addUser(UserInfo user) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("INSERT INTO user(username,password,rolename,lastlogintime,lastloginip) VALUES (?,?,?,?,?)",user.getUsername(),user.getPassword(),user.getRolename(),"","");
	}

    @Override
    public void updateUser(UserInfo user){
        jdbcTemplate.update("UPDATE user SET password = ?, rolename = ?, lastlogintime = ?, lastloginip = ? WHERE username = ?",
        		user.getPassword(),user.getRolename(),user.getLastlogintime(), user.getLastloginip(),user.getUsername());
    }

    @Override
    public void deleteUserByUserId(Integer id) {
        jdbcTemplate.update("DELETE FROM user WHERE uid = ?",id);
    }

	@Override
	public void editUser(String username, String password, String rolename) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("UPDATE user SET password = ?, rolename = ? WHERE username = ?",
				password,rolename,username);
	}

	@Override
	public String getUserName(Integer id) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select username from user WHERE uid = ?",new Object[]{id},String.class);
	}

	@Override
	public List<Map<String, Object>> ListAllUser() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForList("select username from user");
	}

	

	



}
