package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.TaskProgress;
import com.xdl.jjg.model.enums.ProgressEnum;
import com.xdl.jjg.model.vo.EsProgressVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.service.IEsProgressManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>
 * 前端控制器-进度管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esProgressManager")
@Api(value = "/esProgressManager", tags = "进度管理")
public class EsProgressManagerController {

    @Autowired
    private IEsProgressManagerService progressManager;

    @ApiOperation("检测是否有任务正在进行,有任务返回任务id")
    @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "String", paramType = "path", required = true)
    @GetMapping(value = "/hasTask/{taskId}")
    @ResponseBody
    public ApiResponse hasTask(@PathVariable("taskId") String taskId) {

        DubboResult<TaskProgress> result = progressManager.getProgress(taskId);
        if (result.isSuccess()) {
            TaskProgress progress = result.getData();
            if (progress != null && Objects.equals(progress.getTaskStatus(), "DOING")) {
                return ApiResponse.success(taskId);
            }
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查看任务进度", response = EsProgressVO.class)
    @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "String", paramType = "path", required = true)
    @GetMapping(value = "/viewProgress/{taskId}")
    @ResponseBody
    public ApiResponse viewProgress(@PathVariable("taskId") String taskId) {

        DubboResult<TaskProgress> result = progressManager.getProgress(taskId);
        if (result.isSuccess()) {
            TaskProgress taskProgress = result.getData();
            EsProgressVO esProgressVO = null;
            if (taskProgress == null) {
                esProgressVO = new EsProgressVO(100, ProgressEnum.SUCCESS.name());
                return ApiResponse.success(esProgressVO);
            } else {
                esProgressVO = new EsProgressVO(taskProgress);
            }
            //如果是完成或者出错 需要移除任务
            if (!taskProgress.getTaskStatus().equals(ProgressEnum.DOING.name())) {
                progressManager.remove(taskId);
            }
            return ApiResponse.success(esProgressVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation("清除某任务")
    @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "String", paramType = "path", required = true)
    @DeleteMapping(value = "/clear/{taskId}")
    @ResponseBody
    public ApiResponse clear(@PathVariable("taskId") String taskId) {
        DubboResult result = progressManager.remove(taskId);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
