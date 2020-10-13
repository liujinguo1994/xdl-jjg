package com.xdl.jjg.util;

import java.math.BigDecimal;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/24 13:37
 */
public class DecimalUtil {

    public static Integer feeToYuan(BigDecimal amount) {
        Integer fee = 0;
        fee = amount.multiply(new BigDecimal(100)).intValue();
        return fee;
    }

}
