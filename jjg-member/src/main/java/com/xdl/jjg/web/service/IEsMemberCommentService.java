package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.*;
import com.jjg.member.model.dto.EsMemberCommentCopyDTO;
import com.jjg.member.model.dto.EsMemberCommentDTO;
import com.jjg.member.model.dto.EsQueryDetailCommentDTO;
import com.jjg.member.model.dto.QueryCommentListDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberCommentService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCommentDTO    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult insertMemberComment(EsMemberCommentCopyDTO memberCommentDTO);

    /**
     * 自动评论插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult AutoInsertMemberComment();

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCommentDTO    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult updateMemberComment(EsMemberCommentDTO memberCommentDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult<EsMemberCommentDO> getMemberComment(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCommentDTO  评论DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCommentDO>
     */
    DubboPageResult<EsMemberCommentDO> getMemberCommentList(EsMemberCommentDTO memberCommentDTO, int pageNum, int pageSize);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult deleteMemberComment(Long id);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCommentDTO    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult insertMemberCommentOrder(EsMemberCommentDTO memberCommentDTO);

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
     * 依据商品id和订单查询评论信息
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsId    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    DubboResult<Double> getGoodCommentRate(Long goodsId);

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
     * 获取商品印象
     * @date: 2019/06/28 16:40:44
     * @param goodsId    商品主键
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult<List<Map<String, Object>>> getLabelsGroup(Long goodsId);

    /**
     * 查询评价列表
     * @auther: lins 1220316142@qq.com
     * @param queryCommentListDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
     DubboPageResult<EsAdminManagerDO> getAdminManagerList(QueryCommentListDTO queryCommentListDTO, int pageSize, int pageNum);

    /**
     * 批量删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @param ids    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    DubboResult deleteDiscountComment(Integer[] ids);

    /**
     * 统计商品评论数量
     * @auther: lins 1220316142@qq.com
     * @return
     */
    DubboResult<GradeLevelDO> getCountComment(Long goodsId);


    /**
     * 统计商品评论数量
     * @auther: yuanj 595831329@qq.com
     * @return
     */
    DubboResult<EsCommentCountDO> getCommentCount(Long goodsId);

    }


