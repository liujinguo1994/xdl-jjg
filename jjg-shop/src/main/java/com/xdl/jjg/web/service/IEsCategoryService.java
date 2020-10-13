package com.xdl.jjg.web.service;

import com.xdl.jjg.model.co.EsCategoryCO;
import com.xdl.jjg.model.domain.EsBuyerCategoryDO;
import com.xdl.jjg.model.domain.EsCategoryDO;
import com.xdl.jjg.model.domain.EsCategorySpecDO;
import com.xdl.jjg.model.domain.ParameterGroupDO;
import com.xdl.jjg.model.dto.EsCategoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsCategoryService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param categoryDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    DubboResult<EsCategoryDO> insertCategory(EsCategoryDTO categoryDTO);


    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param categoryDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    DubboResult<EsCategoryDO> updateCategory(EsCategoryDTO categoryDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    DubboResult<EsCategoryDO> getCategory(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param categoryId  DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsCategoryDO>
     */
    DubboPageResult<EsCategoryDO> getCategoryList(Long categoryId);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCategoryDO>
     */
    DubboResult<EsCategoryDO> deleteCategory(Long id);

    /**
     * 根据主键ID获取 分类下面所有的子类
     * @param id 主键ID
     * @return
     */
    DubboPageResult<EsCategoryDO> getCategoryParentList(Long id);


    /**
     * 查询店铺经营分类及其子分类
     * @param cateId 主键ID
     * @return
     */
    DubboPageResult<EsCategoryCO> getShopCategory(String cateId);

    /**
     * 查询所有分类 父子关系
     * @param id
     * @return
     */
    DubboPageResult<EsCategoryDO> getCategoryChildren(Long id);

    /**
     * 查询所有分类 树形结构
     * @return
     */
    DubboPageResult<EsCategoryCO> getCategoryList();


    /**
     * 保存分类绑定的规格
     * @param categoryId 分类ID
     * @param specId 规格ID
     * @return
     */
    DubboResult<EsCategorySpecDO> saveCategorySpec(Long categoryId, Long[] specId);

    /**
     * 首页分类
     * @param id
     * @return
     */
    DubboPageResult<EsBuyerCategoryDO> getBuyCategoryChildren(Long id);

    /**
     * 所有的一级分类
     * @return
     */
    DubboPageResult<EsCategoryDO> getFirstBrandList();

    /**
     * 所有的一级分类
     * @return
     */
    DubboResult<EsCategoryDO> getFirstByName(String name);

    /**
     * 根据分类ID集合获取分类信息
     * @param cateIds
     * @return
     */
    DubboPageResult<EsCategoryDO> getCategoryByIds(List<Long> cateIds);

    /**
     * 获取商品参数
     * @param goodsId 商品ID
     * @param category 分类ID
     * @return
     */
    DubboPageResult<ParameterGroupDO> queryParams(Long goodsId, Long category);

    /**
     * 获取商品参数
     * @param goodsId 商品ID
     * @param category 分类ID
     * @return
     */
    DubboPageResult<ParameterGroupDO> queryDraftParams(Long goodsId, Long category);
}
