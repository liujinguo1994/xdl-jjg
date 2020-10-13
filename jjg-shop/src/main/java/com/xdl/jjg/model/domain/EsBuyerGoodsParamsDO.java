package com.xdl.jjg.model.domain;/**
 * @author wangaf
 * @date 2019/11/2 16:29
 **/

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/2
 @Version V1.0
 **/
@Data
public class EsBuyerGoodsParamsDO implements Serializable {

    /**
     * 参数组关联的参数集合
     */
    private List<EsBuyerParamsDO> params;

    /**
     * 参数组名称
     */
    private String groupName;
    /**
     * 参数组id
     */
    private Integer groupId;

}
