package com.yy.linuxManager.util;

public class FileInfo implements Comparable<FileInfo> {
	private String fileName;
	private String filePath;
	private boolean read;
	private boolean write;
	private boolean execute;
	private boolean hidden;
	private boolean directory;
	public String getFileName() {
		return fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public boolean isRead() {
		return read;
	}
	public boolean isWrite() {
		return write;
	}
	public boolean isExecute() {
		return execute;
	}
	public boolean isHidden() {
		return hidden;
	}
	public boolean isDirectory() {
		return directory;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public void setWrite(boolean write) {
		this.write = write;
	}
	public void setExecute(boolean execute) {
		this.execute = execute;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	@Override
	public int compareTo(FileInfo o) {
		if(this.isDirectory()) { //当前对象是个目录
			if(o.isDirectory()) { //比较对象是个目录
				return this.fileName.compareTo(o.fileName);
			} else { //比较对象是个文件
				return -1;
			}
		} else { //当前对象是个文件
			if(o.isDirectory()) { //比较对象是个目录
				return 1;
			} else { //比较对象是个文件
				return this.fileName.compareTo(o.fileName);
			}
		}
	}
}