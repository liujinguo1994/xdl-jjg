package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsCategorySpecDO;
import com.xdl.jjg.model.dto.EsCategorySpecDTO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsCategorySpecService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param categorySpecDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCategorySpecDO>
     */
    DubboResult<EsCategorySpecDO> insertCategorySpec(EsCategorySpecDTO categorySpecDTO);

}
