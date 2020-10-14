package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCompanyDeliveryDO;
import com.jjg.member.model.dto.EsCompanyDeliveryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 签约公司门店自提表 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsCompanyDeliveryService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param companyDeliveryDTO    签约公司门店自提表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    DubboResult insertCompanyDelivery(EsCompanyDeliveryDTO companyDeliveryDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param companyDeliveryDTO    签约公司门店自提表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    DubboResult updateCompanyDelivery(EsCompanyDeliveryDTO companyDeliveryDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    DubboResult<EsCompanyDeliveryDO> getCompanyDelivery(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param companyDeliveryDTO  签约公司门店自提表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCompanyDeliveryDO>
     */
    DubboPageResult<EsCompanyDeliveryDO> getCompanyDeliveryList(EsCompanyDeliveryDTO companyDeliveryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    DubboResult deleteCompanyDelivery(Long id);
}
