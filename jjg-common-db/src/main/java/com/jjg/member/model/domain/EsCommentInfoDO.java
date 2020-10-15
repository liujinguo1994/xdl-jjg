package com.jjg.member.model.domain;

import com.shopx.member.api.model.domain.vo.CommentImageVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCommentInfoDO implements Serializable {


    /**
     * 评论主键
     */
	private Long id;
    /**
     * 评价用户ID
     */
	private Long memberId;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 评论内容
     */
	private String content;
    /**
     * 好中差评
     */
	private String grade;
    /**
     * 综合评分
     */
    private Double commentScore;
    /**
     * 标签信息
     */
    private List<String> tags;
    /**
     * 图片信息
     */
    private List<String> originals;

    /**
     * 图片信息
     */
    private List<EsCommentGalleryDO> imgList;
    /**
     * 评价标签
     */
    private String labels;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * skuId
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 是否有图片 1 有 2没有
     */
    private Integer haveImage;

    /**
     * 追评内容
     */
    private EsAddCommentDO addContentDO;

    /**
     * 描述相符度评分
     */
    private Double descriptionScore;
    /**
     * 服务评分
     */
    private Double serviceScore;
    /**
     * 发货速度评分
     */
    private Double deliveryScore;
    /**
     * 图片地址
     */
    private String original;
    // APP评价多张图片信息
    private CommentImageVO commentImageVO;

    // PC评价多张图片信息
    private List<CommentImageVO> commentImageVOList;
}
