package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsPageDO;
import com.jjg.system.model.dto.EsPageDTO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsPageService {


    /**
     * 根据条件更新更新数据
     *
     * @param pageDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsPageDO>
     * @since 2019-06-04
     */
    DubboResult updatePage(EsPageDTO pageDTO);


    //使用客户端类型和页面类型查询一个楼层
    DubboResult<EsPageDO> getByType(String clientType, String pageType);
}
