package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodsRankingDO;
import com.xdl.jjg.model.dto.EsGoodsRankingDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 热门榜单 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
public interface IEsGoodsRankingService {

    /**
     * 插入数据
     *
     * @param goodsRankingDTO 热门榜单DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     * @since 2020-05-07
     */
    DubboResult insertGoodsRanking(EsGoodsRankingDTO goodsRankingDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param goodsRankingDTO 热门榜单DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     * @since 2020-05-07
     */
    DubboResult updateGoodsRanking(EsGoodsRankingDTO goodsRankingDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     * @since 2020-05-07
     */
    DubboResult<EsGoodsRankingDO> getGoodsRanking(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param goodsRankingDTO 热门榜单DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsRankingDO>
     * @since 2020-05-07
     */
    DubboPageResult<EsGoodsRankingDO> getGoodsRankingList(EsGoodsRankingDTO goodsRankingDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsRankingDO>
     * @since 2020-05-07
     */
    DubboResult deleteGoodsRanking(Long id);

    /**
     * app端分页查询更多榜单列表
     */
    DubboPageResult<EsGoodsRankingDO> selectGoodsRankingList(int pageSize, int pageNum);
}
