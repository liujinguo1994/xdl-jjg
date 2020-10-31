package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommentInfoDO;
import com.jjg.member.model.domain.EsMemberCommentDetailDO;
import com.jjg.member.model.domain.GradeLevelDO;
import com.jjg.member.model.dto.EsMemberCommentCopyDTO;
import com.jjg.member.model.dto.EsQueryDetailCommentDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

public interface MemberCommentService {

    /**
     * 依据商品id和订单查询评论信息
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsId    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult<Double> getGoodCommentRate(Long goodsId);


    /**
     * 获取商品印象
     * @date: 2019/06/28 16:40:44
     * @param goodsId    商品主键
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult<List<Map<String, Object>>> getLabelsGroup(Long goodsId);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCommentDTO    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult insertMemberComment(EsMemberCommentCopyDTO memberCommentDTO);


    /**
     * 依据商品id和订单查询评论信息
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsId    评论DTO
     * @param orderSn esOrderDO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult<EsCommentInfoDO> getPCCommentInfoByGoodsIdAndOrderSn(Long goodsId, String orderSn, Long skuId);
    DubboResult<EsCommentInfoDO> getCommentInfoByGoodsIdAndOrderSn(Long goodsId, String orderSn, Long skuId);



    /**
     * 查询商品详情页面评价
     * @auther: lins 1220316142@qq.com
     * @param memberCommentDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsMemberCommentDetailDO> getMemberDetailCommentList(EsQueryDetailCommentDTO memberCommentDTO, Long memberId, int pageSize, int pageNum);



    /**
     * 统计商品评论数量
     * @auther: lins 1220316142@qq.com
     * @return
     */
    DubboResult<GradeLevelDO> getCountComment(Long goodsId);


}
