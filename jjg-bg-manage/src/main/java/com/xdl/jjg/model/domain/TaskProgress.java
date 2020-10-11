package com.xdl.jjg.model.domain;

import com.shopx.system.api.model.enums.ProgressEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 任务进度
 *
 */
@Data
public class TaskProgress implements Serializable {

    private static final long serialVersionUID = 2857710063571522383L;

    public static final String PROCESS = "PROCESS_";


    /**
     * id
     */
    private String id;
    /**
     * 百分比
     */
    private double sumPer;
    /**
     * 每步占比
     */
    private double stepPer;
    /**
     * 正在生成的内容
     */
    private String text;
    /**
     * 生成状态
     */
    private String taskStatus;
    /**
     * 任务总数
     */
    private int taskTotal;

    /**
     * 消息
     */
    private String message;

    public TaskProgress(){}

    /**
     * 构造时要告诉任务总数，以便计算每步占比
     *
     * @param total
     */
    public TaskProgress(int total) {

        /** 计算每步的百分比 */
        this.taskTotal = total;
        this.taskStatus = ProgressEnum.DOING.name();
        BigDecimal b1 = new BigDecimal("100");
        BigDecimal b2 = new BigDecimal("" + taskTotal);
        stepPer = b1.divide(b2, 5, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 完成一步
     */
    public void step(String text) {

        this.sumPer += this.stepPer;
        this.text = text;
    }

    /**
     * 成功
     */
    public void success() {
        this.sumPer = 100;
        this.text = "完成";
        this.taskStatus = ProgressEnum.SUCCESS.name();
    }

    /**
     * 失败
     *
     */
    public void fail(String text,String message) {
        this.taskStatus = ProgressEnum.EXCEPTION.name();
        this.message=message;
        this.text = text;
    }
}
