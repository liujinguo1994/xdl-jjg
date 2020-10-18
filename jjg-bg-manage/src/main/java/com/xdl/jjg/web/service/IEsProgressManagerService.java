package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.TaskProgress;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 进度管理接口
 */
public interface IEsProgressManagerService {
    /**
     * 获取进度信息
     */
    DubboResult<TaskProgress> getProgress(String id);

    /**
     * 写入进度
     */
    DubboResult putProgress(String id, TaskProgress progress);

    /**
     * 移除任务
     */
    DubboResult remove(String id);


}
