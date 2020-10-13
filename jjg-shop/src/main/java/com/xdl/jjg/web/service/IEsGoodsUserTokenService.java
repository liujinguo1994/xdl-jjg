package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodsUserTokenDO;
import com.xdl.jjg.model.dto.EsGoodsUserTokenDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-10 16:43:10
 */
public interface IEsGoodsUserTokenService {

    /**
     * 插入数据
     * @auther: waf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsUserTokenDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    DubboResult insertGoodsUserToken(EsGoodsUserTokenDTO goodsUserTokenDTO);

    /**
     * 根据条件更新更新数据
     * @auther: waf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsUserTokenDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    DubboResult updateGoodsUserToken(EsGoodsUserTokenDTO goodsUserTokenDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: waf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param goodsUserTokenDTO    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    DubboResult<EsGoodsUserTokenDO>     getGoodsUserToken(EsGoodsUserTokenDTO goodsUserTokenDTO);

    /**
     * 根据查询条件查询列表
     * @auther: waf 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @param goodsUserTokenDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsUserTokenDO>
     */
    DubboPageResult<EsGoodsUserTokenDO> getGoodsUserTokenList(EsGoodsUserTokenDTO goodsUserTokenDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: waf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    DubboResult deleteGoodsUserToken(Long id);

    DubboResult deleteGoodsUserToken(String token);
}
