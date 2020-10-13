package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsSpecificationDO;
import com.xdl.jjg.model.dto.EsSpecificationDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  规格服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsSpecificationService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param specificationDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    DubboResult<EsSpecificationDO> insertSpecification(EsSpecificationDTO specificationDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param specificationDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    DubboResult<EsSpecificationDO> updateSpecification(EsSpecificationDTO specificationDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    DubboResult<EsSpecificationDO> getSpecification(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param specificationDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSpecificationDO>
     */
    DubboPageResult<EsSpecificationDO> getSpecificationList(EsSpecificationDTO specificationDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    DubboResult<EsSpecificationDO> deleteSpecification(Long[] id);
}
