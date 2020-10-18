package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsMessageDO;
import com.jjg.system.model.dto.EsMessageDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsMessage;
import com.xdl.jjg.mapper.EsMessageMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMessageServiceImpl extends ServiceImpl<EsMessageMapper, EsMessage> implements IEsMessageService {

    private static Logger logger = LoggerFactory.getLogger(EsMessageServiceImpl.class);

    @Autowired
    private EsMessageMapper messageMapper;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.message.topic}")
    private String message_topic;

    /**
     * 插入数据
     *
     * @param messageDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMessage(EsMessageDTO messageDTO) {
        try {
            if (messageDTO.getSendType().equals(1)) {
                if (StringUtil.isEmpty(messageDTO.getMemberIds())) {
                    throw new ArgumentException(ErrorCode.NOT_ASSIGN_MEMBER.getErrorCode(), "请指定发送会员");
                }
            }
            EsMessage message = new EsMessage();
            BeanUtil.copyProperties(messageDTO, message);
            messageMapper.insert(message);
            //发送mq消息
            mqProducer.send(message_topic, JsonUtil.objectToJson(message));

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param messageDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMessage(EsMessageDTO messageDTO) {
        try {
            if (messageDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMessage message = new EsMessage();
            BeanUtil.copyProperties(messageDTO, message);
            QueryWrapper<EsMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMessage::getId, messageDTO.getId());
            this.messageMapper.update(message, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     */
    @Override
    public DubboResult<EsMessageDO> getMessage(Long id) {
        try {
            QueryWrapper<EsMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMessage::getId, id);
            EsMessage message = this.messageMapper.selectOne(queryWrapper);
            EsMessageDO messageDO = new EsMessageDO();
            if (message == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(message, messageDO);
            return DubboResult.success(messageDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param messageDTO DTO
     * @param pageSize   页码
     * @param pageNum    页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsMessageDO>
     */
    @Override
    public DubboPageResult<EsMessageDO> getMessageList(EsMessageDTO messageDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMessage> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsMessage> page = new Page<>(pageNum, pageSize);
            IPage<EsMessage> iPage = this.page(page, queryWrapper);
            List<EsMessageDO> messageDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                messageDOList = iPage.getRecords().stream().map(message -> {
                    EsMessageDO messageDO = new EsMessageDO();
                    BeanUtil.copyProperties(message, messageDO);
                    return messageDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), messageDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMessage(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMessage> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMessage::getId, id);
            this.messageMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
