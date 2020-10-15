package com.jjg.trade.model.vo;


import com.jjg.trade.model.enums.ProcessStatusEnum;
import com.jjg.trade.model.enums.RefundOperateEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wangaf
 * @version v7.0
 * @since v7.0 上午11:20 2018/5/2
 */
public class RefundStep implements Serializable{
    private ProcessStatusEnum status;

    private List<RefundOperateEnum> allowableOperate;


    public RefundStep(ProcessStatusEnum status, RefundOperateEnum... operates ){
        this.status = status;
        this.allowableOperate = new ArrayList<RefundOperateEnum>();
        for (RefundOperateEnum refundOperate : operates) {
            allowableOperate.add(refundOperate);
        }

    }

    /**
     * 检测操作是否在步骤中
     * @param operate
     * @return
     */
    public boolean checkAllowable(RefundOperateEnum operate){
        for (RefundOperateEnum orderOperate : allowableOperate) {
            if(operate.compareTo(orderOperate)==0){
                return true;
            }
        }
        return false;
    }
}
