package com.jjg.trade.model.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-06-02
 */
@Data
@ToString
@Accessors(chain = true)
public class EsOrderCommDO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 已评价数量
     */
	private Integer hasComm;
    /**
     *待评价数量
     */
	private Integer notComm;



}
