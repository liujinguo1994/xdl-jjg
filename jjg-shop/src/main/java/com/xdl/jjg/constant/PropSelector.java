package com.xdl.jjg.constant;


import com.xdl.jjg.model.co.SearchSelector;

import java.io.Serializable;
import java.util.List;

/**
 * @author waf
 * @version v2.0
 * @Description: 参数属性选择器
 * @since v7.0.0
 */
public class PropSelector implements Serializable {

    private String key;

    private List<SearchSelector> value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<SearchSelector> getValue() {
        return value;
    }

    public void setValue(List<SearchSelector> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropSelector{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
