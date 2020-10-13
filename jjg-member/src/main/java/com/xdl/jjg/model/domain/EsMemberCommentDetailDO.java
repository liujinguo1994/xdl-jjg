package com.xdl.jjg.model.domain;

import com.shopx.member.api.model.domain.vo.wap.EsWapMemberSpecValuesVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberCommentDetailDO implements Serializable {


    private static final long serialVersionUID = 1302991016055735957L;
    /**
     * 评论主键
     */
	private Long id;
    /**
     * 是否有图片
     */
    private Integer haveImage;
    /**
     * 会员头像
     */
    private String face;
    /**
     * 会员手机号
     */
    private String mobile;
    /**
     * 好中差评
     */
    private String grade;
    /**
     * 综合评分
     */
    private Double commentScore;
    /**
     * 评论时间
     */
    private Long createTime;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * skuId
     */
    private Long skuId;
    /**
     * 评论图片路径
     */
    private List<EsCommentGalleryDO> commentsImage;
    /**
     * 商品标签
     */
    private String goodTag;
    /**
     * 商品编号
     */
    private String goodsSn;
    /**
     * 买家昵称
     */
    private String nickname;
    /**
     * 点赞数量
     */
    private Integer supportNum;
    /**
     * 评论标签
     */
    private String labels;
    /**
     * 评论标签内容
     */
    private List<String> labelsName;
    /**
     * 商家回复
     */
    private EsCommentReplyDO replayContent;
    /**
     * 追加评论内容
     */
    private EsAddCommentDO addContent;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 商家回复内容
     */
    private String replayContentInfo;

    /**
     * 追加评论内容
     */
    private String addContentInfo;

    /**
     * 是否点赞 0：否 1：是
     */
    private Integer judgeCommentSupport;

    private Long memberId;

   private Long isAnonymous;
    /**
     * 商品规格
     */
    private List<EsWapMemberSpecValuesVO> esMemberSpecValuesDO;

}
