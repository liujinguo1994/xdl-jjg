package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsSmsPlatform;
import com.xdl.jjg.mapper.EsSmsPlatformMapper;
import com.xdl.jjg.model.domain.EsSmsPlatformDO;
import com.xdl.jjg.model.vo.EsConfigItemVO;
import com.xdl.jjg.model.vo.EsSmsPlatformVO;
import com.xdl.jjg.plugin.sms.SmsPlatformManage;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsSmsPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类-短信网关设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsSmsPlatformServiceImpl extends ServiceImpl<EsSmsPlatformMapper, EsSmsPlatform> implements IEsSmsPlatformService {

    private static Logger logger = LoggerFactory.getLogger(EsSmsPlatformServiceImpl.class);

    @Autowired
    private EsSmsPlatformMapper smsPlatformMapper;

    @Autowired
    private List<SmsPlatformManage> smsPlatforms;


    /**
     * 根据条件更新数据
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSmsPlatform(EsSmsPlatformVO esSmsPlatformVO) {
        try {
            List<EsSmsPlatformVO> resultList = this.getPlatform();
            for (EsSmsPlatformVO vo : resultList) {
                this.add(vo);
            }
            EsSmsPlatform smsPlateform = this.getSmsPlateform(esSmsPlatformVO.getBean());
            if (smsPlateform == null) {
                throw new ArgumentException(ErrorCode.SMS_PLATEFORM_NOT_EXIT.getErrorCode(), "该短信方案不存在");
            }
            esSmsPlatformVO.setId(smsPlateform.getId());
            this.smsPlatformMapper.updateById(new EsSmsPlatform(esSmsPlatformVO));
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //查询短信平台列表
    @Override
    public DubboPageResult<EsSmsPlatformVO> getSmsPlatformList(int pageSize, int pageNum) {
        List<EsSmsPlatformVO> resultList = this.getPlatform();
        for (EsSmsPlatformVO vo : resultList) {
            this.add(vo);
        }
        Long size = (long) resultList.size();
        return DubboPageResult.success(size,resultList);
    }

    //开启短信网关
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult openPlatform(String bean) {
        try {
            List<EsSmsPlatformVO> resultList = this.getPlatform();
            for (EsSmsPlatformVO vo : resultList) {
                this.add(vo);
            }
            EsSmsPlatform smsPlateform = this.getSmsPlateform(bean);
            if (smsPlateform == null) {
                throw new ArgumentException(ErrorCode.SMS_PLATEFORM_NOT_EXIT.getErrorCode(), "该短信方案不存在");
            }
            EsSmsPlatform esSmsPlatform = new EsSmsPlatform();
            esSmsPlatform.setOpen(0);
            QueryWrapper<EsSmsPlatform> queryWrapper = new QueryWrapper<>();
            smsPlatformMapper.update(esSmsPlatform,queryWrapper);
            esSmsPlatform.setOpen(1);
            esSmsPlatform.setId(smsPlateform.getId());
            smsPlatformMapper.updateById(esSmsPlatform);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("开启短信网关失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("开启短信网关失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //获取开启的短信网关
    @Override
    public DubboResult<EsSmsPlatformDO> getOpen() {
        try {
            QueryWrapper<EsSmsPlatform> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSmsPlatform::getOpen,1);
            EsSmsPlatform esSmsPlatform = smsPlatformMapper.selectOne(queryWrapper);
            EsSmsPlatformDO esSmsPlatformDO = new EsSmsPlatformDO();
            BeanUtil.copyProperties(esSmsPlatform,esSmsPlatformDO);
            return DubboResult.success(esSmsPlatformDO);
        } catch (ArgumentException ae){
            logger.error("获取开启的短信网关失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取开启的短信网关失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 添加短信网关
     *
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult add(EsSmsPlatformVO esSmsPlatformVO) {
        try {
            EsSmsPlatform esSmsPlatform = new EsSmsPlatform(esSmsPlatformVO);
            if (esSmsPlatform.getId() == null || esSmsPlatform.getId() == 0) {
                EsSmsPlatform smsPlateform = getSmsPlateform(esSmsPlatform.getBean());
                if (smsPlateform != null) {
                    throw new ArgumentException(ErrorCode.SMS_PLATEFORM_EXIT.getErrorCode(), "该短信方案已经存在");
                }
                smsPlatformMapper.insert(esSmsPlatform);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("添加短信网关失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("添加短信网关失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据bean获取短信方案
    public EsSmsPlatform getSmsPlateform(String bean) {
        QueryWrapper<EsSmsPlatform> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsSmsPlatform::getBean,bean);
        EsSmsPlatform esSmsPlatform = smsPlatformMapper.selectOne(queryWrapper);
        return esSmsPlatform;
    }

    /**
     * 获取所有的短信信方案
     */
    private List<EsSmsPlatformVO> getPlatform() {
        List<EsSmsPlatformVO> resultList = new ArrayList<>();

        QueryWrapper<EsSmsPlatform> queryWrapper = new QueryWrapper();
        List<EsSmsPlatform> list = smsPlatformMapper.selectList(queryWrapper);

        Map<String, EsSmsPlatform> map = new HashMap<>(16);

        for (EsSmsPlatform esSmsPlatform : list) {
            map.put(esSmsPlatform.getBean(), esSmsPlatform);
        }
        for (SmsPlatformManage plugin : smsPlatforms) {
            EsSmsPlatform esSmsPlatform = map.get(plugin.getPluginId());
            EsSmsPlatformVO result = null;

            if (esSmsPlatform != null) {
                result = getEsSmsPlatformVO(esSmsPlatform);
            } else {
                result = getPlatformVO(plugin);
            }
            resultList.add(result);
        }
        return resultList;
    }

    //给VO设置值
    public EsSmsPlatformVO getEsSmsPlatformVO(EsSmsPlatform esSmsPlatform) {
        EsSmsPlatformVO esSmsPlatformVO = new EsSmsPlatformVO();
        esSmsPlatformVO.setId(esSmsPlatform.getId());
        esSmsPlatformVO.setName(esSmsPlatform.getName());
        esSmsPlatformVO.setOpen(esSmsPlatform.getOpen());
        esSmsPlatformVO.setBean(esSmsPlatform.getBean());
        Gson gson = new Gson();
        esSmsPlatformVO.setConfigItems(gson.fromJson(esSmsPlatform.getConfig(),  new TypeToken< List<EsConfigItemVO> >() {  }.getType()));
        return esSmsPlatformVO;
    }

    //给VO设置值
    public EsSmsPlatformVO getPlatformVO(SmsPlatformManage plugin) {
        EsSmsPlatformVO vo = new EsSmsPlatformVO();
        //vo.setId(new Long((long)0));
        vo.setName(plugin.getPluginName());
        vo.setOpen(plugin.getIsOpen());
        vo.setBean(plugin.getPluginId());
        vo.setConfigItems(plugin.definitionConfigItem());
        return vo;
    }
}
