package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCartConfigure;
import com.xdl.jjg.mapper.EsCartConfigureMapper;
import com.xdl.jjg.model.domain.EsCartConfigureDO;
import com.xdl.jjg.model.dto.EsCartConfigureDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCartConfigureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车配置 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-04
 */
@Service
public class EsCartConfigureServiceImpl extends ServiceImpl<EsCartConfigureMapper, EsCartConfigure> implements IEsCartConfigureService {

    private static Logger logger = LoggerFactory.getLogger(EsCartConfigureServiceImpl.class);

    @Autowired
    private EsCartConfigureMapper cartConfigureMapper;

    /**
     * 插入购物车配置数据
     *
     * @param cartConfigureDTO 购物车配置DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 11:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCartConfigure(EsCartConfigureDTO cartConfigureDTO) {
        try {
            QueryWrapper<EsCartConfigure> queryWrapper=new QueryWrapper<>();
            Integer integer = this.cartConfigureMapper.selectCount(queryWrapper);
            if (integer>0){
                throw new ArgumentException(MemberErrorCode.EXIST_CONFIGURE.getErrorCode(), MemberErrorCode.EXIST_CONFIGURE.getErrorMsg());
            }
            EsCartConfigure cartConfigure = new EsCartConfigure();
            BeanUtil.copyProperties(cartConfigureDTO, cartConfigure);
            this.cartConfigureMapper.insert(cartConfigure);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("购物车配置新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("购物车配置新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新购物车配置数据
     *
     * @param cartConfigureDTO 购物车配置DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 11:19:30
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCartConfigure(EsCartConfigureDTO cartConfigureDTO) {
        try {
            EsCartConfigure esCartConfigure = this.cartConfigureMapper.selectById(cartConfigureDTO.getId());
            if (esCartConfigure ==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_CONFIGURE.getErrorCode(), MemberErrorCode.NOTEXIST_CONFIGURE.getErrorMsg());
            }
            EsCartConfigure cartConfigure = new EsCartConfigure();
            BeanUtil.copyProperties(cartConfigureDTO, cartConfigure);
            QueryWrapper<EsCartConfigure> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCartConfigure::getId, cartConfigureDTO.getId());
            this.cartConfigureMapper.update(cartConfigure, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("购物车配置更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车配置更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取购物车配置详情
     *
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:29:26
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    @Override
    public DubboResult<EsCartConfigureDO> getCartConfigure() {
        try {
            QueryWrapper<EsCartConfigure> queryWrapper=new QueryWrapper<>();
            List<EsCartConfigure> esCartConfigures = this.list();
            //List<EsCartConfigure> esCartConfigures = this.cartConfigureMapper.selectList(queryWrapper);
            EsCartConfigure cartConfigure = esCartConfigures.get(0);
            EsCartConfigureDO cartConfigureDO = new EsCartConfigureDO();
            if (cartConfigure == null) {
                throw new ArgumentException(MemberErrorCode.NOTEXIST_CONFIGURE.getErrorCode(), MemberErrorCode.NOTEXIST_CONFIGURE.getErrorMsg());
            }
            BeanUtil.copyProperties(cartConfigure, cartConfigureDO);
            return DubboResult.success(cartConfigureDO);
        } catch (ArgumentException ae){
            logger.error("购物车配置查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车配置查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询购物车配置列表
     *
     * @param cartConfigureDTO 购物车配置DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:29:10
     * @return: com.shopx.common.model.result.DubboPageResult<EsCartConfigureDO>
     */
    @Override
    public DubboPageResult<EsCartConfigureDO> getCartConfigureList(EsCartConfigureDTO cartConfigureDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCartConfigure> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCartConfigure> page = new Page<>(pageNum, pageSize);
            IPage<EsCartConfigure> iPage = this.page(page, queryWrapper);
            List<EsCartConfigureDO> cartConfigureDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                cartConfigureDOList = iPage.getRecords().stream().map(cartConfigure -> {
                    EsCartConfigureDO cartConfigureDO = new EsCartConfigureDO();
                    BeanUtil.copyProperties(cartConfigure, cartConfigureDO);
                    return cartConfigureDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),cartConfigureDOList);
        } catch (ArgumentException ae){
            logger.error("购物车配置分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("购物车配置分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除购物车配置数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/02 10:30:30
     * @return: com.shopx.common.model.result.DubboResult<EsCartConfigureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCartConfigure(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            //唯一购物车配置无法删除
            QueryWrapper<EsCartConfigure> queryWrapper=new QueryWrapper<>();
            Integer integer = this.cartConfigureMapper.selectCount(queryWrapper);
            if (integer==1){
                throw new ArgumentException(MemberErrorCode.NOTALLOW_CONFIGURE.getErrorCode(), MemberErrorCode.NOTALLOW_CONFIGURE.getErrorMsg());
            }

            this.cartConfigureMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("购物车配置删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("购物车配置删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
