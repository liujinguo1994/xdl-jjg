package com.xdl.jjg.web.service.Impl;


import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.MQProducer;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.constant.TaskProgressConstant;
import com.xdl.jjg.model.domain.TaskProgress;
import com.xdl.jjg.model.enums.ProgressEnum;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsPageCreateManagerService;
import com.xdl.jjg.web.service.IEsProgressManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsPageCreateManagerServiceImpl implements IEsPageCreateManagerService {

    private static Logger logger = LoggerFactory.getLogger(EsPageCreateManagerServiceImpl.class);

    @Autowired
    private IEsProgressManagerService progressManager;

    @Autowired
    private MQProducer mqProducer;
    @Value("${rocketmq.static.page.topic}")
    private String static_page_topic;

    /**
     * 生成静态页
     */
    @Override
    public DubboResult<Boolean> create(String[] choosePages) {
        try {
            DubboResult<TaskProgress> result = progressManager.getProgress(TaskProgressConstant.PAGE_CREATE);
            if (result.isSuccess()){
                TaskProgress taskProgress = result.getData();
               if (taskProgress != null){
                   //判断是否有正在进行中的任务
                   if(taskProgress.getTaskStatus().equals(ProgressEnum.DOING.getStatus())){
                       return DubboResult.success(false);
                   }
               }
               //发送生成静态页的mq消息
                String s = JsonUtil.objectToJson(choosePages);
                mqProducer.send(static_page_topic, s);
            }else {
                throw new ArgumentException(ErrorCode.GET_PROGRESS_ERROR.getErrorCode(), "获取进度信息异常");
            }
            return DubboResult.success(true);
        } catch (ArgumentException ae){
            logger.error("生成静态页失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("生成静态页失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
