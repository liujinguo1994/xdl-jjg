package com.jjg.system.model.dto;


import com.jjg.system.model.vo.EsClearingCycleVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 结算周期设置DTO
 */
@Data
public class EsClearingCycleSettingsDTO implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;

    /**
     * 结算周期设置
     */
    private List<EsClearingCycleVO> ClearingCycleSettings;

}
