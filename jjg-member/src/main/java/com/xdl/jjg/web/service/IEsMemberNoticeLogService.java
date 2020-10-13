package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberNoticeLogDO;
import com.xdl.jjg.model.dto.EsMemberNoticeLogDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 会员站内消息 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberNoticeLogService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberNoticeLogDTO    会员站内消息DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    DubboResult insertMemberNoticeLog(EsMemberNoticeLogDTO memberNoticeLogDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberNoticeLogDTO    会员站内消息DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    DubboResult updateMemberNoticeLog(EsMemberNoticeLogDTO memberNoticeLogDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    DubboResult<EsMemberNoticeLogDO> getMemberNoticeLog(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberNoticeLogDTO  会员站内消息DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberNoticeLogDO>
     */
    DubboPageResult<EsMemberNoticeLogDO> getMemberNoticeLogList(EsMemberNoticeLogDTO memberNoticeLogDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberNoticeLogDO>
     */
    DubboResult deleteMemberNoticeLog(Long id);
}
