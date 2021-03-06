package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsSettlementDO;
import com.jjg.trade.model.dto.EsSettlementDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 结算单 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-20 15:12:52
 */
public interface IEsSettlementService {

    /**
     * 插入数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @param settlementDTO    结算单DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    DubboResult insertSettlement(EsSettlementDTO settlementDTO);

    /**
     * 根据条件更新更新数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @param settlementDTO   结算单DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    DubboResult updateSettlement(EsSettlementDTO settlementDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    DubboResult<EsSettlementDO> getSettlement(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @param settlementDTO  结算单DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsSettlementDO>
     */
    DubboPageResult<EsSettlementDO> getSettlementList(EsSettlementDTO settlementDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSettlementDO>
     */
    DubboResult deleteSettlement(Long id);
}
