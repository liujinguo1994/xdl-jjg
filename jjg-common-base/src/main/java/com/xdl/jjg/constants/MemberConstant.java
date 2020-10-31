package com.xdl.jjg.constants;

public class MemberConstant {
    /**
     * 店铺置顶
     */
    public static final Integer IsTop = 2;
    /**
     * 店铺取消置顶
     */
    public static final Integer uncelTop = 1;
    /**
     * 逻辑删除判断
     */
    public static final Integer IsCommon =0;
    /**
     * 逻辑删除标识
     */
    public static final Integer IsDel = 1;
    /**
     * 全部
     */
    public static final Integer AllMessage =3;
    /**
     * 默认
     */
    public static final Integer IsDefault =0;
    /**
     * 消息系统类型
     */
    public static final Integer msgSysType =0;
    /**
     * 消息店铺类型
     */
    public static final Integer msgShopType =1;
    /**
     * 商品失效
     */
    public static final Integer effect =2;
    /**
     * 降价
     */
    public static final Integer cutPrice =2;
    /**
     * 全部商品
     */
    public static final Integer allGoods =0;
    /**
     * 页数
     */
    public static final Long emptyPage = Long.valueOf(0);
    /**
     * 降价提醒
     */
    public static final Integer cutPriceRemind =2;
    /**
     * 取消降价提醒
     */
    public static final Integer deletePriceRemind =1;
    /**
     * 待付款
     */
    public static final Integer confirm =1;
    /**
     * 待发货
     */
    public static final Integer paidOff =2;
    /**
     * 待收货
     */
    public static  final Integer shipped =3;
    /**
     * 优惠券已使用
     */
    public static final Integer send =2;
    /**
     * 店员启用
     */
    public static final Integer enable =0;
    /**
     * 店员禁用
     */
    public static final Integer unable =1;
    /**
     * 是超级管理员
     */
    public static final Integer isAdmin =1;
    /**
     * 不是超级管理员
     */
    public static final Integer notAdmin =0;
    /**
     * 优惠券未使用
     */
    public static final Integer noSend =1;
    /**
     * 商品标签
     */
    public static final Integer goodsTag =0;
    /**
     * 物流标签
     */
    public static final Integer deliveryTag =1;
    /**
     * 服务标签
     */
    public static final Integer serviceTag =2;
    /**
     * 正常
     */
    public static final Integer commentDefault =0;
    /**
     * 删除
     */
    public static final Integer commentDelete =1;
    /**
     * 已评价
     */
    public static final Integer commented =1;
    /**
     * 待评价
     */
    public static final Integer toBeComment =2;
    /**
     * 好评
     */
    public static final Integer goodsComment =0;
    /**
     **优惠券未使用
     */
    public static final Integer userCoupon = 1;
    /**
     * 优惠券失效
     */
    public static final Integer loseUse = 3;
    /**
     * 1已回复
     */
    public static final Integer repalyContent =1;
    /**
     * 0未回复
     */
    public static final Integer noRepalyContent =0;
    /**
     * 收藏模型策略开关 0 开
     */
    public static final Integer collectionStrategySwitch = 0;
    /**
     * 店铺收藏
     */
    public static  final Integer collectionShop = 2;
    /**
     * 商品收藏
     */
    public static  final Integer collectionGoods =1;
    /**
     * 评论
     */
    public static final Integer collectionComment =3;
    /**
     * 会员状态正常
     */
    public static  final Integer memberDefault = 1;
    /**
     * 成长值 1正常
     */
    public static final Integer growthTypeCommen =1;
    /**
     * 成长值 2删除
     */
    public static final Integer growthTypeDelete =2;
    /**
     * 专票
     */
    public static final Integer subjectReceipt=1;
    /**
     * 电子普通发票
     */
    public static final Integer ommentReceipt=2;
    /**
     * 开票成功
     */
    public static final String receiptSuccess = "E0000";
    /**
     * 发票查询成功
     */
    public static final String queryReceiptSuccess = "E0000";
    /**
     * 税率为0
     */
    public static final String reateValue = "0";
    /**
     * 已开票
     */
    public static final Integer succesecReceiptState =1;
    /**
     * 未开票
     */
    public static final Integer failReceiptState =2;

    /**
     *  类型 0:RFM策略
     */
    public static  final Integer rfmStragety =0;
    /**
     * 类型 1:评价模型策略
     */
    public static final Integer commentStragety =1;
    /**
     * 类型 2收藏模型策略
     */
    public static final Integer collectionStragety =2;
    /**
     * 无图片
     */
    public static final Integer emptyImage =0;
    /**
     * 有图片
     */
    public static final Integer hasImage =1;
    /**
     * 评论状态默认
     */
    public static final Integer defaultCommentState = 0;
    /**
     * 充值
     */
    public static final String recharge = "1";

    /**
     * 查询降价商品
     */
    public static final Integer queryCutPrice =1;
    /**
     * 抬头类型
     */
    public static final String receiptTitle = "企业";

    /**
     * 查询降价商品
     */
    public static final Integer complaint =0;

    /**
     * 查询降价商品
     */
    public static final Integer report =1;

    /**
     * 查询降价商品
     */
    public static final Integer complaintIsNotDel =0;

    /**
     * 查询降价商品
     */
    public static final Integer complaintIsDel =1;
}
