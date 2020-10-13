package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsExpressPlatform;
import com.xdl.jjg.mapper.EsExpressPlatformMapper;
import com.xdl.jjg.model.domain.EsExpressPlatformDO;
import com.xdl.jjg.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.model.dto.EsExpressPlatformDTO;
import com.xdl.jjg.model.enums.CachePrefix;
import com.xdl.jjg.model.form.ExpressPlatform;
import com.xdl.jjg.model.vo.EsConfigItemVO;
import com.xdl.jjg.model.vo.EsExpressPlatformVO;
import com.xdl.jjg.model.vo.ExpressDetailVO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsExpressPlatformService;
import com.xdl.jjg.web.service.IEsLogiCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsExpressPlatformServiceImpl extends ServiceImpl<EsExpressPlatformMapper, EsExpressPlatform> implements IEsExpressPlatformService {

    private static Logger logger = LoggerFactory.getLogger(EsExpressPlatformServiceImpl.class);

    @Autowired
    private EsExpressPlatformMapper expressPlatformMapper;

    @Autowired
    private List<ExpressPlatform> expressPlatforms;
    @Autowired
    private IEsLogiCompanyService logiCompanyService;
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 插入数据
     *
     * @param expressPlatformDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertExpressPlatform(EsExpressPlatformDTO expressPlatformDTO) {
        try {
            if (expressPlatformDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsExpressPlatform expressPlatform = new EsExpressPlatform();
            BeanUtil.copyProperties(expressPlatformDTO, expressPlatform);
            this.expressPlatformMapper.insert(expressPlatform);
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
     * @param expressPlatformDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateExpressPlatform(EsExpressPlatformDTO expressPlatformDTO) {
//       List<EsExpressPlatformVO> platform = this.getPlatform();
////        for (EsExpressPlatformVO vo : platform) {
////            this.add(vo);
////        }
        try {


            QueryWrapper<EsExpressPlatform> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsExpressPlatform::getBean, expressPlatformDTO.getBean());
            EsExpressPlatform expressPlatformDO = this.expressPlatformMapper.selectOne(queryWrapper);
            if (expressPlatformDO == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());

            }
            //校验之前开启状态是未开启，改为开启
            if (expressPlatformDO.getIsOpen().equals(0) && expressPlatformDTO.getIsOpen().equals(1)) {
                //修改开启状态
                QueryWrapper<EsExpressPlatform> update = new QueryWrapper<>();
                update.lambda().in(EsExpressPlatform::getIsOpen, 1);
                List<EsExpressPlatform> platforms = this.expressPlatformMapper.selectList(update);
                platforms.stream().forEach(platf -> {
                    platf.setIsOpen(0);
                    this.expressPlatformMapper.updateById(platf);
                });
            }
            EsExpressPlatform expressPlatform = new EsExpressPlatform();
            BeanUtil.copyProperties(expressPlatformDTO, expressPlatform);
            queryWrapper.lambda().eq(EsExpressPlatform::getBean, expressPlatformDTO.getBean());

            jedisCluster.del(CachePrefix.EXPRESS.getPrefix());
            this.expressPlatformMapper.update(expressPlatform, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     */
    @Override
    public DubboResult<EsExpressPlatformDO> getExpressPlatform(Long id) {
        try {
            QueryWrapper<EsExpressPlatform> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsExpressPlatform::getId, id);
            EsExpressPlatform expressPlatform = this.expressPlatformMapper.selectOne(queryWrapper);
            EsExpressPlatformDO expressPlatformDO = new EsExpressPlatformDO();
            if (expressPlatform == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(expressPlatform, expressPlatformDO);
            return DubboResult.success(expressPlatformDO);
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
     * @param pageSize 页码
     * @param pageNum  页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsExpressPlatformDO>
     */
    @Override
    public DubboPageResult<EsExpressPlatformVO> getExpressPlatformList(int pageSize, int pageNum) {
        try {
            // 查询条件
            List<EsExpressPlatformVO> resultList = this.getPlatform();
            return DubboPageResult.success(resultList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsExpressPlatformDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteExpressPlatform(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsExpressPlatform> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsExpressPlatform::getId, id);
            this.expressPlatformMapper.delete(deleteWrapper);
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

    @Override
    public DubboResult open(String bean) {

        try {
            //修改开启状态
            QueryWrapper<EsExpressPlatform> update = new QueryWrapper<>();
            update.lambda().in(EsExpressPlatform::getIsOpen, 1);
            List<EsExpressPlatform> platforms = this.expressPlatformMapper.selectList(update);
            platforms.stream().forEach(platf -> {
                platf.setIsOpen(0);
                this.expressPlatformMapper.updateById(platf);
            });
            QueryWrapper<EsExpressPlatform> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsExpressPlatform::getBean, bean);
            EsExpressPlatform esExpressPlatform = this.expressPlatformMapper.selectOne(queryWrapper);
            esExpressPlatform.setIsOpen(1);

            this.expressPlatformMapper.updateById(esExpressPlatform);
            jedisCluster.del(CachePrefix.EXPRESS.getPrefix());
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
     * 获取所有的快递查询方案
     *
     * @return 所有的快递方案
     */
    private List<EsExpressPlatformVO> getPlatform() {
        List<EsExpressPlatformVO> resultList = new ArrayList<>();

        QueryWrapper<EsExpressPlatform> queryWrapper = new QueryWrapper<>();
        List<EsExpressPlatform> list = this.expressPlatformMapper.selectList(queryWrapper);

        Map<String, EsExpressPlatform> map = new HashMap<>(16);
        for (EsExpressPlatform expressPlatformDO : list) {


            map.put(expressPlatformDO.getBean(), expressPlatformDO);
        }
        for (ExpressPlatform plugin : expressPlatforms) {
            EsExpressPlatform expressPlatform = map.get(plugin.getPluginId());
            EsExpressPlatformVO result = null;

            if (expressPlatform != null) {
                EsExpressPlatformDO esExpressPlatformDO = new EsExpressPlatformDO();
                BeanUtil.copyProperties(expressPlatform, esExpressPlatformDO);
                result = new EsExpressPlatformVO(esExpressPlatformDO);
            } else {
                result = new EsExpressPlatformVO(plugin);
            }
            resultList.add(result);
        }
        return resultList;
    }


    @Override
    public DubboResult<ExpressDetailVO> getExpressFormDetail(Long id, String nu) {
        try {
            //获取物流公司
            ExpressDetailVO expressDetailVO = new ExpressDetailVO();
            //expressDetail=null;
            DubboResult<EsLogiCompanyDO> result = logiCompanyService.getLogiCompany(id);
            if (result.isSuccess()) {
                EsLogiCompanyDO logiCompanyDO = result.getData();
                if (logiCompanyDO == null || StringUtil.isEmpty(logiCompanyDO.getCode())) {
                    logiCompanyDO.setCode("shunfeng");
                }
                //从缓存中获取开启的快递平台
                //jedisCluster.del(CachePrefix.EXPRESS.getPrefix());
                String s = jedisCluster.get(CachePrefix.EXPRESS.getPrefix());
                EsExpressPlatformVO expressPlatformVO = null;
                if (!StringUtil.isEmpty(s)) {
                    expressPlatformVO = JsonUtil.jsonToObject(s, EsExpressPlatformVO.class);
                }
                //如果没有找到则从数据库查询，将查询到的开启的快递平台放入缓存
                if (expressPlatformVO == null) {
                    QueryWrapper<EsExpressPlatform> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(EsExpressPlatform::getIsOpen, 1);
                    EsExpressPlatform platform = this.expressPlatformMapper.selectOne(queryWrapper);
                    if (platform == null) {
                        throw new ResourceNotFoundException("未找到开启的快递平台");
                    }
                    expressPlatformVO = new EsExpressPlatformVO();
                    expressPlatformVO.setConfig(platform.getConfig());
                    expressPlatformVO.setBean(platform.getBean());
                    String json = JsonUtil.objectToJson(expressPlatformVO);
                    jedisCluster.set(CachePrefix.EXPRESS.getPrefix(), json);
                }
                //得到开启的快递平台方案
                ExpressPlatform expressPlatform = this.findByBeanid(expressPlatformVO.getBean());
                //调用查询接口返回查询到的物流信息
                DubboResult<ExpressDetailVO> result1 = expressPlatform.getExpressDetail(logiCompanyDO.getCode(), nu, this.getConfig());
                if (result1.isSuccess()) {
                    expressDetailVO.setPhone(logiCompanyDO.getPhone());
                    expressDetailVO.setData(result1.getData().getData());
                    expressDetailVO.setName(result1.getData().getName());
                    expressDetailVO.setCourierNum(result1.getData().getCourierNum());
                }
            }
            return DubboResult.success(expressDetailVO);
        } catch (ArgumentException ae) {
            logger.error("获取详情失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取详情失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }


    }

    /**
     * 获取开启的快递平台方案
     *
     * @return
     */
    private Map getConfig() {
        //从缓存中获取开启的快递平台
        String s = jedisCluster.get(CachePrefix.EXPRESS.getPrefix());

        EsExpressPlatformVO platformVO = JsonUtil.jsonToObject(s, EsExpressPlatformVO.class);
        if (StringUtil.isEmpty(platformVO.getConfig())) {
            return new HashMap<>(16);
        }
        Gson gson = new Gson();
        List<EsConfigItemVO> list = gson.fromJson(platformVO.getConfig(), new TypeToken<List<EsConfigItemVO>>() {
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
     * 根据bean查询出可用的快递平台
     *
     * @param beanId
     * @return
     */
    private ExpressPlatform findByBeanid(String beanId) {
        for (ExpressPlatform expressPlatform : expressPlatforms) {
            if (expressPlatform.getPluginId().equals(beanId)) {
                return expressPlatform;
            }
        }
        //如果走到这里，说明找不到可用的快递平台
        throw new ResourceNotFoundException("未找到可用的快递平台");
    }
}
