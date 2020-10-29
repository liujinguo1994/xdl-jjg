package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsTradeDTO;
import com.xdl.jjg.entity.EsBuyerTrade;
import com.xdl.jjg.entity.EsTrade;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单主表-es_trade Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsTradeMapper extends BaseMapper<EsTrade> {


    /**
     * 查询订单列表
     * @author: libw 981087977@qq.com
     * @date: 2019/07/27 15:59:57
     * @param page      page插件
     * @param tradeDTO  查询条件
     * @return: java.util.List<com.shopx.trade.dao.entity.EsBuyerTrade>
     */
    IPage<EsBuyerTrade> getOrderList(IPage page, EsBuyerTrade tradeDTO);

    /**
     * 更新退款后的金额
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 09:13:57
     * @param payMoney          第三方支付金额
     * @param balance           余额支付金额
     * @param refundPayMoney    第三方退款金额
     * @param refundBalance     余额退款金额
     * @param tradeSn           交易单号
     * @return: int
     */
    int updateTradeMoney(@Param("payMoney") Double payMoney, @Param("balance") Double balance, @Param("refundPayMoney") Double refundPayMoney,
                         @Param("refundBalance") Double refundBalance, @Param("tradeSn") String tradeSn);

    int updateTradeStatus(@Param("status") String status, @Param("tradeSn") String tradeSn);

    EsTradeDO getEsTradeByTradeSn(@Param("tradeSn") String tradeSn);

    IPage<EsTrade> getTradeList(Page page, @Param("tradeDTO") EsTradeDTO tradeDTO);

    Double getBlackCardUserMessage(Long black_card_end, Long black_card_start, Long memberId);

}
