package com.xdl.jjg.web.service.Impl;


import com.jjg.shop.model.constant.GoodsErrorCode;
import com.xdl.jjg.mapper.EsLfcGoodsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsLfcGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-25
 */
@Service
public class EsLfcGoodsServiceImpl implements IEsLfcGoodsService {

    private static Logger logger = LoggerFactory.getLogger(EsLfcGoodsServiceImpl.class);
    @Autowired
    private EsLfcGoodsMapper lfcGoodsMapper;

    @Override
    public DubboResult<Long> getGoodsList() {
        try {
            List<Long> goodsIds = this.lfcGoodsMapper.getLfcGoods();
            return DubboResult.success(goodsIds);
        } catch (
        ArgumentException ae){
            logger.error("获取商品id集合失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取商品id集合失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
