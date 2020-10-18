package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsSettingsDO;
import com.jjg.system.model.dto.*;
import com.jjg.system.model.enums.SettingGroup;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsSettings;
import com.xdl.jjg.mapper.EsSettingsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsSettingsServiceImpl extends ServiceImpl<EsSettingsMapper, EsSettings> implements IEsSettingsService {

    private static Logger logger = LoggerFactory.getLogger(EsSettingsServiceImpl.class);

    @Autowired
    private EsSettingsMapper settingsMapper;

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSettingsDO>
     */
    @Override
    public DubboResult<EsSettingsDO> getSettings(Long id) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getId, id);
            EsSettings settings = this.settingsMapper.selectOne(queryWrapper);
            EsSettingsDO settingsDO = new EsSettingsDO();
            if (settings == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(settings, settingsDO);
            return DubboResult.success(settingsDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    @Override
    public DubboResult<EsSettingsDO> getByCfgGroup(String cfgGroup) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getCfgGroup, cfgGroup);
            EsSettings settings = this.settingsMapper.selectOne(queryWrapper);
            EsSettingsDO settingsDO = new EsSettingsDO();
            if (settings != null) {
                BeanUtil.copyProperties(settings, settingsDO);
            }
            return DubboResult.success(settingsDO);
        } catch (ArgumentException ae) {
            logger.error("根据业务标识查系统配置信息失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("根据业务标识查系统配置信息失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSecuritySetting(SettingGroup group, EsSecuritySettingsDTO esSecuritySettingsDTO) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getCfgGroup, group.name());
            EsSettings esSettings = settingsMapper.selectOne(queryWrapper);
            //将要保存的对象 转换成json
            String json = JsonUtil.objectToJson(esSecuritySettingsDTO);
            if (esSettings == null) {
                EsSettings settings = new EsSettings();
                settings.setCfgGroup(group.name());
                settings.setCfgValue(json);
                settingsMapper.insert(settings);
            } else {
                EsSettings settings = new EsSettings();
                settings.setId(esSettings.getId());
                settings.setCfgValue(json);
                settingsMapper.updateById(settings);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改安全设置失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改安全设置失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult saveOrderSetting(SettingGroup group, EsOrderSettingDTO esOrderSettingDTO) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getCfgGroup, group.name());
            EsSettings esSettings = settingsMapper.selectOne(queryWrapper);
            //将要保存的对象 转换成json
            String json = JsonUtil.objectToJson(esOrderSettingDTO);
            if (esSettings == null) {
                EsSettings settings = new EsSettings();
                settings.setCfgGroup(group.name());
                settings.setCfgValue(json);
                settingsMapper.insert(settings);
            } else {
                EsSettings settings = new EsSettings();
                settings.setId(esSettings.getId());
                settings.setCfgValue(json);
                settingsMapper.updateById(settings);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("保存订单设置失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("保存订单设置失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //静态页地址参数保存
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult saveStaticPageAddress(SettingGroup group, EsPageSettingDTO esPageSettingDTO) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getCfgGroup, group.name());
            EsSettings esSettings = settingsMapper.selectOne(queryWrapper);
            //将要保存的对象 转换成json
            String json = JsonUtil.objectToJson(esPageSettingDTO);
            if (esSettings == null) {
                EsSettings settings = new EsSettings();
                settings.setCfgGroup(group.name());
                settings.setCfgValue(json);
                settingsMapper.insert(settings);
            } else {
                EsSettings settings = new EsSettings();
                settings.setId(esSettings.getId());
                settings.setCfgValue(json);
                settingsMapper.updateById(settings);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("静态页地址参数保存失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("静态页地址参数保存失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //保存结算周期设置
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult saveClearingCycle(SettingGroup group, EsClearingCycleSettingsDTO clearingCycleSettingsDTO) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getCfgGroup, group.name());
            EsSettings esSettings = settingsMapper.selectOne(queryWrapper);
            //将要保存的对象 转换成json
            String json = JsonUtil.objectToJson(clearingCycleSettingsDTO.getClearingCycleSettings());
            if (esSettings == null) {
                EsSettings settings = new EsSettings();
                settings.setCfgGroup(group.name());
                settings.setCfgValue(json);
                settingsMapper.insert(settings);
            } else {
                EsSettings settings = new EsSettings();
                settings.setId(esSettings.getId());
                settings.setCfgValue(json);
                settingsMapper.updateById(settings);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("保存结算周期设置失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("保存结算周期设置失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //保存站点设置
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult saveSite(SettingGroup group, EsSiteDTO dto) {
        try {
            QueryWrapper<EsSettings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSettings::getCfgGroup, group.name());
            EsSettings esSettings = settingsMapper.selectOne(queryWrapper);
            //将要保存的对象 转换成json
            String json = JsonUtil.objectToJson(dto);
            if (esSettings == null) {
                EsSettings settings = new EsSettings();
                settings.setCfgGroup(group.name());
                settings.setCfgValue(json);
                settingsMapper.insert(settings);
            } else {
                EsSettings settings = new EsSettings();
                settings.setId(esSettings.getId());
                settings.setCfgValue(json);
                settingsMapper.updateById(settings);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("保存站点设置失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("保存站点设置失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
