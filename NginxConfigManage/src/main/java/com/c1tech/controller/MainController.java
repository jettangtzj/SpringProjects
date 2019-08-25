/**
 * 
 */
package com.c1tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.c1tech.service.FileConfigService;

/**
 * @author jettang
 *
 */
@RestController
public class MainController {
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private FileConfigService fileConfigService;
	
	/**
	 * 列出配置文件列表
	 * @return
	 */
	@RequestMapping(value="/listConfigFiles")
	public String listConfigFiles() {
		return fileConfigService.listConfigFiles();
	}
	
	/**
	 * 列出文件的配置信息
	 * @param filename
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/listDomain")
	public String listDomain(String filename) throws Exception {
		return fileConfigService.listDomain(filename);
	}

	/**
	 * 修改文件的配置信息
	 * @param filename 文件名
	 * @param domain  域名
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changeDomain")
	public String changeDomain(String filename, String domain) throws Exception {
		return fileConfigService.changeDomain(filename, domain);
	}
	
	/**
	 * 修改文件的整行内容
	 * @param filename 文件名
	 * @param linewords	  行关键词
	 * @param content  修改内容
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changeLineContent")
	public String changeLineContent(String filename, String linewords, String content) throws Exception {
		return fileConfigService.changeLineContent(filename, linewords, content);
	}

	/**
	 * 修改参数值
	 * @param filename 配置文件名
	 * @param param   参数名
	 * @param value   参数值
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changeParamValue")
	public String changeParamValue(String filename, String param, String value) throws Exception {
		return fileConfigService.changeParamValue(filename, param, value);
	}

}
