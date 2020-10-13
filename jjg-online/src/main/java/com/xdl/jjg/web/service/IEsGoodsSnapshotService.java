package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsGoodsSnapshotDO;
import com.shopx.trade.api.model.domain.dto.EsGoodsSnapshotDTO;

/**
 * <p>
 * 商品快照 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsGoodsSnapshotService {

    /**
     * 插入数据
     *
     * @param goodsSnapshotDTO 商品快照DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    DubboResult insertGoodsSnapshot(EsGoodsSnapshotDTO goodsSnapshotDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param goodsSnapshotDTO 商品快照DTO
     * @param id               主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    DubboResult updateGoodsSnapshot(EsGoodsSnapshotDTO goodsSnapshotDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    DubboResult<EsGoodsSnapshotDO> getGoodsSnapshot(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param goodsSnapshotDTO 商品快照DTO
     * @param pageSize         行数
     * @param pageNum          页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsSnapshotDO>
     */
    DubboPageResult<EsGoodsSnapshotDO> getGoodsSnapshotList(EsGoodsSnapshotDTO goodsSnapshotDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSnapshotDO>
     */
    DubboResult deleteGoodsSnapshot(Long id);
}
