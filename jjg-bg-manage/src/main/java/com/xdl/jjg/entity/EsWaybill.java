package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.gson.Gson;
import com.jjg.system.model.vo.EsWaybillVO;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_waybill")
public class EsWaybill extends Model<EsWaybill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否开启
     */
    private Integer open;
    /**
     * 电子面单配置
     */
    private String config;
    /**
     * 电子面单bean
     */
    private String bean;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public EsWaybill() {
    }

    public EsWaybill(EsWaybillVO waybillVO) {
        this.id = waybillVO.getId();
        this.name = waybillVO.getName();
        this.open = waybillVO.getOpen();
        this.bean = waybillVO.getBean();
        Gson gson = new Gson();
        this.config = gson.toJson(waybillVO.getConfigItems());
    }

}
