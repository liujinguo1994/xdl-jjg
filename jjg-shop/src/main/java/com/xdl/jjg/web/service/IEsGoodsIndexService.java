package com.xdl.jjg.web.service;


import com.xdl.jjg.model.co.EsPcSelectSearchCO;
import com.xdl.jjg.model.co.EsSelectSearchCO;
import com.xdl.jjg.model.domain.EsGoodsIndexDO;
import com.xdl.jjg.model.dto.EsGoodsIndexDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * 商品ES接口
 * @author wangaf 826988665@qq.com
 *
 */
public interface  IEsGoodsIndexService {

    /**
     * 新增商品索引
     * @param
     * @return
     */
    DubboResult insertEsGoodsIndex(List<EsGoodsIndexDTO> esGoodsIndexDTO);
    /**
     * 修改商品索引
     * @param esGoodsIndexDTO
     * @return
     */
    DubboResult<EsGoodsIndexDO> updateEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO);
    /**
     * 删除商品索引
     * @param esGoodsIndexDTO
     * @return
     */
    DubboResult<EsGoodsIndexDO> deleteEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO);
    /**
     * 全部删除商品索引
     * @return
     */
    DubboResult<EsGoodsIndexDO> deleteEsGoodsIndex();

    DubboResult<EsGoodsIndexDO> deleteEsGoodsIndex(String prod);
    /**
     * 查询商品索引
     * @param esGoodsIndexDTO

     * @return
     */
    DubboPageResult<EsGoodsIndexDO> getEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO, int pageSiz, int pageNum);

    /**
     * 批量生成商品索引
     * @param
     * @return
     */
    DubboResult batchInsertEsGoodsIndex(List<EsGoodsIndexDTO> esGoodsIndexDTO);

    /**
     * 搜索选择器
     * @param goodsSearch
     * @return
     */
    DubboResult<EsSelectSearchCO> getSelector(EsGoodsIndexDTO goodsSearch);
    DubboPageResult<EsPcSelectSearchCO> getPcSelector(EsGoodsIndexDTO goodsSearch);


}
