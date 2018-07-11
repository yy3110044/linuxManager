package com.yy.linuxManager.controller.administration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.yy.linuxManager.util.ResponseObject;

/**
 * 文件管理controller
 * @author yy
 *
 */
@RestController
@RequestMapping(value="/administration", method=RequestMethod.POST)
public class FileAdminController {

	@RequestMapping("/fileList")
	public ResponseObject fileList() {
		return new ResponseObject(100, "file list ok!");
	}
}