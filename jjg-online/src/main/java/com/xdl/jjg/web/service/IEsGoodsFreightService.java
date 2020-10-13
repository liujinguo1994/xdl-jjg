package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsGoodsFreightDO;
import com.shopx.trade.api.model.domain.EsShipTemplateDO;
import com.shopx.trade.api.model.domain.dto.EsGoodsFreightDTO;

/**
 * <p>
 * 分类运费模板关联表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsGoodsFreightService {

    /**
     * 插入数据
     *
     * @param goodsFreightDTO 分类运费模板关联表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    DubboResult insertGoodsFreight(EsGoodsFreightDTO goodsFreightDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param goodsFreightDTO 分类运费模板关联表DTO
     * @param id              主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    DubboResult updateGoodsFreight(EsGoodsFreightDTO goodsFreightDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    DubboResult<EsGoodsFreightDO> getGoodsFreight(Long id);

    /**
     * 商品模块
     * 根据categoryId ,shopId获取数据
     * @param categoryId shopId
     * @auther: LiuJG
     */
    DubboResult<EsShipTemplateDO> getGoodsFreightByCategoryId(Long categoryId, Long shopId);

    /**
     * 根据查询条件查询列表
     *
     * @param goodsFreightDTO 分类运费模板关联表DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsFreightDO>
     */
    DubboPageResult<EsGoodsFreightDO> getGoodsFreightList(EsGoodsFreightDTO goodsFreightDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    DubboResult deleteGoodsFreight(Long id);
    /**
     * 系统模块
     * 分类绑定运费模板的接口
     *@auther: LiuJG 344009799@qq.com
     * @date: 2019/06/22 17:00
     */
    DubboResult saveCatShipTemplate(Long id, Long shipTemplateId);

}
