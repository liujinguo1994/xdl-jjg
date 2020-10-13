package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberShopScoreDO;
import com.xdl.jjg.model.dto.EsMemberShopScoreDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺评分 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberShopScoreService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberShopScoreDTO    店铺评分DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    DubboResult insertMemberShopScore(EsMemberShopScoreDTO memberShopScoreDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberShopScoreDTO    店铺评分DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    DubboResult updateMemberShopScore(EsMemberShopScoreDTO memberShopScoreDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    DubboResult<EsMemberShopScoreDO> getMemberShopScore(Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param goodId    goodId
     * @param memberId    memberId
     * @param orderSn    orderSn
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    DubboResult<EsMemberShopScoreDO> getMemberShopScoreByGoodAndMemberAndSn(Long goodId, Long memberId, String orderSn, Long shopId);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberShopScoreDTO  店铺评分DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberShopScoreDO>
     */
    DubboPageResult<EsMemberShopScoreDO> getMemberShopScoreList(EsMemberShopScoreDTO memberShopScoreDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
    DubboResult deleteMemberShopScore(Long id);

    /**
     * 計算好中差评价
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param memberShopScoreDTO    EsMemberShopScoreDTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberShopScoreDO>
     */
   /* DubboResult<EsMemberCommentDO> getCountCommentType(EsMemberShopScoreDTO memberShopScoreDTO);*/

}
