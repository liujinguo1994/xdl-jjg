package com.xdl.jjg;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;


public class ZuulTest extends ZuulFilter {

	@Override
	public Object run() {
		System.out.println("--------------------------------");
		RequestContext ctx=RequestContext.getCurrentContext();
		ctx.setSendZuulResponse(true);
	
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
