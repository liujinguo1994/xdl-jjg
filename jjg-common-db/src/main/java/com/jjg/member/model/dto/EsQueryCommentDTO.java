package com.jjg.member.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsQueryCommentDTO implements Serializable {


    /**
     * 查询类型0好评，1中评，2差评，3带图，4全部
     */
    private Integer type;
    /**
     * 商品id
     */
    private  Long goodsId;


}
