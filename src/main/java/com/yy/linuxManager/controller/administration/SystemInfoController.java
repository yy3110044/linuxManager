package com.yy.linuxManager.controller.administration;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yy.linuxManager.util.Command;
import com.yy.linuxManager.util.MyMap;
import com.yy.linuxManager.util.ResponseObject;
import com.yy.linuxManager.util.Util;

/**
 * 系统信息controller
 * @author yy
 *
 */
@RestController
@RequestMapping(value="/administration", method=RequestMethod.POST)
public class SystemInfoController {
	private static final Logger logger = LogManager.getLogger(SystemInfoController.class);
	
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
		
		Command comm = Command.execute("whoami");
		String result = comm.getStrResult();
		comm.getStrErrorResult();
		map.set("whoami", result);
		return new ResponseObject(100, "success", map);
	}
	
	/**
	 * 执行一个服务器命令
	 * @param comm
	 * @return
	 */
	@RequestMapping("/execute")
	public ResponseObject execute(@RequestParam final String comm) {
		if(!Util.empty(comm)) {
			final ResponseObject ro = new ResponseObject();
			
			CommandThread ct = new CommandThread(comm, ro);
			Thread th = new Thread(ct);
			th.start();
			try {
				th.join(5000); //等待th线程执行完毕，只等5秒
			} catch (InterruptedException e) {
				logger.error(e.toString());
			}

			ct.destroy();
			if(ro.getCode() == 100) {
				return ro;
			} else {
				return new ResponseObject(102, "执行失败");
			}
		} else {
			return new ResponseObject(101, "执行命令不能为空");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(new SystemInfoController().execute("ls"));
	}

	private class CommandThread implements Runnable {
		private Command command;
		private ResponseObject ro;
		CommandThread(String comm, ResponseObject ro) {
			this.command = Command.execute(comm);
			this.ro = ro;
		}
		@Override
		public void run() {
			ro.setResult(new MyMap().set("result", command.getStrResult()).set("error", command.getStrErrorResult()));
			ro.setCode(100);
			ro.setMsg("success");
		}
		
		public void destroy() {
			if(command != null) command.destroy();
		}
	}
}