package com.jjg.member.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 地区对象
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-15
 */
@Data
@ToString
public class RegionVO {
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 镇id
     */
    private Long townId;
    /**
     * 县区id
     */
    private Long countyId;
    /**
     * 省id
     */
    private Long provinceId;
    /**
     * 省名称
     */
    private String province;
    /**
     * 县区名称
     */
    private String county;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 镇名称
     */
    private String town;


}
