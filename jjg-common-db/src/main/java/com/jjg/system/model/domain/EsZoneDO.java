package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 专区管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Data
public class EsZoneDO extends Model<EsZoneDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 专区名称
     */
    private String zoneName;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 图片1
     */
    private String picture1;

    /**
     * 图片2
     */
    private String picture2;
}
