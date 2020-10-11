package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsSmtpDO;
import com.xdl.jjg.model.dto.EsSmtpDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsSmtpService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param smtpDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    DubboResult insertSmtp(EsSmtpDTO smtpDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param smtpDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    DubboResult updateSmtp(EsSmtpDTO smtpDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    DubboResult<EsSmtpDO> getSmtp(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param smtpDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsSmtpDO>
     */
    DubboPageResult<EsSmtpDO> getSmtpList(EsSmtpDTO smtpDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSmtpDO>
     */
    DubboResult deleteSmtp(Long id);


    /**
     * 获取当前使用的smtp方案
     */
    DubboResult<EsSmtpDO> getCurrentSmtp();
}
