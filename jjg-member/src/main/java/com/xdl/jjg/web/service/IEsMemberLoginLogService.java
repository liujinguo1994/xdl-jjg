package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberLoginLogDO;
import com.xdl.jjg.model.dto.EsMemberLoginLogDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 会员登录日志 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-16
 */
public interface IEsMemberLoginLogService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 10:39:30
     * @param memberLoginLogDTO    会员登录日志DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    DubboResult insertMemberLoginLog(EsMemberLoginLogDTO memberLoginLogDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 11:00:10
     * @param memberLoginLogDTO    会员登录日志DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    DubboResult updateMemberLoginLog(EsMemberLoginLogDTO memberLoginLogDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 10:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    DubboResult<EsMemberLoginLogDO> getMemberLoginLog(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 09:42:53
     * @param memberLoginLogDTO  会员登录日志DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberLoginLogDO>
     */
    DubboPageResult<EsMemberLoginLogDO> getMemberLoginLogList(EsMemberLoginLogDTO memberLoginLogDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/16 11:10:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLoginLogDO>
     */
    DubboResult deleteMemberLoginLog(Long id);
}
