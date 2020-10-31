package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.cache.EsCustomCO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface CustomService {

    /**
     * 查询所有的分类，父子关系
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/21 15:57:44
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCO>
     */
    DubboPageResult<EsCustomCO> getCategoryList(Long shopId);

}
