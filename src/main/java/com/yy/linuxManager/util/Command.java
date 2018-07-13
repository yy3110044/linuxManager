package com.yy.linuxManager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 此类用执行一个服务器命令
 * @author yy
 *
 */
public class Command {
	private static final Logger logger = LogManager.getLogger(Command.class);

	private Process process;
	private Command(String command) {
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public static Command execute(String command) {
		return new Command(command);
	}
	
	public List<String> getStrListResult() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			List<String> list = new ArrayList<String>();
			
			String str = null;
			while((str = br.readLine()) != null) {
				list.add(str);
			}
			return list;
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public String getStrResult() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			StringBuilder sb = new StringBuilder();
			
			String str = null;
			while((str = br.readLine()) != null) {
				sb.append(str).append(System.lineSeparator());
			}
			return sb.toString();
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public String getStrErrorResult() {		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
			StringBuilder sb = new StringBuilder();
			
			String str = null;
			while((str = br.readLine()) != null) {
				sb.append(str).append(System.lineSeparator());
			}
			return sb.toString();
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public void write(String str) {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
			bw.write(str);
			bw.flush();
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
}
