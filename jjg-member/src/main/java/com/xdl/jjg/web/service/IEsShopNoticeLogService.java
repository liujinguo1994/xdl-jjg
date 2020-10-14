package com.xdl.jjg.web.service;


import com.jjg.member.model.dto.EsShopNoticeLogDTO;
import com.xdl.jjg.model.domain.EsShopNoticeLogDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺站内消息(平台-店铺) 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-25
 */
public interface IEsShopNoticeLogService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 16:39:30
     * @param shopNoticeLogDTO    店铺站内消息(平台-店铺)DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    DubboResult insertShopNoticeLog(EsShopNoticeLogDTO shopNoticeLogDTO);


    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 15:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    DubboResult<EsShopNoticeLogDO> getShopNoticeLog(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 13:42:53
     * @param shopNoticeLogDTO  店铺站内消息(平台-店铺)DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopNoticeLogDO>
     */
    DubboPageResult<EsShopNoticeLogDO> getShopNoticeLogList(EsShopNoticeLogDTO shopNoticeLogDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 16:40:44
     * @param ids    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsShopNoticeLogDO>
     */
    DubboResult<EsShopNoticeLogDO> deleteShopNoticeLog(long[] ids);


    /**
     * 设置已读
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/25 16:16:44
     * @param ids    主键ids
     */
    DubboResult  read(Long[] ids);
}
