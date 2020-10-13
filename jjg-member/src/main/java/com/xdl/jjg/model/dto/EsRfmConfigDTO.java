package com.xdl.jjg.model.dto;

import com.shopx.member.api.model.domain.vo.EsMemberRfmConfigVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * RFM
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-07-25 10:49:10
 */
@Data
@ToString
public class EsRfmConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * list属性
     */
    private List<EsMemberRfmConfigVO> rfmConfigVOList;


}
