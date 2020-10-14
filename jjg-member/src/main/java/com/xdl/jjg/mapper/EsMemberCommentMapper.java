package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsAdminManagerDO;
import com.jjg.member.model.domain.EsCommentLabelDO;
import com.jjg.member.model.domain.EsMemberCommentDetailDO;
import com.jjg.member.model.dto.EsQueryDetailCommentDTO;
import com.jjg.member.model.dto.QueryCommentListDTO;
import com.xdl.jjg.entity.EsMemberComment;
import com.xdl.jjg.model.domain.EsCommentInfoDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberCommentMapper extends BaseMapper<EsMemberComment> {

    /**
     * 查询评价标签
     * @param array
     * @return
     */
    List<EsCommentLabelDO> getCommentLabel(@Param("array") String[] array);

    /**
     * 查询评价所有信息
     * @param goodsId
     * @param orderSn
     * @return
     */
    EsCommentInfoDO getCommentByOrdersAndGoodsId(@Param("goodsId") Long goodsId, @Param("orderSn") String orderSn, @Param("skuId") Long skuId);

    /**
     * 查询商品详情页面评价信息
     */
    IPage<EsMemberCommentDetailDO> getMemberDetailCommentList(Page page, @Param("es") EsQueryDetailCommentDTO esQueryDetailCommentDTO);

    /**
     * 查询后台会员管理列表
     */
    IPage<EsAdminManagerDO> getAdminManagerList(Page page, @Param("es") QueryCommentListDTO queryCommentListDTO);
    /**
     * 查询当天评价次数
     * @param memberId
     * @param timesNow
     * @return
     */
    int getCollectCommentNum(@Param("memberId") Long memberId, @Param("timesNow") String timesNow);

    List<String> getLabelsGroup(@Param("goodsId") Long goodsId);

}
