package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jjg.member.model.domain.EsAddCommentDO;
import com.jjg.member.model.domain.EsCommentGalleryDO;
import com.jjg.member.model.domain.EsCommentLabelDO;
import com.jjg.member.model.domain.EsCommentReplyDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:46
 */
@Data
@Api
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论主键
     */
	@ApiModelProperty(value = "评论主键")
	private Long id;

    /**
     * 商品id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品id")
	private Long goodsId;

    /**
     * skuid
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "skuid")
	private Long skuId;

    /**
     * 会员id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员id")
	private Long memberId;

    /**
     * 卖家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "卖家id")
	private Long shopId;

    /**
     * 会员名称
     */
	@ApiModelProperty(value = "会员名称")
	private String memberName;

    /**
     * 会员头像
     */
	@ApiModelProperty(value = "会员头像")
	private String memberFace;

    /**
     * 商品名称
     */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

    /**
     * 评论内容
     */
	@ApiModelProperty(value = "评论内容")
	private String content;

    /**
     * 评论时间
     */
	@ApiModelProperty(value = "评论时间")
	private Long createTime;

    /**
     * 是否有图片 1 有 0 没有
     */
	@ApiModelProperty(value = "是否有图片 1 有 0 没有")
	private Integer haveImage;

    /**
     * 状态  0 正常 1 删除
     */
	@ApiModelProperty(value = "状态  0 正常 1 删除")
	private Integer state;

    /**
     * 好中差评(0:好评，1中评，2差评)
     */
	@ApiModelProperty(value = "好中差评(0:好评，1中评，2差评)")
	private String grade;

    /**
     * 订单明细编号
     */
	@ApiModelProperty(value = "订单明细编号")
	private String orderSn;

    /**
     * 是否回复 1 是 0 否
     */
	@ApiModelProperty(value = "是否回复 1 是 0 否")
	private Integer replyStatus;

    /**
     * 评价标签
     */
	@ApiModelProperty(value = "评价标签")
	private String labels;

    /**
     * 综合评分
     */
	@ApiModelProperty(value = "综合评分")
	private Double commentScore;

    /**
     * 商品编号
     */
	@ApiModelProperty(value = "商品编号")
	private String goodsSn;

    /**
     * 匿名(2:匿名，1:否)
     */
	@ApiModelProperty(value = "匿名(2:匿名，1:否)")
	private Integer isAnonymous;

	/**
	 * 标签信息
	 */
	@ApiModelProperty(value = "标签信息")
	private List<EsCommentLabelDO> esCommentLabelDOList;
	/**
	 * 评论图片路径
	 */
	@ApiModelProperty(value = "评论图片路径")
	private List<EsCommentGalleryDO> commentsImage;
	/**
	 * 商家回复
	 */
	@ApiModelProperty(value = "商家回复")
	private EsCommentReplyDO replayContent;
	/**
	 * 追加评论内容
	 */
	@ApiModelProperty(value = "追加评论内容")
	private EsAddCommentDO addContent;

	protected Serializable pkVal() {
		return this.id;
	}

}
