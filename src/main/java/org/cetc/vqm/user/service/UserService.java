package org.cetc.vqm.user.service;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.user.dao.UserInfoMapper;
import org.cetc.vqm.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
    UserInfoMapper userInfoMapper;
	
	public UserInfo login(String Username, String Password) {
		// TODO Auto-generated method stub
		UserInfo user=userInfoMapper.login(Username,Password);
		return user;
	}
	
	public float ListAllUserCount()
	{
		return userInfoMapper.ListAllUserCount();
	}
	
	public List<Map<String, Object>> ListAllUser(Integer pagenum)
	{
		return userInfoMapper.ListAllUser(pagenum);
	}
	
	public UserInfo getUser(String username) {
        return userInfoMapper.getUser(username);
    }
	
	public void addUser(UserInfo user) {
		// TODO Auto-generated method stub
		userInfoMapper.addUser(user);
	}

    public void updateUser(UserInfo user) {
        userInfoMapper.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userInfoMapper.deleteUserByUserId(id);
    }

	public void editUser(String username, String password, String rolename) {
		// TODO Auto-generated method stub
		userInfoMapper.editUser(username,password,rolename);
	}

	public String getUserName(Integer id) {
		// TODO Auto-generated method stub
		return userInfoMapper.getUserName(id);
	}

	public List<Map<String, Object>> ListAllUser() {
		// TODO Auto-generated method stub
		return userInfoMapper.ListAllUser();
	}



	


}
