package com.xdl.jjg.web.service.Impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.context.ApplicationContextHolder;
import com.xdl.jjg.model.domain.EsSmsPlatformDO;
import com.xdl.jjg.model.dto.EsSmsSendDTO;
import com.xdl.jjg.model.vo.EsConfigItemVO;
import com.xdl.jjg.plugin.sms.SmsPlatformManage;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsSmsPlatformService;
import com.xdl.jjg.web.service.IEsSmsService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手机短信实现
 */
@Service
public class EsSmsServiceImpl implements IEsSmsService {

    @Autowired
    private IEsSmsPlatformService platformService;

    private static Logger logger = LoggerFactory.getLogger(EsSmsServiceImpl.class);

    @Override
    public DubboResult send(EsSmsSendDTO sendDTO) {
        try {
            DubboResult<EsSmsPlatformDO> result = platformService.getOpen();
            if (!result.isSuccess() || result.getData() == null) {
                throw new ArgumentException(ErrorCode.GET_OPEN_SMS_ERROR.getErrorCode(), "获取开启的短信网关异常");
            }
            EsSmsPlatformDO smsPlatformDO = result.getData();
            SmsPlatformManage smsPlatformManage = (SmsPlatformManage) ApplicationContextHolder.getBean(smsPlatformDO.getBean());

            logger.info("使用阿里云短信模板开始" + JSONObject.fromObject(sendDTO).toString());

            boolean b = smsPlatformManage.onSend(sendDTO, this.getConfig(smsPlatformDO.getConfig()));
            if (b) {
                logger.info("发送短信请求成功");
            } else {
                logger.info("发送短信请求失败");
                throw new ArgumentException(ErrorCode.SMS_SMS_ERROR.getErrorCode(), "发送短信请求失败");
            }

            logger.info("使用阿里云短信模板结束" + JSONObject.fromObject(sendDTO).toString());
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("发送短信失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("发送短信失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboResult sendLfc(EsSmsSendDTO sendDTO) {
        try {
            DubboResult<EsSmsPlatformDO> result = platformService.getOpen();
            if (!result.isSuccess() || result.getData() == null){
                throw new ArgumentException(ErrorCode.GET_OPEN_SMS_ERROR.getErrorCode(), "获取开启的短信网关异常");
            }
            EsSmsPlatformDO smsPlatformDO = result.getData();
            SmsPlatformManage smsPlatformManage = (SmsPlatformManage) ApplicationContextHolder.getBean(smsPlatformDO.getBean());

            logger.info("使用阿里云短信模板开始"+ JSONObject.fromObject(sendDTO).toString());

            boolean b = smsPlatformManage.onSend(sendDTO, this.getLfcConfig(smsPlatformDO.getConfig()));
            if(b){
                logger.info("发送短信请求成功");
            }else {
                logger.info("发送短信请求失败");
                throw new ArgumentException(ErrorCode.SMS_SMS_ERROR.getErrorCode(), "发送短信请求失败");
            }

            logger.info("使用阿里云短信模板结束"+ JSONObject.fromObject(sendDTO).toString());
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("发送短信失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("发送短信失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 将json参数转换为map格式
     */
    private Map getConfig(String config) {
        if (StringUtil.isEmpty(config)) {
            return new HashMap<>(16);
        }
        Gson gson = new Gson();
        List<EsConfigItemVO> list = gson.fromJson(config, new TypeToken<List<EsConfigItemVO>>() {
        }.getType());
        Map<String, String> result = new HashMap<>(16);
        if (list != null) {
            for (EsConfigItemVO item : list) {
                result.put(item.getName(), StringUtil.toString(item.getValue()));
            }
        }
        return result;
    }


    /**
     * 将json参数转换为map格式
     */
    private Map getLfcConfig(String config) {
        if (StringUtil.isEmpty(config)) {
            return new HashMap<>(16);
        }
        Gson gson = new Gson();
        List<EsConfigItemVO> list = gson.fromJson(config, new TypeToken<List<EsConfigItemVO>>() {
        }.getType());
        Map<String, String> result = new HashMap<>(16);
        if (list != null) {
            for (EsConfigItemVO item : list) {
                result.put(item.getName(), StringUtil.toString(item.getValue()));
                if (item.getName().equals("signName")) {
                    result.put(item.getName(), "世纪联华");
                }
            }
        }
        return result;
    }
}
