package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsBrandDO;
import com.jjg.shop.model.domain.EsBrandSelectDO;
import com.jjg.shop.model.domain.EsCategoryBrandDO;
import com.xdl.jjg.response.service.DubboPageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsCategoryBrandService {


    /**
     * 保存分类绑定的品牌
     * @param categoryId 分类ID
     * @param brandId 品牌ID
     * @return
     */
    DubboPageResult<EsCategoryBrandDO> saveCategoryBrand(Long categoryId, Integer[] brandId);

    /**
     * 根据分类ID 获取品牌信息
     * @return
     */
    DubboPageResult<EsBrandSelectDO> getCategoryList(Long cateId);

    /**
     * 根据分类ID 获取品牌信息
     * @return
     */
    DubboPageResult<EsBrandDO> getBrandsByCategoryList(Long cateId);
}
