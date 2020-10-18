package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsSalesRankingGoodsDO;
import com.jjg.shop.model.dto.EsGoodsQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface GoodsService {

    /** 管理后台使用
     * 分页查询商品信息
     * @param esGoodsDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/adminGetEsGoodsList")
    DubboPageResult<EsGoodsDO> adminGetEsGoodsList(@RequestBody EsGoodsQueryDTO esGoodsDTO, @RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 管理后台商品下架 支持批量下架
     * @param ids
     * @return
     */
    @GetMapping("/adminUnderEsGoods")
    DubboResult<EsGoodsDO> adminUnderEsGoods(@RequestParam("ids") Integer[] ids);
    /**
     * 商品审核 支持批量审核
     * @param ids 商品id数组
     * @param status 审核状态
     * @param message 审核原因
     * @return
     */
    @PostMapping("/authEsGoods")
    DubboResult<EsGoodsDO> authEsGoods(@RequestParam("ids") Integer[] ids, @RequestParam("status") Integer status, @RequestParam("message") String message);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    @GetMapping("/getEsBuyerGoods")
    DubboResult<EsGoodsCO> getEsBuyerGoods(@RequestParam("id") Long id);

    /**
     * app端热门榜单排行商品信息
     */
    @GetMapping("/getByCategoryId")
    DubboPageResult<EsSalesRankingGoodsDO> getByCategoryId(@RequestParam("categoryId") Long categoryId, @RequestParam("goods") Long goods);

    /**
     * 根据分类ID 获取商品
     * @param category
     * @return
     */
    @GetMapping("/buyerGetGoodsByCategoryId")
    DubboPageResult<EsGoodsDO> buyerGetGoodsByCategoryId(@RequestParam("category") Long category);

    @GetMapping("/adminGetEsGoodsCount")
    DubboResult adminGetEsGoodsCount();

    /** 管理后台初始化索引使用
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/adminGetEsGoodsList")
    DubboPageResult<EsGoodsDO> adminGetEsGoodsList(@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

}
