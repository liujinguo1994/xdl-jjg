package com.xdl.jjg.web.service;


import com.jjg.trade.model.dto.EsRefundLogDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsRefundLogService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param refundLogDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    DubboResult insertRefundLog(EsRefundLogDTO refundLogDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param refundLogDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    DubboResult updateRefundLog(EsRefundLogDTO refundLogDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    DubboResult getRefundLog(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param refundLogDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundLogDO>
     */
    DubboPageResult getRefundLogList(EsRefundLogDTO refundLogDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRefundLogDO>
     */
    DubboResult deleteRefundLog(Long id);
}
