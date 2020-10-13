package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsCartConfigureDO;
import com.xdl.jjg.model.dto.EsCartConfigureDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 购物车配置 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-02
 */
public interface IEsCartConfigureService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:50:30
     * @param cartConfigureDTO    购物车配置DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    DubboResult insertCartConfigure(EsCartConfigureDTO cartConfigureDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:19:30
     * @param cartConfigureDTO    购物车配置DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    DubboResult updateCartConfigure(EsCartConfigureDTO cartConfigureDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:20:30
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    DubboResult<EsCartConfigureDO> getCartConfigure();

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:29:20
     * @param cartConfigureDTO  购物车配置DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCartConfigureDO>
     */
    DubboPageResult<EsCartConfigureDO> getCartConfigureList(EsCartConfigureDTO cartConfigureDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:12:30
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    DubboResult deleteCartConfigure(Long id);
}
