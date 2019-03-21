package org.cetc.vqm.user.dao;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.user.pojo.UserInfo;

public interface UserInfoMapper {
	
	//用户登录
	UserInfo login(String username,String password);
	
	//用户总数
	float ListAllUserCount();
	
	//查询所有用户
	List<Map<String, Object>> ListAllUser(Integer pagenum);
	
	//根据用户名称获取用户
	UserInfo getUser(String username);
	
	//添加用户
	void addUser(UserInfo user);

    void updateUser(UserInfo user);

    void deleteUserByUserId(Integer id);

	void editUser(String username, String password, String rolename);

	String getUserName(Integer id);

	List<Map<String, Object>> ListAllUser();

	

}
