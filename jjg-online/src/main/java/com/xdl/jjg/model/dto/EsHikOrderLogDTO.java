package com.xdl.jjg.model.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
public class EsHikOrderLogDTO extends Model<EsHikOrderLogDTO> {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 海康操作人姓名
     */
    private String name;

    /**
     * 创建时间（操作时间）
     */
    private Long createTime;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 退款单号
     */
    private String refundSn;

    /**
     * 订单号
     */
    private String orderSn;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
