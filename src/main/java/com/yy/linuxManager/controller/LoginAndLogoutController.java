package com.yy.linuxManager.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yy.linuxManager.util.ResponseObject;

/**
 * 登陆以及退出controller
 * @author yy
 *
 */
@RestController
@RequestMapping(method=RequestMethod.POST)
public class LoginAndLogoutController {
	@Value("${web.config.admin-user-name}")
	private String adminUserName;
	
	@Value("${web.config.admin-pass-word}")
	private String adminPassWord;
	
	@RequestMapping("/login")
	public ResponseObject login(@RequestParam String adminUserName, @RequestParam String adminPassWord, HttpSession session) {
		if(this.adminUserName.equals(adminUserName) && this.adminPassWord.equals(adminPassWord)) {
			session.setAttribute("adminUserName", this.adminUserName);
			return new ResponseObject(100, "登陆成功");
		} else {
			return new ResponseObject(101, "用户名或密码错误");
		}
	}
	
	@RequestMapping(value="/logout")
	public ResponseObject logout(HttpSession session) {
		session.removeAttribute("adminUserName");
		session.invalidate();
		return new ResponseObject(100, "退出成功");
	}
}