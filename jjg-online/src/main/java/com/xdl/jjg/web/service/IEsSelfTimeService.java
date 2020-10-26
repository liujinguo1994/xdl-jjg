package com.xdl.jjg.web.service;


import com.jjg.trade.model.dto.EsSelfTimeDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 自提时间 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsSelfTimeService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param selfTimeDTO    自提时间DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    DubboResult insertSelfTime(EsSelfTimeDTO selfTimeDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param selfTimeDTO    自提时间DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    DubboResult updateSelfTime(EsSelfTimeDTO selfTimeDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    DubboResult getSelfTime(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param selfTimeDTO  自提时间DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSelfTimeDO>
     */
    DubboPageResult getSelfTimeList(EsSelfTimeDTO selfTimeDTO, int pageSize, int pageNum);

    /**
     *  系统后台
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfTimeDO>
     */
    DubboResult deleteSelfTime(Long id);

    /**
     * 系统后台 和 买家端
     * 通过dateID获取时间段列表
     * @param dateId
     * @return
     */
    DubboPageResult getSelfTimeListByDateid(Long dateId);

}
