package com.xdl.jjg.web.controller.feign;


import com.jjg.shop.model.co.EsGoodsCO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsContorller {

    @Autowired
    private IEsGoodsService iEsGoodsService;

    @GetMapping("/getGoodsById")
    DubboResult<EsGoodsCO> getGoodsById(@RequestParam("id") Long id) {
        DubboResult<EsGoodsCO> esGoods = iEsGoodsService.getEsGoods(id);
        return esGoods;
    }
}
