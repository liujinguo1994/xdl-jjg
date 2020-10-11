package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.gson.Gson;
import com.xdl.jjg.model.vo.EsSmsPlatformVO;
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
@TableName("es_sms_platform")
public class EsSmsPlatform extends Model<EsSmsPlatform> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 平台名称
     */
	private String name;
    /**
     * 是否开启
     */
	private Integer open;
    /**
     * 配置
     */
	private String config;
    /**
     * bean
     */
	private String bean;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public EsSmsPlatform() {}

	public EsSmsPlatform(EsSmsPlatformVO smsPlatformVO) {
		this.id = smsPlatformVO.getId();
		this.name = smsPlatformVO.getName();
		this.open = smsPlatformVO.getOpen();
		this.bean = smsPlatformVO.getBean();
		Gson gson = new Gson();
		this.config = gson.toJson(smsPlatformVO.getConfigItems());
	}

}
