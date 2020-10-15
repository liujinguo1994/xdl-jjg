package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 *  前端控制器-移动端-售后订单查询参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-03
 */
@ApiModel
@Data
public class EsWapAfterSaleQueryForm extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;
    /**
     * 输入框输入值（订单编号,商品名称）
     */
    private String keyword;

    /**
     * 查询时间段
     */
    //private Integer time;

}
