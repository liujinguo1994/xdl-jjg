package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("es_page")
public class EsPage extends Model<EsPage> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 楼层名称
     */
    @TableField("page_name")
	private String pageName;
    /**
     * 楼层数据
     */
    @TableField("page_data")
	private String pageData;
    /**
     * 页面类型
     */
    @TableField("page_type")
	private String pageType;
    /**
     * 客户端类型
     */
    @TableField("client_type")
	private String clientType;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
