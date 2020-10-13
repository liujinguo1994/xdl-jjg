package com.xdl.jjg.web.service;


import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  国寿商品 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-25
 */
public interface IEsLfcGoodsService {

    /**
     * 获取国寿上架商品
     * @auther: yuanj 595831329@qq.com
     */
    DubboResult<Long> getGoodsList();

}
