package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsShopThemesDO;
import com.jjg.member.model.dto.EsShopThemesDTO;
import com.jjg.member.model.enums.ShopThemesEnum;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsShopDetail;
import com.xdl.jjg.entity.EsShopThemes;
import com.xdl.jjg.mapper.EsShopDetailMapper;
import com.xdl.jjg.mapper.EsShopThemesMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsShopThemesService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺模版 服务实现类
 * </p>
 *
 * @author lyuanj 595831329@qq.com
 * @since 2019-07-01
 */
@Service
public class EsShopThemesServiceImpl extends ServiceImpl<EsShopThemesMapper, EsShopThemes> implements IEsShopThemesService {

    private static Logger logger = LoggerFactory.getLogger(EsShopThemesServiceImpl.class);

    @Autowired
    private EsShopThemesMapper shopThemesMapper;

    @Autowired
    private EsShopDetailMapper esShopDetailMapper;
    /**
     * 插入店铺模版数据
     *
     * @param shopThemesDTO 店铺模版DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/01 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopThemes(EsShopThemesDTO shopThemesDTO) {
        try {

            //店铺模版标识不能重复
            EsShopThemes themesByMark = getThemesByMark(shopThemesDTO.getMark());
            if (themesByMark!=null){
                throw new ArgumentException(MemberErrorCode.THEMES_AGAIN.getErrorCode(), MemberErrorCode.THEMES_AGAIN.getErrorMsg());
            }

            //如果不存在模版则设置为默认模版
            Integer count = getCount();
            if (count==0){
                shopThemesDTO.setIsDefault(1);
            }
            //只能有一个默认模板
            if(shopThemesDTO.getIsDefault().equals(1)){
                editTem(shopThemesDTO.getType());
            }
            EsShopThemes shopThemes = new EsShopThemes();
            BeanUtil.copyProperties(shopThemesDTO, shopThemes);
            this.shopThemesMapper.insert(shopThemes);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺模版新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺模版新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据模版标识查询模板
     * @param mark
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/07/01 14:37:36
     * @return EsShopThemes
     */
    private EsShopThemes getThemesByMark(String mark) {
        QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsShopThemes::getMark, mark);
        EsShopThemes shopThemes = this.shopThemesMapper.selectOne(queryWrapper);
        return shopThemes;

    }

    /**
     * 修改模板是否默认
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/07/01 14:37:36
     * @return EsShopThemes
     */
    private void editTem(String type) {

        QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsShopThemes::getType, type).eq(EsShopThemes::getIsDefault,1);
        EsShopThemes shopThemes = this.shopThemesMapper.selectOne(queryWrapper);
        if (shopThemes!=null){
            shopThemes.setIsDefault(0);
            this.shopThemesMapper.updateById(shopThemes);
        }

    }

    /**
     * 查询模板总数
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/07/01 14:47:30
     * @return EsShopThemes
     */
    private Integer getCount() {
        QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
        Integer integer = this.shopThemesMapper.selectCount(queryWrapper);
        return integer;

    }

    /**
     * 根据条件更新店铺模版数据
     *
     * @param shopThemesDTO 店铺模版DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/01 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShopThemes(EsShopThemesDTO shopThemesDTO,Long id) {
        try {
            EsShopThemes shopThemes1 = this.shopThemesMapper.selectById(id);
            if (shopThemes1==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            //模版标识是否重复
            QueryWrapper<EsShopThemes> query=new QueryWrapper<>();
            query.lambda().eq(EsShopThemes::getMark,shopThemesDTO.getMark()).ne(EsShopThemes::getId,id);
            EsShopThemes themes = this.shopThemesMapper.selectOne(query);
            if (themes!=null){
                throw new ArgumentException(MemberErrorCode.THEMES_AGAIN.getErrorCode(), MemberErrorCode.THEMES_AGAIN.getErrorMsg());
            }
            //只能有一个默认模板
            if(shopThemesDTO.getIsDefault().equals(1)) {
                editTem(shopThemesDTO.getType());
            }
            EsShopThemes shopThemes = new EsShopThemes();
            BeanUtil.copyProperties(shopThemesDTO, shopThemes);
            QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopThemes::getId, id);
            this.shopThemesMapper.update(shopThemes, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺模版更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺模版更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺模版详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/01 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @Override
    public DubboResult<EsShopThemesDO> getShopThemes(Long id) {
        try {
            Assert.notNull(id,"id不能为空");

            QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopThemes::getId, id);
            EsShopThemes shopThemes = this.shopThemesMapper.selectOne(queryWrapper);
            EsShopThemesDO shopThemesDO = new EsShopThemesDO();
            if (shopThemes == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopThemes, shopThemesDO);
            return DubboResult.success(shopThemesDO);
        } catch (ArgumentException ae){
            logger.error("店铺模版查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺模版查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺模版列表
     *
     * @param shopThemesDTO 店铺模版DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/01 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopThemesDO>
     */
    @Override
    public DubboPageResult<EsShopThemesDO> getShopThemesList(EsShopThemesDTO shopThemesDTO,int pageSize, int pageNum) {
        QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if (StringUtils.isNotEmpty(shopThemesDTO.getType())){
                if(!shopThemesDTO.getType().equals(ShopThemesEnum.PC.name())&&!shopThemesDTO.getType().equals(ShopThemesEnum.WAP.name())) {
                    throw new ArgumentException(MemberErrorCode.THEMES_NOT_RIGHT.getErrorCode(), MemberErrorCode.THEMES_NOT_RIGHT.getErrorMsg());
                }
                queryWrapper.lambda().eq(EsShopThemes::getType,shopThemesDTO.getType());
            }
            if (StringUtils.isNotEmpty(shopThemesDTO.getName())){
                queryWrapper.lambda().like(EsShopThemes::getName,shopThemesDTO.getName());
            }
            Page<EsShopThemes> page = new Page<>(pageNum, pageSize);
            IPage<EsShopThemes> iPage = this.page(page, queryWrapper);
            List<EsShopThemesDO> shopThemesDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shopThemesDOList = iPage.getRecords().stream().map(shopThemes -> {
                    EsShopThemesDO shopThemesDO = new EsShopThemesDO();
                    BeanUtil.copyProperties(shopThemes, shopThemesDO);
                    return shopThemesDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),shopThemesDOList);
        } catch (ArgumentException ae){
            logger.error("店铺模版分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺模版分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺模版列表
     *
     * @param shopThemesDTO 店铺模版DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/01 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopThemesDO>
     */
    @Override
    public DubboPageResult<EsShopThemesDO> getSellerShopThemesList(EsShopThemesDTO shopThemesDTO,Long shopId,int pageSize, int pageNum) {
        QueryWrapper<EsShopThemes> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if (StringUtils.isNotEmpty(shopThemesDTO.getType())){
                if(!shopThemesDTO.getType().equals(ShopThemesEnum.PC.name())&&!shopThemesDTO.getType().equals(ShopThemesEnum.WAP.name())) {
                    throw new ArgumentException(MemberErrorCode.THEMES_NOT_RIGHT.getErrorCode(), MemberErrorCode.THEMES_NOT_RIGHT.getErrorMsg());
                }
                queryWrapper.lambda().eq(EsShopThemes::getType,shopThemesDTO.getType());
            }
            if (StringUtils.isNotEmpty(shopThemesDTO.getName())){
                queryWrapper.lambda().like(EsShopThemes::getName,shopThemesDTO.getName());
            }
            QueryWrapper<EsShopDetail> shopDetailQueryWrapper =  new QueryWrapper<>();
            shopDetailQueryWrapper.lambda().eq(EsShopDetail::getShopId,shopId);
            EsShopDetail shopDetail = this.esShopDetailMapper.selectOne(shopDetailQueryWrapper);
            AtomicReference<Long> id = new AtomicReference<>();
            if(shopThemesDTO.getType().equals(ShopThemesEnum.PC.name())){
                id.set(shopDetail.getShopThemeid());
            }else {
                id.set( shopDetail.getWapThemeid());
            }
            Page<EsShopThemes> page = new Page<>(pageNum, pageSize);
            IPage<EsShopThemes> iPage = this.page(page, queryWrapper);
            List<EsShopThemesDO> shopThemesDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shopThemesDOList = iPage.getRecords().stream().map(shopThemes -> {
                    EsShopThemesDO shopThemesDO = new EsShopThemesDO();
                    BeanUtil.copyProperties(shopThemes, shopThemesDO);
                    if(shopThemes.getId()==id.get()){
                        shopThemesDO.setCurrentUse(1);
                    }else{
                        shopThemesDO.setCurrentUse(0);
                    }
                    return shopThemesDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),shopThemesDOList);
        } catch (ArgumentException ae){
            logger.error("店铺模版分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺模版分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺模版数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/01 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopThemesDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShopThemes(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            this.shopThemesMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺模版删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺模版删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult changeShopThemes(Long themesId,Long shopId) {
        EsShopThemes shopThemes = this.shopThemesMapper.selectById(themesId);
        try{
            if(shopThemes==null) {
                throw new ArgumentException(MemberErrorCode.NOTEXIST_SHOPTEM.getErrorCode(), MemberErrorCode.NOTEXIST_SHOPTEM.getErrorMsg());
            }
            EsShopDetail esShopDetail = new EsShopDetail();
            if(shopThemes.getType().equals(ShopThemesEnum.PC.name())){
                esShopDetail.setShopThemeid(themesId);
                esShopDetail.setShopThemePath(shopThemes.getMark());
            }else{
                esShopDetail.setWapThemeid(themesId);
                esShopDetail.setWapThemePath(shopThemes.getMark());
            }
            QueryWrapper<EsShopDetail> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopDetail::getShopId,shopId);
            this.esShopDetailMapper.update(esShopDetail,queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更换店铺模版失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更换店铺模版失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
