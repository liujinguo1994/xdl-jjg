package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsBillDetailDO;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 结算单 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsBillDetailService {

    /**
     * 插入数据
     *
     * @param billDetailDTO 结算单DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    DubboResult insertBillDetail(EsBillDetailDTO billDetailDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param billDetailDTO 结算单DTO
     * @param id            主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    DubboResult updateBillDetail(EsBillDetailDTO billDetailDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    DubboResult<EsBillDetailDO> getBillDetail(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param billDetailDTO 结算单DTO
     * @param pageSize      行数
     * @param pageNum       页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsBillDetailDO>
     */
    DubboPageResult<EsBillDetailDO> getBillDetailList(EsBillDetailDTO billDetailDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsBillDetailDO>
     */
    DubboResult deleteBillDetail(Long id);
}
