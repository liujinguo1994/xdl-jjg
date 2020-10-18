package com.xdl.jjg.web.service.Impl;


import com.jjg.system.model.domain.TaskProgress;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsProgressManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsProgressManagerServiceImpl implements IEsProgressManagerService {

    private static Logger logger = LoggerFactory.getLogger(EsProgressManagerServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取进度信息
     */
    @Override
    public DubboResult<TaskProgress> getProgress(String id) {
        id = TaskProgress.PROCESS + id;
        String s = (String) redisTemplate.opsForValue().get(id);
        TaskProgress taskProgress = null;
        if (!StringUtil.isEmpty(s)) {
            taskProgress = JsonUtil.jsonToObject(s, TaskProgress.class);
        }
        return DubboResult.success(taskProgress);
    }

    /**
     * 写入进度
     */
    @Override
    public DubboResult putProgress(String id, TaskProgress progress) {
        id = TaskProgress.PROCESS + id;
        progress.setId(id);
        String s = JsonUtil.objectToJson(progress);
        redisTemplate.opsForValue().set(id, s);
        return DubboResult.success();
    }

    /**
     * 移除任务
     */
    @Override
    public DubboResult remove(String id) {
        id = TaskProgress.PROCESS + id;
        redisTemplate.delete(id);
        return DubboResult.success();
    }
}
