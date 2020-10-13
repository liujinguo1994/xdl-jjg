package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsExpressPlatformDO;
import com.xdl.jjg.model.dto.EsExpressPlatformDTO;
import com.xdl.jjg.model.vo.EsExpressPlatformVO;
import com.xdl.jjg.model.vo.ExpressDetailVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsExpressPlatformService {

    /**
     * 插入数据
     *
     * @param expressPlatformDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     * @since 2019-06-04
     */
    DubboResult insertExpressPlatform(EsExpressPlatformDTO expressPlatformDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param expressPlatformDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     * @since 2019-06-04
     */
    DubboResult updateExpressPlatform(EsExpressPlatformDTO expressPlatformDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     * @since 2019-06-04
     */
    DubboResult<EsExpressPlatformDO> getExpressPlatform(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param pageSize 行数
     * @param pageNum  页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsExpressPlatformDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsExpressPlatformVO> getExpressPlatformList(int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     * @since 2019-06-04
     */
    DubboResult deleteExpressPlatform(Long id);

    /**
     * 根据主键删除数据
     *
     * @param bean
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     * @since 2019-06-04
     */
    DubboResult open(String bean);


    /**
     * 查询物流信息
     *
     * @param id 物流公司id
     * @param nu 物流单号
     * @return 物流详细
     */
    DubboResult<ExpressDetailVO> getExpressFormDetail(Long id, String nu);
}
