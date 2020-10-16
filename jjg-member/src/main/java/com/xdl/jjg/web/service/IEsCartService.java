package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCartDO;
import com.jjg.member.model.domain.EsCartNumDO;
import com.jjg.member.model.dto.EsCartDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-02
 */
public interface IEsCartService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:39:30
     * @param cartDTO    购物车DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    DubboResult<Long> insertCart(EsCartDTO cartDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param cartDTO    购物车DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    DubboResult updateCart(EsCartDTO cartDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    DubboResult<EsCartDO> getCart(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/03 13:42:53
     * @param cartDTO  购物车DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCartDO>
     */
    DubboPageResult<EsCartDO> getCartList(EsCartDTO cartDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    DubboResult deleteCart(Long id);

    /**
     * 根据会员id查询 会员购物车项数量 及 购物车id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 09:20:44
     * @param memberId    memberId
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    DubboResult <EsCartNumDO> getByMemberId(Long memberId);

    /**
     * 根据会员id清空购物车
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/03 09:29:40
     * @param memberId    memberId
     * @return: com.shopx.common.model.result.DubboResult<EsCartDO>
     */
    DubboResult clear(Long memberId);


}
