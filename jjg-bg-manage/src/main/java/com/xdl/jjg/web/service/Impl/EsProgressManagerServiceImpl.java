package com.xdl.jjg.web.service.Impl;

import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.system.api.model.domain.TaskProgress;
import com.shopx.system.api.service.IEsProgressManagerService;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsProgressManagerService.class, timeout = 50000)
public class EsProgressManagerServiceImpl implements IEsProgressManagerService {

    private static Logger logger = LoggerFactory.getLogger(EsProgressManagerServiceImpl.class);

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 获取进度信息
     */
    @Override
    public DubboResult<TaskProgress> getProgress(String id) {
        id = TaskProgress.PROCESS + id;
        String s = jedisCluster.get(id);
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
        jedisCluster.set(id, s);
        return DubboResult.success();
    }

    /**
     * 移除任务
     */
    @Override
    public DubboResult remove(String id) {
        id = TaskProgress.PROCESS + id;
        jedisCluster.del(id);
        return DubboResult.success();
    }
}
