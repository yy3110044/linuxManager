package com.yy.linuxManager.controller.administration;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.yy.linuxManager.util.Command;
import com.yy.linuxManager.util.MyMap;
import com.yy.linuxManager.util.ResponseObject;

/**
 * 系统信息controller
 * @author yy
 *
 */
@RestController
@RequestMapping(value="/administration", method=RequestMethod.POST)
public class SystemInfoController {
	
	/**
	 * 返回内存使用情况
	 * @return
	 */
	@RequestMapping("/memInfo")
	public ResponseObject memInfo() {
		Command comm = Command.execute("free -b");
		List<String> result = comm.getStrListResult();
		String error = comm.getStrErrorResult();
		if(result.size() > 0) {
			MyMap map = new MyMap();
			String memInfo = result.get(1);//内存信息
			String[] memInfos = memInfo.split("\\s+");
			
			//内存使用情况
			map.put("mem", new MyMap().set("total", Long.valueOf(memInfos[1].trim()))
					                    .set("used", Long.valueOf(memInfos[2].trim()))
					                    .set("free", Long.valueOf(memInfos[3].trim()))
					                    .set("shared", Long.valueOf(memInfos[4].trim()))
					                    .set("buff_cache", Long.valueOf(memInfos[5].trim()))
					                    .set("available", Long.valueOf(memInfos[6].trim())));
			
			//swap使用情况
			String swapInfo = result.get(2);
			String[] swapInfos = swapInfo.split("\\s+");
			map.put("swap", new MyMap().set("total", Long.valueOf(swapInfos[1].trim()))
					                    .set("used", Long.valueOf(swapInfos[2].trim()))
					                    .set("free", Long.valueOf(swapInfos[3].trim())));
			
			return new ResponseObject(100, "success", map);
		} else {
			return new ResponseObject(101, "fail", new MyMap().set("error", error));
		}
	}
	
	/**
	 * 返回jre相关信息
	 * @return
	 */
	@RequestMapping("/jreInfo")
	public ResponseObject jreInfo(HttpServletRequest req) {
		MyMap map = new MyMap();
		ServletContext sc = req.getServletContext();
		map.set("availableProcessors", Runtime.getRuntime().availableProcessors());//处理器数量
		map.set("webContainer", sc.getServerInfo()); //web容器
		map.set("webRootPath", sc.getRealPath("/")); //web项目运行目录
		map.set("javaVersion", System.getProperty("java.version")); //java版本
		map.set("javaVendor", System.getProperty("java.vendor")); //java供应商
		map.set("javaHome", System.getProperty("java.home"));
		map.set("ip", req.getRemoteAddr()); //访问ip
		return new ResponseObject(100, "success", map);
	}
}