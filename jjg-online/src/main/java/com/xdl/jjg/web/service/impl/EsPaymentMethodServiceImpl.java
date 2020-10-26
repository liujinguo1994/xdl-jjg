package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjg.trade.model.domain.EsPaymentMethodDO;
import com.jjg.trade.model.domain.PaymentPluginDO;
import com.jjg.trade.model.dto.EsPaymentMethodDTO;
import com.jjg.trade.model.enums.ClientType;
import com.jjg.trade.model.vo.ClientConfigVO;
import com.jjg.trade.model.vo.PayConfigItemVO;
import com.jjg.trade.model.vo.PaymentPluginVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsPaymentMethod;
import com.xdl.jjg.mapper.EsPaymentMethodMapper;
import com.xdl.jjg.plugin.PaymentPluginManager;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsPaymentMethodService;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsPaymentMethodService.class, timeout = 50000)
public class EsPaymentMethodServiceImpl extends ServiceImpl<EsPaymentMethodMapper, EsPaymentMethod> implements IEsPaymentMethodService {

    private static Logger logger = LoggerFactory.getLogger(EsPaymentMethodServiceImpl.class);

    @Autowired
    private EsPaymentMethodMapper paymentMethodMapper;

    @Autowired
    private List<PaymentPluginManager> paymentPluginManagerList;

