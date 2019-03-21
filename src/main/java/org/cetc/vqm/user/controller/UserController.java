package org.cetc.vqm.user.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.cetc.vqm.log.pojo.LogInfo;
import org.cetc.vqm.log.service.LogService;
import org.cetc.vqm.user.dao.UserInfoImpl;
import org.cetc.vqm.user.pojo.UserInfo;
import org.cetc.vqm.user.service.UserService;
import org.cetc.vqm.user.util.IPUtil;
import org.cetc.vqm.user.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

@RestController
public class UserController {
	
	@Autowired // (required = false) Spring容器不再抛出异而是认为这两个属性为null。
	private UserService userService;
	
	@Autowired
	private LogService logService;
	
	//用户登录
	@PostMapping(value = "/user/login")
	//@RequestMapping(value = "/user/login",method = RequestMethod.GET)
	public Map<String, Object> login(@RequestParam("username") String username,
						@RequestParam("password") String password, HttpSession session,HttpServletRequest request) throws UnknownHostException {
		
		Map<String, Object> rs = new HashMap<String, Object>();
		
		String password1 = MD5Util.encode2hex(password);
		
		UserInfo user = userService.login(username, password1);
		
		//System.out.println(user);
		
		if(user == null)
		{
			// 登录失败，设置失败信息，并调转到登录页面
			rs.put("flag", "error");
			rs.put("msg", "登录名和密码错误，请重新输入");
			return rs;
		}
		
		session.setAttribute("loginUser", username);
		
		Date now = new Date();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		
		String nowdate = dateFormat.format( now ); 
		
		user.setLastlogintime(nowdate);
		
		String ip = request.getRemoteAddr();
		//String ip = IPUtil.getIpAddr(request);
		
		user.setLastloginip(ip);
        
        userService.updateUser(user);
        
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(user.getUsername());
		
		loginfo.setTime(Timestamp.valueOf(user.getLastlogintime()));
		loginfo.setOperation("用户登录");
		
		logService.insertLog(loginfo);
		
//		if(!StringUtils.isEmpty(username) && "123456".equals(password)) {
//			session.setAttribute("loginUser", username);
//			rs.put("flag","success");
//			rs.put("msg","查询成功");
//		}
		
		rs.put("rolename", user.getRolename());
		rs.put("flag", "success");
		rs.put("msg", "登录成功");

		return rs;
	}
	
	//用户列表
	//@GetMapping(value = "/user/getAllUsers")
	@PostMapping(value = "/user/getAllUsers")
	public Map<String, Object> getAllUsers(@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		float count = userService.ListAllUserCount();
		
		int countpage = (int) Math.ceil(count / 10);
		
		if (pagenum < 1 || countpage < 1) {
			
			int pagenum1 = 0;
			List<Map<String, Object>> users = userService.ListAllUser(pagenum1);
			rs.put("Data", users);
		} else if (pagenum <= countpage) {
			List<Map<String, Object>> users = userService.ListAllUser((pagenum - 1) * 10);
			rs.put("Data", users);
		} else {
			int pagenum1 = (countpage - 1) * 10;
			List<Map<String, Object>> users = userService.ListAllUser(pagenum1);
			rs.put("Data", users);
		}
		rs.put("count", count);
		rs.put("countpage", countpage);
		rs.put("flag", "success");
		rs.put("msg", "展示所有会场成功");
	
		return rs;
	}
	
	
	/**
     * 添加用户
     */
	@PostMapping("/user/addUsers")
	public Map<String, Object> addUsers(@RequestParam("username") String username, @RequestParam("password")
	String password,@RequestParam("rolename") String rolename, HttpSession session) {

		Map<String, Object> rs = new HashMap<String, Object>();
		
		UserInfo user1 = userService.getUser(username);
		if (user1 != null) {
			rs.put("flag", "error");
			rs.put("msg", "帐号已存在，请选择其它帐号");
			return rs;
		}
		

		String username1 = (String) session.getAttribute("loginUser");
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowdate = dateFormat.format( now );
		
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(username1);
		loginfo.setTime(Timestamp.valueOf(nowdate));
		String operation = "添加用户 " + username;
		loginfo.setOperation(operation);
		logService.insertLog(loginfo);
		
		UserInfo user = new UserInfo();
		user.setUsername(username);
		String password1  = MD5Util.encode2hex(password);
		user.setPassword(password1);
		user.setRolename(rolename);
		userService.addUser(user);

		rs.put("flag", "success");
		rs.put("msg", "添加成功");
		
		return rs;
	}
	
	/**
     * 更新用户信息
     */
    @PostMapping("/user/editUser")
    public Map<String, Object> editUser(@RequestParam("username") String username,
    		@RequestParam("password") String password,@RequestParam("rolename") String rolename, HttpSession session){

    	Map<String, Object> rs = new HashMap<String, Object>();		
	
    	
    	String password1  = MD5Util.encode2hex(password);
    	
    	userService.editUser(username,password1,rolename);
    	
    	rs.put("flag", "success");
    	
		rs.put("msg", "修改成功");
		
		String username1 = (String) session.getAttribute("loginUser");
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowdate = dateFormat.format( now );
		
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(username1);
		loginfo.setTime(Timestamp.valueOf(nowdate));
		String event = "修改账号 " + username + " 信息";
		loginfo.setOperation(event);
		logService.insertLog(loginfo);
    	
    	return rs;
    }

    //@DeleteMapping("/deleteUser/")
    @PostMapping("/user/deleteUser")
    Map<String, Object> deleteUser(@RequestParam("id")  Integer id, HttpSession session){
    	
    	Map<String, Object> rs = new HashMap<String, Object>();

    	String username = userService.getUserName(id);
    	
    	String username1 = (String) session.getAttribute("loginUser");
    	
    	if (username == username1) {
			rs.put("flag", "error");
			rs.put("msg", "不允许删除当前账户");
			return rs;
		}
    	
        userService.deleteUser(id);
        rs.put("flag", "success");
		rs.put("msg", "删除成功");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowdate = dateFormat.format( now );
		
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(username1);
		loginfo.setTime(Timestamp.valueOf(nowdate));
		String operation = "删除用户 " + username + " 信息";
		loginfo.setOperation(operation);
		logService.insertLog(loginfo);
		
		return rs;
    }

}
