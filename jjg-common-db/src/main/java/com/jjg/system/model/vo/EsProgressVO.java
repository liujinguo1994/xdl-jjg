package com.jjg.system.model.vo;

import com.xdl.jjg.model.domain.TaskProgress;
import com.xdl.jjg.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 进度条
 */
@Data
@ApiModel
public class EsProgressVO implements Serializable {


    private static final long serialVersionUID = 7371147231726752886L;

    @ApiModelProperty("百分比")
    private Integer percentage = 0;
    @ApiModelProperty("状态：枚举 ProgressEnum")
    private String status = "";
    @ApiModelProperty("正在生成")
    private String text = "";
    @ApiModelProperty("消息")
    private String message = "";

    public EsProgressVO() {
    }

    public EsProgressVO(TaskProgress taskProgress) {
        this.percentage = (int) taskProgress.getSumPer();
        this.status = taskProgress.getTaskStatus();
        if (!StringUtil.isEmpty(taskProgress.getText())) {
            this.text = taskProgress.getText();
        }
        if (!StringUtil.isEmpty(taskProgress.getMessage())) {
            this.text = taskProgress.getMessage();
        }
    }

    public EsProgressVO(Integer percentage, String status, String text, String message) {
        this.percentage = percentage;
        this.status = status;
        if (!StringUtil.isEmpty(text)) {
            this.text = text;
        }
        if (!StringUtil.isEmpty(message)) {
            this.text = text;
        }
    }

    public EsProgressVO(Integer percentage, String status) {
        this.percentage = percentage;
        this.status = status;
    }
}
