package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsAgreementDO;
import com.jjg.system.model.dto.EsAgreementDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 协议维护 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsAgreementService {

    /**
     * 插入数据
     *
     * @param agreementDTO 协议维护DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     * @since 2019-06-04
     */
    DubboResult insertAgreement(EsAgreementDTO agreementDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param agreementDTO 协议维护DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     * @since 2019-06-04
     */
    DubboResult updateAgreement(EsAgreementDTO agreementDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     * @since 2019-06-04
     */
    DubboResult<EsAgreementDO> getAgreement(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param agreementDTO 协议维护DTO
     * @param pageSize     行数
     * @param pageNum      页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsAgreementDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsAgreementDO> getAgreementList(EsAgreementDTO agreementDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     * @since 2019-06-04
     */
    DubboResult deleteAgreement(Long id);

    //获取注册协议
    DubboResult<EsAgreementDO> getEsAgreement();
}
