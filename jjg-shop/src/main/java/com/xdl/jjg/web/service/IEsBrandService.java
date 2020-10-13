package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsBrandDO;
import com.xdl.jjg.model.dto.EsBrandDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface IEsBrandService {

    /**
     * 新增品牌
     * @param brandDTO
     * @return
     */
    DubboResult<EsBrandDO> insertBrand(EsBrandDTO brandDTO);

    /**
     * 修改品牌
     * @param brandDTO
     * @return
     */
    DubboResult<EsBrandDO> updateBrand(EsBrandDTO brandDTO, Long id);
    /**
     * 逻辑删除品牌
     * @param ids
     * @return
     */
    DubboResult<EsBrandDO> deleteBrand(Integer[] ids);

    /**
     * 根据品牌ID 获取品牌信息
     * @param id
     * @return
     */
    DubboResult<EsBrandDO> getBrand(Long id);
    /**
     * 分页查询品牌信息
     * @param brandDTO
     * @return
     */
    DubboPageResult<EsBrandDO> getBrandList(EsBrandDTO brandDTO, int pageSize, int pageNum);

    DubboPageResult<EsBrandDO> getBrandsByCategory(Long categoryId);

    DubboPageResult<EsBrandDO> getAllBrandList();

    /**
     * 根据品牌ID集合获取品牌信息
     * @param ids
     * @return
     */
    DubboPageResult<EsBrandDO> getBrandByIds(List<Long> ids);
}
