package com.xdl.jjg.web.service;

import com.jjg.shop.model.domain.EsParametersDO;
import com.jjg.shop.model.dto.EsParametersDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  参数服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsParametersService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parametersDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    DubboResult<EsParametersDO> insertParameters(EsParametersDTO parametersDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parametersDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    DubboResult<EsParametersDO> updateParameters(EsParametersDTO parametersDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    DubboResult<EsParametersDO> getParameters(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parametersDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsParametersDO>
     */
    DubboPageResult<EsParametersDO> getParametersList(EsParametersDTO parametersDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    DubboResult<EsParametersDO> deleteParameters(Long id);

    /**
     * 根据参数组ID 删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param groupId    参数组ID
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    DubboResult<EsParametersDO> deleteByGroupIdParameters(Long groupId);

    /**
     *
     * @param id 参数主键ID
     * @param sortType 移动类型 up 上移 down 下移
     * @return
     */
    DubboResult<EsParametersDO> sortParameters(Long id, String sortType);

    /**
     * 根据商品参数DTO 获取商品参数信息
     * @param esParametersDTO
     * @return
     */
    DubboPageResult<EsParametersDO> getParametersList(EsParametersDTO esParametersDTO);
}
