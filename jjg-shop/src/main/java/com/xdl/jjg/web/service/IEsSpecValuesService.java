package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsSpecValuesDO;
import com.xdl.jjg.model.dto.EsSpecValuesDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  规格值服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsSpecValuesService {

    /**
     *新增某个规格的规格值
     * @param specId
     * @param specValues
     * @return
     */
    DubboResult<EsSpecValuesDO> insertSpecValues(Long specId, String[] specValues);

    /**
     *  新增规格值
     * @param esSpecValuesDTO
     * @return
     */
    DubboResult<EsSpecValuesDO> insertSpecValues(EsSpecValuesDTO esSpecValuesDTO);
    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param specValuesDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSpecValuesDO>
     */
    DubboResult<EsSpecValuesDO> updateSpecValues(EsSpecValuesDTO specValuesDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSpecValuesDO>
     */
    DubboResult<EsSpecValuesDO> getSpecValues(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param specValuesDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSpecValuesDO>
     */
    DubboPageResult<EsSpecValuesDO> getSpecValuesList(EsSpecValuesDTO specValuesDTO, int pageSize, int pageNum);

    /**
     * 获取某个规格的规格值列表
     * @param specId 规格ID
     * @return
     */
    DubboPageResult<EsSpecValuesDO> getSpecValuesList(Long specId);
    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSpecValuesDO>
     */
    DubboResult<EsSpecValuesDO> deleteSpecValues(Long id);
}
