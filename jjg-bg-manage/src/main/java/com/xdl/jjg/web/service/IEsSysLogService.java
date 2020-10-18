package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsSysLogDO;
import com.jjg.system.model.dto.EsSysLogDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 系统操作日志 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsSysLogService {

    /**
     * 插入数据
     *
     * @param sysLogDTO 系统操作日志DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     * @since 2019-06-04
     */
    DubboResult insertSysLog(EsSysLogDTO sysLogDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param sysLogDTO 系统操作日志DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     * @since 2019-06-04
     */
    DubboResult updateSysLog(EsSysLogDTO sysLogDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     * @since 2019-06-04
     */
    DubboResult<EsSysLogDO> getSysLog(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param sysLogDTO 系统操作日志DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsSysLogDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsSysLogDO> getSysLogList(EsSysLogDTO sysLogDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     * @since 2019-06-04
     */
    DubboResult deleteSysLog(Long id);
}
