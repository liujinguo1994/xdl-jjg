package com.xdl.jjg.web.service.Impl;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.service.IEsLfcGoodsService;
import com.shopx.goods.dao.mapper.EsLfcGoodsMapper;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-25
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsLfcGoodsService.class, timeout = 50000)
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
