package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsDiscountDO;
import com.xdl.jjg.model.dto.EsDiscountDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 公司折扣表 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-28
 */
public interface IEsDiscountService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 15:39:30
     * @param discountDTO    公司折扣表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult insertDiscount(EsDiscountDTO discountDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:10
     * @param discountDTO    公司折扣表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult updateDiscount(EsDiscountDTO discountDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult<EsDiscountDO> getDiscount(Long id);

    /**
     * 通过CompanyID 获取 分类ID list 获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboPageResult<EsDiscountDO> getDiscountByCompanyId(Long id);

    DubboResult<EsDiscountDO> getDiscountByCompanyCodeAndCategoryId(String companyCode, Long categoryId);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 13:42:53
     * @param discountDTO  公司折扣表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsDiscountDO>
     */
    DubboPageResult<EsDiscountDO> getDiscountList(EsDiscountDTO discountDTO, int pageSize, int pageNum);

    /**
     * 批量删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @param ids    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult deleteDiscount(Integer[] ids);

    /**
     * 根据分类id查询
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult<EsDiscountDO> getByCategoryId(Long id);

    /**
     * 根据分类id批量修改名称
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @param categoryId    分类id
     * @param categoryName    分类名称
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult updateByCategoryId(Long categoryId, String categoryName);
}
