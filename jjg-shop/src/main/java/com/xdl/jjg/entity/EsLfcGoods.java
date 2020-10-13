package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 人寿商品
 * @author yuanj
 * @version v2.0
 * @since v7.0.0
 * 2020-03-25 16:32:45
 */
@Data
@TableName("es_lfc_goods")
public class EsLfcGoods implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**商品id*/
    @TableField("goods_id")
    private Long goodsId;

    /**审核状态 1审核通过 2 待审核*/
    @TableField("is_auth")
    private Integer isAuth;

}