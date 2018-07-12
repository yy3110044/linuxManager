package com.yy.linuxManager.interceptor;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 文件下载Interceptor，用于下载文件后把临时文件删除
 * @author yy
 *
 */
public class FileDownloadInterceptor implements HandlerInterceptor {
	//下载完成后把临时文件删除
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		File file = new File(request.getServletContext().getRealPath(request.getServletPath()));
		File parentFile = new File(file.getParent());
		FileUtils.deleteQuietly(parentFile);
	}
}