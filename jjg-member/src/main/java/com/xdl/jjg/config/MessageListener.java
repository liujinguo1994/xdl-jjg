package com.xdl.jjg.config;


import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.model.dto.EsMemberNoticeLogDTO;
import com.xdl.jjg.model.dto.EsMessageDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsMemberNoticeLogService;
import com.xdl.jjg.web.service.IEsMemberService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * mq 更新会员活跃信息
 * Description: zhuox-shop-member
 * <p>
 * Created by Administrator on 2019/7/17 10:23
 */
public class MessageListener implements MessageListenerConcurrently {


    @Autowired
    IEsMemberService iEsMemberService;
    @Autowired
    IEsMemberNoticeLogService iEsMemberNoticeLogService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(list)){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        if (CollectionUtils.isEmpty(list)) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt message = list.get(0);
        if (message.getBody() == null || message.getBody().length == 0) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        String body = new String(message.getBody());
        EsMessageDTO esMessageDTO = JsonUtil.jsonToObject(body, EsMessageDTO.class);
        if (null != esMessageDTO && null != esMessageDTO.getSendType()) {
            if (esMessageDTO.getSendType() == MemberConstant.msgSysType) {
                DubboPageResult<EsMemberDO> esMemberList = iEsMemberService.getMemberListNoPage();
                if (esMemberList.isSuccess()) {
                    if (CollectionUtils.isNotEmpty(esMemberList.getData().getList())) {
                        esMemberList.getData().getList().stream().forEach(esMemberSysDO -> {
                            EsMemberNoticeLogDTO esMemberNoticeLogDTO = new EsMemberNoticeLogDTO();
                            esMemberNoticeLogDTO.setMemberId(esMemberSysDO.getId());
                            esMemberNoticeLogDTO.setContent(esMessageDTO.getContent());
                            esMemberNoticeLogDTO.setMsgType(MemberConstant.msgSysType);
                            esMemberNoticeLogDTO.setTitle(esMessageDTO.getTitle());
                            esMemberNoticeLogDTO.setSendTime(System.currentTimeMillis());
                            esMemberNoticeLogDTO.setIsDel(MemberConstant.IsDefault);
                            esMemberNoticeLogDTO.setIsRead(MemberConstant.IsDefault);
                            iEsMemberNoticeLogService.insertMemberNoticeLog(esMemberNoticeLogDTO);
                        });
                    }
                }
            }
            if (esMessageDTO.getSendType() == MemberConstant.msgShopType) {
                String[] ids = esMessageDTO.getMemberIds().split(",");
                for (String id : ids) {
                    DubboResult<EsMemberDO> esMemberDoInfo = iEsMemberService.getMemberByIdInfo(Long.valueOf(id));
                    if (esMemberDoInfo.isSuccess() && null != esMemberDoInfo.getData()) {
                        EsMemberNoticeLogDTO esMemberNoticeLogDTO = new EsMemberNoticeLogDTO();
                        esMemberNoticeLogDTO.setMemberId(Long.valueOf(id));
                        esMemberNoticeLogDTO.setContent(esMessageDTO.getContent());
                        esMemberNoticeLogDTO.setMsgType(MemberConstant.msgShopType);
                        esMemberNoticeLogDTO.setTitle(esMessageDTO.getTitle());
                        esMemberNoticeLogDTO.setSendTime(System.currentTimeMillis());
                        esMemberNoticeLogDTO.setIsDel(MemberConstant.IsDefault);
                        esMemberNoticeLogDTO.setIsRead(MemberConstant.IsDefault);
                        iEsMemberNoticeLogService.insertMemberNoticeLog(esMemberNoticeLogDTO);
                    }

                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }
}
