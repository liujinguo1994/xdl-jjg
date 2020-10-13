package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EsSeckillTimetableDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时刻 如（上午7点：7  下午2点：14）
     */
    private Integer timeline;

    /**
     * 状态（1：抢购中，2：即将开始）
     */
    private Integer state;
    /**
     * 日期
     */
    private String day;


    /**
     * 结束倒计时时间戳（正在进行的场次为下一场的开始时间，未开始的为本场开始时间）
     */
    private Long remainTimestamp;
}
