package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsParameterGroupDO;
import com.jjg.shop.model.domain.ParameterGroupDO;
import com.jjg.shop.model.dto.EsParameterGroupDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  参数组服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsParameterGroupService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parameterGroupDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    DubboResult<EsParameterGroupDO> insertParameterGroup(EsParameterGroupDTO parameterGroupDTO);

    /**
     *  修改数据
     * @param esParameterGroupDTO
     * @param id
     * @return
     */
    DubboResult<EsParameterGroupDO> updateParameterGroup(EsParameterGroupDTO esParameterGroupDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    DubboResult<EsParameterGroupDO> getParameterGroup(Long id);

    /**
     *
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parameterGroupDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsParameterGroupDO>
     */
    DubboPageResult<EsParameterGroupDO> getParameterGroupList(EsParameterGroupDTO parameterGroupDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    DubboResult<EsParameterGroupDO> deleteParameterGroup(Long id);

    /**
     *
     * @param groupId 参数组ID
     * @param sortType 移动类型
     * @return
     */
    DubboResult<EsParameterGroupDO> sortParameterGroup(Long groupId, String sortType);

    /**
     * 根据商品分类ID 获取参数组 参数
     * @param categoryId 商品分类ID
     * @return
     */
    DubboPageResult<ParameterGroupDO> getParameterGroupList(Long categoryId);

    /**
     * 根据商品分类ID 获取参数组信息
     * @param categoryId 商品分类ID
     * @return
     */
    DubboPageResult<EsParameterGroupDO> getBuyerParameterGroup(Long categoryId);
}
