package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsShopSildeDO;
import com.jjg.member.model.dto.EsShopSildeDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsShopSilde;
import com.xdl.jjg.mapper.EsShopSildeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsShopSildeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 店铺幻灯片 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-24
 */
@Service
public class EsShopSildeServiceImpl extends ServiceImpl<EsShopSildeMapper, EsShopSilde> implements IEsShopSildeService {

    private static Logger logger = LoggerFactory.getLogger(EsShopSildeServiceImpl.class);

    @Autowired
    private EsShopSildeMapper shopSildeMapper;

    /**
     * 插入店铺幻灯片数据
     *
     * @param shopSildeDTO 店铺幻灯片DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 20:21:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopSilde(List<EsShopSildeDTO> shopSildeDTO, Long shopId) {
        try {
            QueryWrapper<EsShopSilde> queryWrapper  = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopSilde::getShopId,shopId);
            this.shopSildeMapper.delete(queryWrapper);
            List<EsShopSilde> shopSildeList = BeanUtil.copyList(shopSildeDTO,EsShopSilde.class);
            this.saveBatch(shopSildeList);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺幻灯片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺幻灯片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺幻灯片数据
     *
     * @param list 店铺幻灯片DTO集合
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 20:25:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShopSilde(List<EsShopSildeDTO> list) {

        try {
            if (list.size() > 0) {
                for (EsShopSildeDTO shopSildeDTO : list) {
                    //获取卖家信息（补充）
                    //                shopSildeDTO.setShopId(seller.getSellerId());
                    //                if(shopSildeDTO.getSildeId()==0){
                    //                    // 如果前端传入id不为0 则为修改 否则为新增
                    //                    this.add(shopSildeDTO);
                    //                }else {
                    //对不存在的或不属于本店铺的幻灯片进行校验
                    //ShopSildeDTO model = this.getModel(shopSildeDTO.getSildeId());
                    QueryWrapper<EsShopSilde> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(EsShopSilde::getId, shopSildeDTO.getId());
                    //EsShopSilde esShopSilde = this.shopSildeMapper.selectOne(queryWrapper);
                    //                    if(esShopSilde==null||!esShopSilde.getShopId().equals(seller.getSellerId())) {
                    //                        return DubboResult.fail(MemberErrorCode.CUSTOM_ERROR_SILDE.getErrorCode(), MemberErrorCode.CUSTOM_ERROR_SILDE.getErrorMsg());
                    //                    }
                    EsShopSilde esShopSilde1 = new EsShopSilde();
                    BeanUtil.copyProperties(esShopSilde1, shopSildeDTO);
                    this.shopSildeMapper.updateById(esShopSilde1);
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺幻灯片更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺幻灯片更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }


    /**
     * 根据id获取店铺幻灯片详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 20:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    @Override
    public DubboResult<EsShopSildeDO> getShopSilde(Long id) {
        try {
            EsShopSilde shopSilde = this.shopSildeMapper.selectById(id);
            EsShopSildeDO shopSildeDO = new EsShopSildeDO();
            if (shopSilde == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopSilde, shopSildeDO);
            return DubboResult.success(shopSildeDO);
        } catch (ArgumentException ae){
            logger.error("店铺幻灯片查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺幻灯片查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺幻灯片列表
     *
     * @param shopId      店铺id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 20:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopSildeDO>
     */
    @Override
    public DubboPageResult<EsShopSildeDO> getShopSildeList(Long shopId) {
        QueryWrapper<EsShopSilde> queryWrapper = new QueryWrapper<>();
        try{
            queryWrapper.lambda().eq(EsShopSilde::getShopId,shopId);
            List<EsShopSilde> esShopSildes = this.shopSildeMapper.selectList(queryWrapper);
            return DubboPageResult.success(esShopSildes);
        } catch (ArgumentException ae){
            logger.error("店铺幻灯片分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺幻灯片分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺幻灯片数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 20:45:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopSildeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShopSilde(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }

            //对于不存在的或者不属于本店铺的幻灯片同时校验
            EsShopSilde esShopSilde = this.shopSildeMapper.selectById(id);
//            if (esShopSilde==null ||!esShopSilde.getShopId().equals(seller.getSellerId()){
//                throw new ArgumentException(MemberErrorCode.EXIST_SILDE.getErrorCode(), MemberErrorCode.EXIST_SILDE.getErrorMsg());
//
//            }
            QueryWrapper<EsShopSilde> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsShopSilde::getId, id);
            this.shopSildeMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺幻灯片删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺幻灯片删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


}
