package com.xdl.jjg.model.co;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author wangaf
 * ES 索引实体类
 */
@Document(indexName = "zfsc_dev" ,type = "goods",shards = 1,replicas = 0)
@Data
public class EsGoodsIndexDevCO {
    @Id
    private Long  id ;

    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String goodsName;

    @Field(type = FieldType.Text)
    private String original;

    @Field(type = FieldType.Integer)
    private Integer buyCount;

    @Field(type = FieldType.Integer)
    private Integer shopId;

    @Field(type = FieldType.Text)
    private String shopName;

    @Field(type = FieldType.Integer)
    private Integer commentNum;

    @Field(type = FieldType.Double)
    private Double grade;

    @Field(type = FieldType.Double)
    private Double money;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Text)
    private String categoryPath;

    @Field(type = FieldType.Integer)
    private Integer isDel;

    @Field(type = FieldType.Integer)
    private Integer marketEnable;

    @Field(type = FieldType.Integer)
    private Integer isAuth;

    @Field(type = FieldType.Text)
    private String intro;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Integer)
    private Integer isGifts;

    @Field(type = FieldType.Nested,index= true, store=true)
    private List<EsParams> params;
    @Field(type = FieldType.Keyword)
    private  String brandIdStr;
    @Field(type = FieldType.Keyword)
    private String categoryIdStr;

    @Field(type = FieldType.Keyword)
    private String goodsIdStr;
}
