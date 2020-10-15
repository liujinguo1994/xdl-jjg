package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("秒杀时刻表")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSeckillTimetableVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 时刻 如（上午7点：7  下午2点：14）
     */
    @ApiModelProperty("时刻 例（上午7点：7  下午2点：14）")
    private Integer timeline;

    @ApiModelProperty("状态（1：抢购中，2：即将开始）")
    private Integer state;

    @ApiModelProperty("日期")
    private String day;

    @ApiModelProperty("结束倒计时时间戳（正在进行的场次为下一场的开始时间，未开始的为本场开始时间）")
    private Long remainTimestamp;

    List<EsSeckillApplyVO> esSeckillApplyList;
}
