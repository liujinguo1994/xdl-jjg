package com.jjg.member.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/8/10 9:37
 */
@Data
public class LabelValueBeanVO implements Serializable {
    private String key;
    private String value;

    private List<LabelValueBeanVO> subObj;
}