    /**
     * 根据条件更新数据
     *
     * @param paymentMethod     插件方法
     * @param paymentPluginId   插件id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentMethodDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updatePaymentMethod(EsPaymentMethodDTO paymentMethod, String paymentPluginId) {
        try {
            // 删除数据库里的字段
           this.paymentMethodMapper.delete(new QueryWrapper<EsPaymentMethod>().lambda().eq(EsPaymentMethod::getPluginId, paymentPluginId));

            PaymentPluginManager paymentPlugin = findPlugin(paymentPluginId);
            if (paymentPlugin == null) {
                return DubboResult.fail(TradeErrorCode.PAYMENT_METHOD_NOT_EXIST.getErrorCode(),
                        TradeErrorCode.PAYMENT_METHOD_NOT_EXIST.getErrorMsg());
            }
            paymentMethod.setMethodName(paymentPlugin.getPluginName());
            paymentMethod.setPluginId(paymentPluginId);

            // 配置信息
            List<ClientConfigVO> clients = paymentMethod.getEnableClient();
            Map<String, ClientConfigVO> map = new HashMap<>(16);
            for (ClientConfigVO client : clients) {

                String keyStr = client.getKey();
                keyStr = keyStr.replace("amp;","");
                // 区分客户端 pc_config&wap_config
                String[] keys = keyStr.split("&");

                for (String key : keys) {
                    map.put(key, client);
                }
            }

            //为了防止传值格式错误，以自定义格式为准
            List<ClientConfigVO> needClients = paymentPlugin.definitionClientConfig();
            Map<String, Object> jsonMap = new HashMap<>(16);
            for (ClientConfigVO clientConfig : needClients) {
                String keyStr = clientConfig.getKey();
                // 区分客户端 pc_config&wap_config
                String[] keys = keyStr.split("&");
                for (String key : keys) {
                    // 传值来的对象
                    ClientConfigVO client = map.get(key);
                    if(client == null){
                        return DubboResult.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(),
                                "缺少"+clientConfig.getName()+"相关配置");
                    }
                    //未开启
                    Integer open = client.getIsOpen();
                    clientConfig.setIsOpen(client.getIsOpen());
                    if(open==0){
                        //未开启则不保存配置参数
                        jsonMap.put(StringUtil.lowerUpperCaseColumn(key), JsonUtil.objectToJson(clientConfig));
                        continue;
                    }
                    //传值来的配置参数
                    List<PayConfigItemVO> list = client.getConfigList();
                    if(list == null){
                        return DubboResult.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(),
                                clientConfig.getName()+"的配置不能为空");
                    }
                    // 循环成key value 格式
                    Map<String, String> valueMap = new HashMap<>(list.size());
                    for (PayConfigItemVO item : list) {
                        valueMap.put(item.getName(), item.getValue());
                    }
                    // 配置参数设置
                    List<PayConfigItemVO> configList = clientConfig.getConfigList();
                    for (PayConfigItemVO item : configList) {
                        String value = valueMap.get(item.getName());
                        if (StringUtil.isEmpty(value)) {
                            return DubboResult.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(),
                                    clientConfig.getName()+"的"+item.getText()+"必填");
                        }
                        item.setValue(value);
                    }
                    clientConfig.setConfigList(configList);

                    jsonMap.put(StringUtil.lowerUpperCaseColumn(key), JsonUtil.objectToJson(clientConfig));
                }
            }

            EsPaymentMethod payment = new EsPaymentMethod();
            //复制配置信息
            BeanUtil.copyPropertiesInclude(jsonMap, payment);
            BeanUtil.copyProperties(paymentMethod, payment);
            this.paymentMethodMapper.insert(payment);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据支付插件ID获取详情
     *
     * @param pluginId 支付插件ID
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<PaymentPluginVO>
     */
    @Override
    public DubboResult getPaymentMethod(String pluginId) {
        try {
            QueryWrapper<EsPaymentMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPaymentMethod::getPluginId, pluginId);
            EsPaymentMethod paymentMethod = this.paymentMethodMapper.selectOne(queryWrapper);

            if (paymentMethod == null) {

                PaymentPluginManager plugin = findPlugin(pluginId);
                if (plugin == null) {
                    return DubboResult.fail(TradeErrorCode.PAYMENT_METHOD_NOT_EXIST.getErrorCode(),
                            TradeErrorCode.PAYMENT_METHOD_NOT_EXIST.getErrorMsg());
                }
                PaymentPluginDO payment = new PaymentPluginDO(plugin.getPluginName(), plugin.getPluginId(), plugin.getIsRetrace());
                payment.setEnableClient(plugin.definitionClientConfig());
                return DubboResult.success(payment);
            } else {

                Map<String, ClientConfigVO> map = new HashMap<>(16);

                String pcConfig = paymentMethod.getPcConfig();
                if (pcConfig != null) {
                    ClientConfigVO config = JsonUtil.jsonToObject(pcConfig, ClientConfigVO.class);
                    map.put(config.getKey(), config);
                }

                String wapConfig = paymentMethod.getWapConfig();
                if (wapConfig != null) {
                    ClientConfigVO config = JsonUtil.jsonToObject(wapConfig, ClientConfigVO.class);
                    map.put(config.getKey(), config);
                }

                String appReactConfig = paymentMethod.getAppReactConfig();
                if (appReactConfig != null) {
                    ClientConfigVO config = JsonUtil.jsonToObject(appReactConfig, ClientConfigVO.class);
                    map.put(config.getKey(), config);
                }

                String appNativeConfig = paymentMethod.getAppNativeConfig();
                if (appNativeConfig != null) {
                    ClientConfigVO config = JsonUtil.jsonToObject(appNativeConfig, ClientConfigVO.class);
                    map.put(config.getKey(), config);
                }

                String appletConfig = paymentMethod.getAppletConfig();
                if (appletConfig != null){
                    ClientConfigVO config = JsonUtil.jsonToObject(appletConfig, ClientConfigVO.class);
                    map.put(config.getKey(),config);
                }

                PaymentPluginDO pluginDO = new PaymentPluginDO();
                BeanUtil.copyProperties(paymentMethod, pluginDO);
                // 配置信息
                List<ClientConfigVO> clientConfigs = new ArrayList<>();
                for (String key : map.keySet()) {
                    clientConfigs.add(map.get(key));
                }
                pluginDO.setEnableClient(clientConfigs);
                return DubboResult.success(pluginDO);
            }
        } catch(Throwable th){
                logger.error("查询失败", th);
                return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据客户端类型获取数据
     *
     * @param clientType 客户端类型
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentMethodDO>
     */
    @Override
    public DubboPageResult getPaymentMethodListByClient(String clientType) {
        try {

            QueryWrapper<EsPaymentMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().like(StringUtil.equals(clientType, ClientType.NATIVE.getClient()),
                    EsPaymentMethod::getAppNativeConfig, "%\"isOpen\":1%");
            queryWrapper.lambda().like(StringUtil.equals(clientType, ClientType.PC.getClient()),
                    EsPaymentMethod::getPcConfig, "%\"isOpen\":1%");
            queryWrapper.lambda().like(StringUtil.equals(clientType, ClientType.WAP.getClient()),
                    EsPaymentMethod::getWapConfig, "%\"isOpen\":1%");
            List<EsPaymentMethod> paymentMethodList = this.paymentMethodMapper.selectList(queryWrapper);

            List<EsPaymentMethodDO> esPaymentMethodDOS = BeanUtil.copyList(paymentMethodList, EsPaymentMethodDO.class);
            return DubboPageResult.success(esPaymentMethodDOS);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param paymentMethodDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsPaymentMethodDO>
     */
    @Override
    public DubboPageResult getPaymentMethodList(EsPaymentMethodDTO paymentMethodDTO, int pageSize, int pageNum) {
        try {
            // 查询条件
            List<PaymentPluginVO> resultList = new ArrayList<>();

            // 查询数据库中的支付方式
            List<EsPaymentMethod> list = this.paymentMethodMapper.selectList(null);
            List<EsPaymentMethodDO> paymentPluginList = BeanUtil.copyList(list, EsPaymentMethodDO.class);
            Map<String, EsPaymentMethodDO> map = new HashMap<>(list.size());

            for (EsPaymentMethodDO payment : paymentPluginList) {
                map.put(payment.getPluginId(), payment);
            }

            for (PaymentPluginManager plugin : paymentPluginManagerList) {
                EsPaymentMethodDO payment = map.get(plugin.getPluginId());
                PaymentPluginVO result = null;

                //数据库中已经有支付方式
                if (payment != null) {
                    result = new PaymentPluginVO(payment);
                } else {
                    result = new PaymentPluginVO(plugin.getPluginName(), plugin.getPluginId(), plugin.getIsRetrace());
                }

                resultList.add(result);
            }
            Long size = (long) resultList.size();
            return DubboPageResult.success(size, resultList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsPaymentMethodDO> getByPluginId(String pluginId) {
        try {
            QueryWrapper<EsPaymentMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPaymentMethod::getPluginId, pluginId);
            EsPaymentMethod paymentMethod = this.paymentMethodMapper.selectOne(queryWrapper);
            EsPaymentMethodDO paymentMethodDO = new EsPaymentMethodDO();
            if (paymentMethod == null) {
                throw new ArgumentException(TradeErrorCode.GET_PAY_METHOD_ERROR.getErrorCode(), TradeErrorCode.GET_PAY_METHOD_ERROR.getErrorMsg());
            }
            BeanUtil.copyProperties(paymentMethod, paymentMethodDO);
            return DubboResult.success(paymentMethodDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 查找支付插件
     * @param pluginId
     * @return
     */
    private PaymentPluginManager findPlugin(String pluginId){
        for (PaymentPluginManager plugin : paymentPluginManagerList) {
            if(plugin.getPluginId().equals( pluginId)){
                return plugin;
            }
        }
        return null;
    }
}
