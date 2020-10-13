package com.xdl.jjg.model.co;/**
 * @author wangaf
 * @date 2019/10/29 14:50
 **/

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/10/29
 @Version V1.0
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EsParams {
    @Field(type = FieldType.Keyword)
    private String paramName;

    @Field(type = FieldType.Keyword)
    private String paramValue;
}
