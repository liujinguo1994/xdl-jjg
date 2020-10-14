package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionShopLabelDO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标签名字
     */
    private String tagName;
    /**
     * 销量
     */
    private List<EsMemberGoodsDO> memberGoodsDO;

}
