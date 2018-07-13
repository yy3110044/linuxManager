package com.yy.linuxManager.controller.administration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.yy.linuxManager.util.FileInfo;
import com.yy.linuxManager.util.ResponseObject;
import com.yy.linuxManager.util.Util;

/**
 * 文件管理controller
 * @author yy
 *
 */
@RestController
@RequestMapping(value="/administration", method=RequestMethod.POST)
public class FileAdminController {
	private static final Logger logger = LogManager.getLogger(FileAdminController.class);
	
	/**
	 * 文件上传
	 * @return
	 */
	@RequestMapping("/fileUpload")
	public ResponseObject fileUpload(@RequestParam MultipartFile file, HttpSession session) {
		String currentPath = (String)session.getAttribute("currentPath");
		if(!Util.empty(currentPath)) {
			if(file != null && file.getSize() > 0) {
				String originalFileName = file.getOriginalFilename(); //文件原始名称
				File newFile = new File(currentPath, originalFileName);
				if(!newFile.exists()) {
					try {
						file.transferTo(newFile);
						return new ResponseObject(100, "文件上传成功");
					} catch (IllegalStateException | IOException e) {
						return new ResponseObject(104, "文件上传失败，请检查是否有权限");
					}
				} else {
					return new ResponseObject(103, "文件名重复，请重新命令，或删除重复的文件");
				}
			} else {
				return new ResponseObject(102, "上传失败，上传文件为空");
			}
		} else {
			return new ResponseObject(101, "上传失败，当前目录不存在");
		}
	}
	
	/**
	 * 创建一个目录
	 * @return
	 */
	@RequestMapping("/createFolder")
	public ResponseObject createFolder(@RequestParam String folderName, HttpSession session) {
		String currentPath = (String)session.getAttribute("currentPath");
		if(!Util.empty(currentPath)) {
			if(!Util.empty(folderName)) {
				File file = new File(currentPath, folderName);
				try {
					FileUtils.forceMkdir(file);
					return new ResponseObject(100, "创建目录成功");
				} catch (IOException e) {
					return new ResponseObject(103, "创建目录失败，请检查是否有权限");
				}
			} else {
				return new ResponseObject(102, "目录名不能为空");
			}
		} else {
			return new ResponseObject(101, "创建失败，当前目录不存在");
		}
	}
	
	/**
	 * 修改文件以及目录的名称
	 * @param newName
	 * @param session
	 * @return
	 */
	@RequestMapping("/reName")
	public ResponseObject reName(@RequestParam String oldName, @RequestParam String newName, HttpSession session) {
		String currentPath = (String)session.getAttribute("currentPath");
		if(!Util.empty(currentPath)) {
			if(!Util.empty(oldName, newName)) {
				File oldFile = new File(currentPath, oldName);
				File newFile = new File(currentPath, newName);
				try {
					oldFile.renameTo(newFile);
					return new ResponseObject(100, "重命名成功");
				} catch (Exception e) {
					return new ResponseObject(103, "重命名失败");
				}
			} else {
				return new ResponseObject(102, "旧名称以及新名称不能为空");
			}
		} else {
			return new ResponseObject(101, "重命名失败，当前目录不存在");
		}
	}
	
	/**
	 * 文件下载
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/fileDownload", method=RequestMethod.GET)
	public void fileDownload(@RequestParam String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		File file = new File(path);
		if(file.exists() && !file.isDirectory()) { //暂时只支持下载文件，下载目录功能，以后再支持
			String tmpFolder = UUID.randomUUID().toString().replaceAll("-", "");
			String fileTmpUrl = "/tmp/" + tmpFolder + "/" + Util.urlEncode(file.getName());
			File destFile = new File(req.getServletContext().getRealPath("/tmp/") + tmpFolder, file.getName());
			logger.debug("下载：" + file.getPath() + "，临时路径：" + destFile.getPath());
			FileUtils.copyFile(file, destFile);
			resp.sendRedirect(fileTmpUrl);
		} else {
			resp.getWriter().print("file is not valid!");
		}
	}
	
	/**
	 * 文件删除
	 * @param path
	 * @return
	 */
	@RequestMapping("/fileDelete")
	public ResponseObject fileDelete(@RequestParam String path) {
		if(FileUtils.deleteQuietly(new File(path))) {
			return new ResponseObject(100, "删除成功");
		} else {
			return new ResponseObject(101, "删除失败，可能是没有权限或者文件不存在");
		}
	}

