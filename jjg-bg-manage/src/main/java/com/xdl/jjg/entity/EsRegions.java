package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xdl.jjg.model.domain.EsRegionsDO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_regions")
public class EsRegions extends Model<EsRegions> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 父地区id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("parent_id")
    private Long parentId;
    /**
     * 路径
     */
    @TableField("region_path")
    private String regionPath;
    /**
     * 级别
     */
    @TableField("region_grade")
    private Integer regionGrade;
    /**
     * 名称
     */
    @TableField("local_name")
    private String localName;
    /**
     * 邮编
     */
    private String zipcode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * 转DO
     */
    public EsRegionsDO toDO() {
        EsRegionsDO regionsDO = new EsRegionsDO();
        regionsDO.setLocalName(this.getLocalName());
        regionsDO.setParentId(this.getParentId());
        regionsDO.setId(this.getId());
        regionsDO.setChildren(new ArrayList<EsRegionsDO>());
        regionsDO.setRegionGrade(this.getRegionGrade());
        return regionsDO;
    }

}
