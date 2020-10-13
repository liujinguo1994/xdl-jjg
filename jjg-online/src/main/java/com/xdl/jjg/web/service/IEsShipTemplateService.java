package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsShipTemplateDO;
import com.shopx.trade.api.model.domain.dto.EsShipTemplateDTO;

/**
 * <p>
 * 运费模板表 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsShipTemplateService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shipTemplateDTO    运费模板表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult insertShipTemplate(EsShipTemplateDTO shipTemplateDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param shipTemplateDTO    运费模板表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult updateShipTemplate(EsShipTemplateDTO shipTemplateDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult getShipTemplate(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shipTemplateDTO  运费模板表DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsShipTemplateDO>
     */
    DubboPageResult getShipTemplateList(EsShipTemplateDTO shipTemplateDTO, int pageSize, int pageNum);

    /**
     * 卖家端
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44

     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult deleteSellerShipTemplate(Long id);

    /**
     * 系统模块
     * 获取所有运费模板信息列表（给我提供个运费模板的下拉框数据 阮明）
     *@auther: LiuJG 344009799@qq.com
     * @date: 2019/06/22 17:00
     */
    DubboPageResult<EsShipTemplateDO> getComboBox();

    /**
     * 卖家端
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shipTemplateDTO    运费模板表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult insertSellerShipTemplate(EsShipTemplateDTO shipTemplateDTO);

    /**
     * 卖家端
     * 修改运费模板
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shipTemplateDTO    运费模板表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult updateSellerShipTemplate(EsShipTemplateDTO shipTemplateDTO);

    /**
     * 根据模板id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param templateId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    DubboResult<EsShipTemplateDO> getShipTemplateByTemplateId(Long templateId);

    DubboResult deleteShipTemplate(Long id);
}
