package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 限时抢购
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
public class EsSeckillQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 208745614233252259L;
    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String seckillName;
}
