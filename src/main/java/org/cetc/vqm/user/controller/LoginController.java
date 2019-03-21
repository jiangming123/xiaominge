package org.cetc.vqm.user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.cetc.vqm.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;
import org.cetc.vqm.chainstate.dao.ChainstateImpl;
import org.cetc.vqm.spring.SpringUtil;

@RestController
public class LoginController {
	
	@RequestMapping("/test1")
    public Object testSpringUtil1() {
        return SpringUtil.getBean(ChainstateImpl.class);
	}
	
//	@RequestMapping({"/","/login.html"})
//	public String login() {
//		return "login";
//	}
//	
	
//	@PostMapping(value = "/user/login")
//	//@RequestMapping(value = "/user/login",method = RequestMethod.GET)
//	public String login(@RequestParam("username") String username,
//			@RequestParam("password") String password,Map<String, Object> map) {
//		map.put("flag", "success");
//		return "index";
//	}
	
	
	//@PostMapping(value = "/user/login")
	//@RequestMapping(value = "/user/login",method = RequestMethod.GET)
	public Map<String, Object> login(@RequestParam("username") String username,
						@RequestParam("password") String password, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("hello2 world!");
		System.out.println(username);
		System.out.println(password);
		if(!StringUtils.isEmpty(username) && "123456".equals(password)) {
			session.setAttribute("loginUser", username);
			map.put("flag","success");
			map.put("msg","查询成功");
		}

		return map;
	}
}
