package com.xdl.jjg.web.controller;


import com.jjg.system.model.domain.TaskProgress;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TaskProgressConstant;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.web.service.IEsProgressManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * 前端控制器-商品索引
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-24 15:46:00
 */
@Api(value = "/esGoodsIndex", tags = "商品索引")
@RestController
@RequestMapping("/esGoodsIndex")
public class EsGoodsIndexController {

    @Autowired
    private IEsProgressManagerService progressManager;

    private MQProducer mqProducer;

    @Value("${rocketmq.goods.index.topic}")
    private String goods_index_topic;

    @ApiOperation(value = "商品索引初始化")
    @GetMapping(value = "/create")
    @ResponseBody
    public ApiResponse create() {
        DubboResult<TaskProgress> result = progressManager.getProgress(TaskProgressConstant.GOODS_INDEX);
        if (result.isSuccess()) {
            TaskProgress taskProgress = result.getData();
            if (taskProgress != null && Objects.equals(taskProgress.getTaskStatus(), "DOING")) {
                return ApiResponse.fail(1001, "有索引任务正在进行中，需等待本次任务完成后才能再次生成。");
            }
            //发送生成商品索引的mq消息
            try {
                mqProducer.send(goods_index_topic, "发送生成商品索引的mq消息");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResponse.fail(1002, "发送生成商品索引的mq消息失败");
            }
            return ApiResponse.success(TaskProgressConstant.GOODS_INDEX);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
