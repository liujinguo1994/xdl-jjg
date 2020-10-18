package com.xdl.jjg.web.service;


import com.jjg.trade.model.dto.EsSelfDateDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 自提日期 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsSelfDateService {

    /**
     *  系统后台
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/02
     * @param selfDateDTO    自提日期DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult insertSelfDate(EsSelfDateDTO selfDateDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param selfDateDTO    自提日期DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult updateSelfDate(EsSelfDateDTO selfDateDTO);

    /**
     * 系统后台
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult getSelfDate(Long id);

    /**
     *  系统后台
     * 0702 获取当前状态为有效的时间点
     * @return
     */
    DubboResult getAllSelfDate();

    /**
     *   系统后台
     * 查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/02
     * @param selfDateDTO  自提日期DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSelfDateDO>
     */
    DubboPageResult getSelfDateList(EsSelfDateDTO selfDateDTO, int pageSize, int pageNum);

    /**
     *  系统后台
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult deleteSelfDate(Long id);

    /**
     * 卖家结算页面 通过自提点ID 获取自提日期列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/1015 16:40:44
     * @param deliveryId    自提点id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboPageResult getSelfDateListByDeliveryId(Long deliveryId);
}
