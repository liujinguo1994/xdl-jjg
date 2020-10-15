package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xdl.jjg.response.web.QueryPageForm;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-25
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsHomeForm extends QueryPageForm {



}
