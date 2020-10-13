package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsShopDO implements Serializable {


    /**
     * 店铺ID
     */
	private Long id;
    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 会员名称
     */
	private String memberName;
    /**
     * 店铺状态
     */
	private String state;
    /**
     * 店铺名称
     */
	private String shopName;
    /**
     * 店铺创建时间
     */
	private Long shopCreatetime;
    /**
     * 店铺关闭时间
     */
	private Long shopEndtime;

    /**
     * 佣金比列
     */
	private Double commission;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 店铺详情
     */
    private EsShopDetailDO shopDetailDO;
    /**
     * 热门商品列表
     */
    private List<EsMemberGoodsDO> hotGoodList;

    /**
     * 上新商品列表
     */
    private List<EsMemberGoodsDO> newGoodList;

}
