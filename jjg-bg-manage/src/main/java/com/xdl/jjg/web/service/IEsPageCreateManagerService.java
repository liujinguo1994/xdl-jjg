package com.xdl.jjg.web.service;

import com.xdl.jjg.response.service.DubboResult;

/**
 * 
 * 静态页生成接口
 */
public interface IEsPageCreateManagerService {
	/**
	 * 生成静态页
	 */
	DubboResult<Boolean> create(String[] choosePages);
}
