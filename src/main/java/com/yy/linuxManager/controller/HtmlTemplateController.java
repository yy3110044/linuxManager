package com.yy.linuxManager.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yy.linuxManager.util.Util;

/**
 * html模版controller
 * @author yy
 *
 */
@Controller
public class HtmlTemplateController {

	@RequestMapping("/login")
	public String login(HttpServletRequest req) {
		String msg = req.getParameter("msg");
		req.setAttribute("msg", Util.urlDecode(msg));
		return "login";
	}
	
	/**
	 * 头部共用部分
	 * @return
	 */
	@RequestMapping("/head")
	public String head() {
		return "head";
	}
	
	/**
	 * 系统文件列表
	 * @return
	 */
	@RequestMapping("/fileList")
	public String fileList() {
		return "fileList";
	}
}