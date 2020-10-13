package com.xdl.jjg.web.service;


import com.xdl.jjg.model.cache.EsCustomCO;
import com.xdl.jjg.model.domain.EsCustomDO;
import com.xdl.jjg.model.dto.EsCustomDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺自定义分类 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsCustomService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param customDTO    店铺自定义分类DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    DubboResult insertCustom(EsCustomDTO customDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param customDTO    店铺自定义分类DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    DubboResult updateCustom(EsCustomDTO customDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    DubboResult<EsCustomDO> getCustom(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param customDTO  店铺自定义分类DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCustomDO>
     */
    DubboPageResult<EsCustomDO> getCustomList(EsCustomDTO customDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    DubboResult deleteCustom(Long id);

    /**
     * 查询所有的分类，父子关系
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/21 15:57:44
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCO>
     */
    DubboPageResult<EsCustomCO> getCategoryList(Long shopId);

    /**
     * 根据父ID获取分类下面的子类
     * @auther: yuanj 595831328@qq.com
     * @date: 2019/6/21 15:57:44
     * @return: com.shopx.common.model.result.DubboResult<EsCustomDO>
     */
    DubboPageResult<EsCustomDO> getCategoryParentList(Long id);
}
