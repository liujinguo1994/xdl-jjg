package com.xdl.jjg.constant;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *  用ThreadLocal来存储Session,以便实现Session any where 
 * @author wangaf
 * @version 1.1
 * 新增request any where
 */
public class ThreadContextHolder  {

	private static ThreadLocal<HttpServletRequest> requestThreadLocalHolder = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responseThreadLocalHolder = new ThreadLocal<HttpServletResponse>();


	public static void setHttpRequest(HttpServletRequest request){
		
		requestThreadLocalHolder.set(request);
	}

	public static void setHttpResponse(HttpServletResponse response){
		responseThreadLocalHolder.set(response);
	}

	
	public static void remove(){
		requestThreadLocalHolder.remove();
		responseThreadLocalHolder.remove();
	}
	
	

	public static HttpServletResponse getHttpResponse(){
		
		return responseThreadLocalHolder.get();
	}
	public static HttpServletRequest getHttpRequest(){
		return  requestThreadLocalHolder.get();
	}


	
}
