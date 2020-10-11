package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsSysLogDO;
import com.xdl.jjg.model.dto.EsSysLogDTO;
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
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param sysLogDTO    系统操作日志DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    DubboResult insertSysLog(EsSysLogDTO sysLogDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param sysLogDTO    系统操作日志DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    DubboResult updateSysLog(EsSysLogDTO sysLogDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    DubboResult<EsSysLogDO> getSysLog(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param sysLogDTO  系统操作日志DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsSysLogDO>
     */
    DubboPageResult<EsSysLogDO> getSysLogList(EsSysLogDTO sysLogDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSysLogDO>
     */
    DubboResult deleteSysLog(Long id);
}
