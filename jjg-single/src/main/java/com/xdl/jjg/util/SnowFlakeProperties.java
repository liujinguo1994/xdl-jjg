package com.xdl.jjg.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author sanqi
 * @date 2019/07/16
 */
@Data
@Component
public class SnowFlakeProperties {

    @Value("${snowFlak.datacenterId}")
    private Long datacenterId;
    @Value("${snowFlak.machineId}")
    private Long machineId;


}
