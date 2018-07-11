package com.yy.linuxManager.controller.template;

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

	@RequestMapping("/login.html")
	public String login(HttpServletRequest req) {
		String msg = req.getParameter("msg");
		System.out.println(msg);
		req.setAttribute("msg", Util.urlDecode(msg));
		return "/login.html";
	}
}