package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsFocusPictureDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface FocusPictureService {


    //根据客户端类型查询轮播图列表
    DubboPageResult<EsFocusPictureDO> getList(String clientType);
}
