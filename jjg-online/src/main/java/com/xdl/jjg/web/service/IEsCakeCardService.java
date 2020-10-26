package com.xdl.jjg.web.service;/**
 * @author wangaf
 * @date 2020/3/31 13:43
 **/


import com.jjg.trade.model.domain.EsCakeCardDO;
import com.jjg.trade.model.dto.EsCakeCardDTO;
import com.jjg.trade.model.dto.EsCakeCardQueryDTO;
import com.jjg.trade.model.dto.EsCakeImportDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 @Author wangaf 826988665@qq.com
 @Date 2020/3/31
 @Version V1.0
 **/
public interface IEsCakeCardService {

    DubboResult<EsCakeCardDO> getByCode(String code);

    DubboResult<EsCakeCardDO> getLowCode();

    DubboResult updateCakeCard(EsCakeCardDTO esCakeCardDTO);

    /**
     * 修改商品
     *
     * @param param
     *            商品
     * @return Goods 商品
     */
    DubboPageResult<EsCakeCardDO> list(EsCakeCardQueryDTO param);

    DubboResult<EsCakeCardDO>  getCakeCardByOrderSn(String orderSn);
    /**
     * 获取商品
     *
     * @param id
     *            商品主键
     * @return GoodsSku 商品
     */
    DubboResult<EsCakeCardDO> getModel(Integer id);



    /**
     * 新增卡券
     *
     * @param cakeCardDO
     *            商品主键
     * @return GoodsSku 商品
     */
    DubboResult<EsCakeCardDO> add(EsCakeCardDTO cakeCardDO);

    /**
     * 批量修改余额
     *
     * @return 会员信息
     */
    DubboResult<EsCakeImportDTO> importExcel(byte[] base64);





    /**
     * 修改蛋糕卡券
     * @param cakeCardDO 蛋糕卡券
     * @param id 蛋糕卡券主键
     * @return cakeCardDO 蛋糕卡券
     */
    DubboResult<EsCakeCardDO> edit(EsCakeCardDO cakeCardDO, Integer id);

}
