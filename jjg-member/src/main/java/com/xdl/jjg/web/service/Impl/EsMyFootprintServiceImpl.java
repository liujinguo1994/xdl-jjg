package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMyFootprintDO;
import com.shopx.member.api.model.domain.dto.EsMyFootprintDTO;
import com.shopx.member.api.service.IEsMyFootprintService;
import com.xdl.jjg.entity.EsMyFootprint;
import com.shopx.member.dao.mapper.EsMyFootprintMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 会员活跃信息服务实现类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:44
 */
@Service
public class EsMyFootprintServiceImpl extends ServiceImpl<EsMyFootprintMapper, EsMyFootprint> implements IEsMyFootprintService {

    private static Logger logger = LoggerFactory.getLogger(EsMyFootprintServiceImpl.class);

    @Autowired
    private EsMyFootprintMapper myFootprintMapper;

    @Reference(version = "${dubbo.application.version}" ,timeout = 50000,check = false)
    private IEsGoodsService goodsService;

    /**
     * 插入数据
     *
     * @param myFootprintDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMyFootprintDO> insertMyFootprint(EsMyFootprintDTO myFootprintDTO) {
        try {
            long start = System.currentTimeMillis();
            logger.info("Member插入足迹开始时间1[{}]",start);
            //今日零点时间戳
            long currentTimestamps = System.currentTimeMillis();
            long oneDayTimestamps = 60 * 60 * 24 * 1000;
            long zeroTimestamps = currentTimestamps-(currentTimestamps + 60*60*8*1000) % oneDayTimestamps;
            long start1 = System.currentTimeMillis();
            logger.info("Member插入足迹2[{}]，时间差[{}]",start1,start1-start);
            //查询当天是否浏览
            QueryWrapper<EsMyFootprint> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMyFootprint::getGoodsId, myFootprintDTO.getGoodsId())
                    .eq(EsMyFootprint::getMemberId, myFootprintDTO.getMemberId())
                    .gt(EsMyFootprint::getCreateTime, zeroTimestamps);
            EsMyFootprint myFootprint = myFootprintMapper.selectOne(queryWrapper);
            long start2 = System.currentTimeMillis();
            logger.info("Member插入足迹3[{}]，时间差[{}]",start2,start2-start1);
            //如果存在,更新浏览时间
            EsMyFootprintDO myFootprintDO = new EsMyFootprintDO();
            if(myFootprint != null){
                myFootprint.setCreateTime(currentTimestamps);
                myFootprintMapper.updateById(myFootprint);
                BeanUtil.copyProperties(myFootprint, myFootprintDO);
                long start3 = System.currentTimeMillis();
                logger.info("Member插入足迹4[{}]，时间差[{}]",start3,start3-start2);
                return DubboResult.success(myFootprintDO);
            }
            myFootprint = new EsMyFootprint();
            BeanUtil.copyProperties(myFootprintDTO, myFootprint);
            myFootprintMapper.insert(myFootprint);
            goodsService.updateViewCount(myFootprint.getGoodsId());
            BeanUtil.copyProperties(myFootprint, myFootprintDO);
            long start5 = System.currentTimeMillis();
            logger.info("Member插入足迹5[{}]，时间差[{}]",start5,start5-start2);
            return DubboResult.success(myFootprintDO);
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param myFootprintDTO DTO
     * @param id             主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMyFootprint(EsMyFootprintDTO myFootprintDTO, Long id) {
        try {
            EsMyFootprint myFootprint = this.myFootprintMapper.selectById(id);
            if (myFootprint == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(myFootprintDTO, myFootprint);
            QueryWrapper<EsMyFootprint> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMyFootprint::getId, id);
            this.myFootprintMapper.update(myFootprint, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @Override
    public DubboResult<EsMyFootprintDO> getMyFootprint(Long id) {
        try {
            EsMyFootprint myFootprint = this.myFootprintMapper.selectById(id);
            EsMyFootprintDO myFootprintDO = new EsMyFootprintDO();
            if (myFootprint == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(myFootprint, myFootprintDO);
            return DubboResult.success(myFootprintDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param memberId 页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMyFootprintDO>
     */
    @Override
    public DubboPageResult<EsMyFootprintDO> getMyFootprintPage(int pageSize, int pageNum, Long memberId) {
        try {
            Page<EsMyFootprintDO> page = new Page<>(pageNum, pageSize);
            IPage<EsMyFootprintDO> iPage = this.myFootprintMapper.getFootListsPage(page, memberId);
            return DubboPageResult.success(iPage.getTotal(), iPage.getRecords());
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param myFootprintDTO myFootprintDTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMyFootprintDO>
     */
    @Override
    public DubboResult<EsMyFootprintDO> getMyFootprintList(EsMyFootprintDTO myFootprintDTO) {
        QueryWrapper<EsMyFootprint> queryWrapper = new QueryWrapper<>();

        try {
            if(myFootprintDTO.getMemberId() != null){
                queryWrapper.lambda().eq(EsMyFootprint::getMemberId, myFootprintDTO.getMemberId());
            }
            if(myFootprintDTO.getGoodsId() != null){
                queryWrapper.lambda().eq(EsMyFootprint::getGoodsId, myFootprintDTO.getGoodsId());
            }
            if(myFootprintDTO.getShopId() != null){
                queryWrapper.lambda().eq(EsMyFootprint::getShopId, myFootprintDTO.getShopId());
            }

            List<EsMyFootprint> list = myFootprintMapper.selectList(queryWrapper);
            List<EsMyFootprintDO> resultList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(list)){
                resultList = BeanUtil.copyList(list, EsMyFootprintDO.class);
                resultList = resultList.stream().map(e -> {
                    e.setViewTime(DateFormatUtils.format(e.getCreateTime(), "yyyy-MM-dd"));
                    return e;
                }).collect(Collectors.toList());

            }
            return DubboResult.success(resultList);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

        /**
         * 根据时间删除数据
         *
         * @param memberId
         * @param viewTime
         * @auther: lins 1220316142@qq.com
         * @date: 2019/05/31 16:40:44
         * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
         */
        @Override
        @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
        public DubboResult deleteMyFoot (Long memberId,String viewTime){
            try {
                this.myFootprintMapper.deleteMyFoot(memberId,viewTime);
                return DubboResult.success();
            } catch (ArgumentException ae) {
                logger.error("删除失败", ae);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
            } catch (Throwable th) {
                logger.error("删除失败", th);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
            }
        }

    @Override
    public DubboResult getTopMyFoot(Long memberId, Long shopId) {
        try {
            List<EsMyFootprintDO> esMyFootprintDOList = this.myFootprintMapper.getTopMyFoot(memberId, shopId);
            if(CollectionUtils.isEmpty(esMyFootprintDOList)){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            return DubboPageResult.success(esMyFootprintDOList);
        } catch (ArgumentException ae) {
            logger.error("top10查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("top10查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id
     * @auther: xl 2917729601@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMyFootById (Long id){
        try {
            myFootprintMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult clearMyFoot(Long memberId) {
        try {
            QueryWrapper<EsMyFootprint> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMyFootprint::getMemberId,memberId);
            this.myFootprintMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("清空我的足迹失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("清空我的足迹失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMyFoot(Integer[] ids) {
        try {
            QueryWrapper<EsMyFootprint> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsMyFootprint::getId, ids);
            this.myFootprintMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("批量删除我的足迹失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("批量删除我的足迹失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
