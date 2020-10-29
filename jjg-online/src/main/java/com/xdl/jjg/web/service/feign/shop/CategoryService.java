package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsCategoryDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

public interface CategoryService {

    /**
     * 所有的一级分类
     * @return
     */
    DubboResult<EsCategoryDO> getFirstByName(String name);

    /**
     * 查询所有分类 父子关系
     * @param id
     * @return
     */
    DubboPageResult<EsCategoryDO> getCategoryChildren(Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    DubboResult<EsCategoryDO> getCategory(Long id);
}
