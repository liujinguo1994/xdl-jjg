package com.xdl.jjg.model.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 促销活动工具枚举
 *
 * @author Snow
 * @version v1.0
 * 2017年08月18日17:55:35
 * @since V6.4
 */
public enum PromotionTypeEnum {

    /**
     * 不参与活动（指不参与任何单品活动）
     */
    NO("no", ""),

    /**
     * 单品立减活动
     * 计算价格时:
     * 1、CartVO.SkuList.Sku.purchase_price 需要修改。<br>
     * 2、CartVO.price.discount_price 需要累加。<br>
     * 3、CartVO.SkuList.Sku.subtotal 需要计算<br>
     */
    MINUS("minusPlugin", "单品立减"),

    /**
     * 第二件半价活动
     * 计算价格时:
     * 1、CartVO.price.discount_price 需要累加。<br>
     * 2、CartVO.SkuList.Sku.subtotal 需要计算<br>
     */
    HALF_PRICE("halfPricePlugin", "第二件半价"),

    /**
     * 商品折扣活动
     * 计算价格时:
     * 1、CartVO.price.discount_price 需要累加。<br>
     */
    GOODS_DISCOUNT("goodsDiscountPlugin", "商品折扣"),

    /**
     * 满优惠活动
     * 计算价格时:
     * 1、CartVO.price.discount_price 需要累加。<br>
     */
    FULL_DISCOUNT("fullDiscountPlugin", "满优惠"),

    /**
     * 公司折扣活动
     * 计算价格时:
     * 1、CartVO.price.discount_price 需要累加。<br>
     */
    COMPANY_DISCOUNT("companyDiscountPlugin", "公司折扣"),

    /**
     * 限时抢购
     * 如果商品参与的显示抢购活动，则不允许参与其他活动。
     * 计算价格时：
     * 1、如果购买的数量没有超过售空数量，则商品价格为商家设置的活动价，如果超过售空数量，则将商品的价格设置为原价。
     */
    SECKILL("seckillPlugin", "限时抢购");

    private String pluginId;
    private String promotionName;

    /**
     * 构造器
     *
     * @param pluginId      插件id
     * @param promotionName 活动名称
     */
    PromotionTypeEnum(String pluginId, String promotionName) {
        this.pluginId = pluginId;
        this.promotionName = promotionName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * 读取单品活动集合
     * 活动顺序: 商品打折、第二件半价、单品立减、企业员工优惠、满减满赠
     * @return
     */
    public static List<String> getSingle() {
        List<String> pluginId = new ArrayList<>();
        pluginId.add(PromotionTypeEnum.GOODS_DISCOUNT.getPluginId());
        pluginId.add(PromotionTypeEnum.HALF_PRICE.getPluginId());
        pluginId.add(PromotionTypeEnum.MINUS.getPluginId());
        pluginId.add(PromotionTypeEnum.SECKILL.getPluginId());
        pluginId.add(PromotionTypeEnum.COMPANY_DISCOUNT.getPluginId());

        return pluginId;
    }

    /**
     * 读取组合活动集合
     *
     * @return
     */
    public static List<String> getGroup() {
        List<String> pluginId = new ArrayList<>();
        pluginId.add(PromotionTypeEnum.FULL_DISCOUNT.getPluginId());
        return pluginId;
    }

    /**
     * 读取某些活动具有独立库存的活动工具
     * 1：限时抢购活动 (订单付款时，增加已售数量)
     * 2：团购活动 (订单创建时，增加已售数量)
     *
     * @return
     */
    public static List<String> getIndependent() {
        List<String> pluginId = new ArrayList<>();
        pluginId.add(PromotionTypeEnum.SECKILL.getPluginId());
        return pluginId;
    }

    /**
     * 判断是否是单品活动
     *
     * @param type
     * @return
     */
    public static boolean isSingle(String type) {
        if (PromotionTypeEnum.GOODS_DISCOUNT.name().equals(type)) {
            return true;
        } else if (PromotionTypeEnum.MINUS.name().equals(type)) {
            return true;
        } else if (PromotionTypeEnum.HALF_PRICE.name().equals(type)) {
            return true;
        } else if (PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(type)) {
            return true;
        } else if (PromotionTypeEnum.SECKILL.name().equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是组合活动
     *
     * @param type
     * @return
     */
    public boolean isGroup(String type) {
        if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 获取插件名称。根据type字段
     *
     * @param type
     * @return
     */
    public static String getPlugin(String type) {

        if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(type)) {
            return PromotionTypeEnum.FULL_DISCOUNT.pluginId;
        } else if (PromotionTypeEnum.HALF_PRICE.name().equals(type)) {
            return PromotionTypeEnum.HALF_PRICE.pluginId;
        } else if (PromotionTypeEnum.MINUS.name().equals(type)) {
            return PromotionTypeEnum.MINUS.pluginId;
        } else if (PromotionTypeEnum.COMPANY_DISCOUNT.name().equals(type)) {
            return PromotionTypeEnum.COMPANY_DISCOUNT.pluginId;
        } else {
            return "";
        }
    }

    /**
     * 根据名称、pluginId 匹配活动
     * @return 返回活动
     */
    public static PromotionTypeEnum match(String s){
        if (StringUtils.equalsAny(s,FULL_DISCOUNT.pluginId,FULL_DISCOUNT.toString())) {
            return FULL_DISCOUNT;
        }else if (StringUtils.equalsAny(s,HALF_PRICE.pluginId,HALF_PRICE.toString())) {
            return HALF_PRICE;
        }else if (StringUtils.equalsAny(s,MINUS.pluginId,MINUS.toString())) {
            return MINUS;
        }else if (StringUtils.equalsAny(s,GOODS_DISCOUNT.pluginId,GOODS_DISCOUNT.toString())) {
            return GOODS_DISCOUNT;
        }else if (StringUtils.equalsAny(s,SECKILL.pluginId,SECKILL.toString())) {
            return SECKILL;
        }else if (StringUtils.equalsAny(s,COMPANY_DISCOUNT.pluginId,COMPANY_DISCOUNT.toString())) {
            return COMPANY_DISCOUNT;
        }else {
            return NO;
        }
    }



}
