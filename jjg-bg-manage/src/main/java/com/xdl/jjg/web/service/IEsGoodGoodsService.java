package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodGoodsDO;
import com.xdl.jjg.model.dto.EsGoodGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 品质好货 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
public interface IEsGoodGoodsService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2019-08-06
     * @param goodGoodsDTO    品质好货DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodGoodsDO>
     */
    DubboResult insertGoodGoods(EsGoodGoodsDTO goodGoodsDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-08-06
     * @param goodGoodsDTO    品质好货DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodGoodsDO>
     */
    DubboResult updateGoodGoods(EsGoodGoodsDTO goodGoodsDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-08-06
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodGoodsDO>
     */
    DubboResult<EsGoodGoodsDO> getGoodGoods(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-08-06
     * @param goodGoodsDTO  品质好货DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodGoodsDO>
     */
    DubboPageResult<EsGoodGoodsDO> getGoodGoodsList(EsGoodGoodsDTO goodGoodsDTO, int pageSize, int pageNum);

    /**
     * 删除或批量删除
     */
    DubboResult batchDel(Integer[] ids);

    /**
     * 发布或批量发布
     */
    DubboResult batchRelease(Integer[] ids);

    /**
     * 下架
     */
    DubboResult underGoodGoods(EsGoodGoodsDTO goodGoodsDTO);

    /**
     * 分页查询发布中的品质好货
     */
    DubboPageResult<EsGoodGoodsDO> getList(int pageSize, int pageNum);
}