	/**
	 * 文件列表
	 * @param path
	 * @param session
	 * @return
	 */
	@RequestMapping("/fileList")
	public ResponseObject fileList(String path, HttpSession session) {
		if(Util.empty(path)) { //没有path
			String currentPath = (String)session.getAttribute("currentPath");
			if(Util.empty(currentPath)) { //当前path也没空，就进入根目录
				path = "/";
			} else {
				path = currentPath;
			}
		}
		session.setAttribute("currentPath", path); //重设当前path
		
		File file = new File(path);
		if(!file.exists()) { //文件不存在
			return new ResponseObject(101, "文件不存在");
		} else { //文件存在
			if(file.isDirectory()) { //此文件是个目录
				File[] files = file.listFiles();

				if(files != null) {
					List<FileInfo> fileInfoList = new ArrayList<FileInfo>();  //文件信信列表
					for(File f : files) {
						FileInfo fi = new FileInfo();
						fi.setFileName(f.getName());
						fi.setFilePath(f.getPath());
						fi.setRead(f.canRead());
						fi.setWrite(f.canWrite());
						fi.setExecute(f.canExecute());
						fi.setHidden(f.isHidden());
						fi.setDirectory(f.isDirectory());
						fi.setLength(f.length());
						fi.setLengthStr(FileUtils.byteCountToDisplaySize(fi.getLength()));
						fileInfoList.add(fi);
					}
					Collections.sort(fileInfoList); //给list排下序
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileInfoList", fileInfoList);
					map.put("currentFolderList", getAllParent(path));
					map.put("currentPath", path);
					return new ResponseObject(100, "返回成功", map);
				} else {
					String currentPath = file.getParent();
					session.setAttribute("currentPath", currentPath);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("currentPath", currentPath);
					return new ResponseObject(103, "此目录下为空，或者无法访问", map);
				}
			} else { //此文件不是目录，提示是否下载此文件
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("currentPath", path);
				return new ResponseObject(102, "不是目录，是否下载此文件", map);
			}
		}
	}
	//返回目录的所有父目录
	private List<FileInfo> getAllParent(String folder) {
		List<FileInfo> list = new ArrayList<FileInfo>();
		File file = new File(folder);
		list.add(getFileInfoByFile(file));
		
		String str = null;
		while(!Util.empty(str = file.getParent())) {
			file = new File(str);
			FileInfo fi = new FileInfo();
			if(file.getPath().equals("/")) {
				fi.setFileName("/");
			} else {
				fi.setFileName(file.getName());
			}
			fi.setFilePath(file.getPath());
			fi.setDirectory(file.isDirectory());
			fi.setExecute(file.canExecute());
			fi.setWrite(file.canWrite());
			fi.setRead(file.canRead());
			fi.setHidden(file.isHidden());
			list.add(fi);
		}
		return list;
	}
	private FileInfo getFileInfoByFile(File file) {
		FileInfo fi = new FileInfo();
		fi.setFilePath(file.getPath());
		if(fi.getFilePath().equals("/")) {
			fi.setFileName("/");
		} else {
			fi.setFileName(file.getName());
		}
		fi.setDirectory(file.isDirectory());
		fi.setExecute(file.canExecute());
		fi.setWrite(file.canWrite());
		fi.setRead(file.canRead());
		fi.setHidden(file.isHidden());
		return fi;
	}
}