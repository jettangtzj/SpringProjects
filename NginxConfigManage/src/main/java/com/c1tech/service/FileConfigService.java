/**
 * 
 */
package com.c1tech.service;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author jettang
 *
 */
@Service
public class FileConfigService {
	
	/**
	 * 配置文件的文件夹
	 */
	@Value("${config.dir}")
	String configDir;
	
	/**
	 * #文件中指定的行含有的字符
	 */
	@Value("${config.line.server}")
	String configLineServer;
	
	/**
	 * #文件中指定的行含有的字符
	 */
	@Value("${config.line.words}")
	String configLineWords;
	
	
	/**
	 * 列出配置文件列表
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "errorHandle_listConfigFiles")
	public String listConfigFiles() {
		File dirFile = new File(configDir);
		if(dirFile.isDirectory()) {
			File[] array = dirFile.listFiles(); 
			String fileStr = "";
			for(File file : array) {
				fileStr += "<br/>" + file.getName();
			}
			return fileStr;
		}
		return "";
		
	}
	
	/**
	 * 错误处理方法
	 * @param name
	 * @return
	 */
	public String errorHandle_listConfigFiles(String name) {
		return "Hystrix: Sorry, method listConfigFiles has an error" 
	+ "<br/>time:" + new Date();
	}
	
	/**
	 * 查看具体文件的域名配置
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	@HystrixCommand(fallbackMethod = "errorHandle_listDomain")
	public String listDomain(String filename) throws Exception {
		File file=new File(configDir + "/" + filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String serverLine="";
		String DomainLine="";
		String line = "";
		String returnStr = "";
		while((line=br.readLine())!=null){
			if(line.contains(configLineServer)) {//server_name行
				returnStr += line + "&nbsp;&nbsp;&nbsp;";
            	continue;
            }
            if(line.contains(configLineWords)) {//rewrite行
            	returnStr += line + "<br/>";
            	break;
            }
        }
		//关闭输入流
        br.close();
		return returnStr;
	}
	
	public String errorHandle_listDomain(String name) {
		return "Hystrix: Sorry, method listDomain has an error" 
	+ "<br/>time:" + new Date();
	}
	
	/**
	 * 设置新的域名
	 * @param filename
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	@HystrixCommand(fallbackMethod = "errorHandle_changeDomain")
	public String changeDomain(String filename, String domain) throws Exception {
		File file=new File(configDir + "/" + filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		CharArrayWriter caw=new CharArrayWriter();
		String line="";
		while((line=br.readLine())!=null){
            if(line.contains(configLineWords)) {
            	line = "rewrite  /  " + domain + "   permanent;";
            }
            //将该行写入内存
            caw.write(line);
            //添加换行符，并进入下次循环
            caw.append(System.getProperty("line.separator"));
        }
		//关闭输入流
        br.close();
        //将内存中的流写入源文件
        FileWriter fw=new FileWriter(file);
        caw.writeTo(fw);
        fw.close();
        //重启nginx
        Runtime.getRuntime().exec("service nginx restart");
        //
		return "ok";
	}
	
	public String errorHandle_changeDomain(String name) {
		return "Hystrix: Sorry, method changeDomain has an error" 
	+ "<br/>time:" + new Date();
	}
	
	/**
	 * 修改配置文件中具体的整行内容
	 * @param filename
	 * @param linewords
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@HystrixCommand(fallbackMethod = "errorHandle_changeLineContent")
	public String changeLineContent(String filename, String linewords, String content) throws Exception {
		File file=new File(configDir + "/" + filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		CharArrayWriter caw=new CharArrayWriter();
		String line="";
		while((line=br.readLine())!=null){
            if(line.contains(linewords)) {
            	line = content;
            }
            //将该行写入内存
            caw.write(line);
            //添加换行符，并进入下次循环
            caw.append(System.getProperty("line.separator"));
        }
		//关闭输入流
        br.close();
        //将内存中的流写入源文件
        FileWriter fw=new FileWriter(file);
        caw.writeTo(fw);
        fw.close();
        //重启nginx
        Runtime.getRuntime().exec("service nginx restart");
        //
		return "ok";
	}
	
	public String errorHandle_changeLineContent(String name) {
		return "Hystrix: Sorry, method changeLineContent has an error" 
	+ "<br/>time:" + new Date();
	}
	
}
