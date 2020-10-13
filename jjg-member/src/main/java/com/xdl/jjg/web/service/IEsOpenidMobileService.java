package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsOpenidMobileDO;
import com.xdl.jjg.model.dto.EsOpenidMobileDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 微信关联手机号 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-09
 */
public interface IEsOpenidMobileService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-09
     * @param openidMobileDTO    微信关联手机号DTO
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    DubboResult insertOpenidMobile(EsOpenidMobileDTO openidMobileDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-09
     * @param openidMobileDTO    微信关联手机号DTO
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    DubboResult updateOpenidMobile(EsOpenidMobileDTO openidMobileDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-09
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    DubboResult<EsOpenidMobileDO> getOpenidMobile(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2020-05-09
     * @param openidMobileDTO  微信关联手机号DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsOpenidMobileDO>
     */
    DubboPageResult<EsOpenidMobileDO> getOpenidMobileList(EsOpenidMobileDTO openidMobileDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-09
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    DubboResult deleteOpenidMobile(Long id);

    /**
     * 查询该微信关联的手机号list
     */
    DubboPageResult<EsOpenidMobileDO> getListByOpenid(String openid);


}
