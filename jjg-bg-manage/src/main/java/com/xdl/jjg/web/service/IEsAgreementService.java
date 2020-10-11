package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsAgreementDO;
import com.xdl.jjg.model.dto.EsAgreementDTO;
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
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param agreementDTO    协议维护DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    DubboResult insertAgreement(EsAgreementDTO agreementDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param agreementDTO    协议维护DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    DubboResult updateAgreement(EsAgreementDTO agreementDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    DubboResult<EsAgreementDO> getAgreement(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param agreementDTO  协议维护DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsAgreementDO>
     */
    DubboPageResult<EsAgreementDO> getAgreementList(EsAgreementDTO agreementDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAgreementDO>
     */
    DubboResult deleteAgreement(Long id);

    //获取注册协议
    DubboResult<EsAgreementDO> getEsAgreement();
}
