package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.ApplyStep1DO;
import com.jjg.member.model.domain.EsShopDO;
import com.jjg.member.model.dto.*;
import com.jjg.member.model.enums.GoodTagEnums;
import com.jjg.member.model.enums.ShopStatusEnums;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.domain.EsTagsDO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberCollectionShop;
import com.xdl.jjg.entity.EsShop;
import com.xdl.jjg.entity.EsShopDetail;
import com.xdl.jjg.mapper.EsMemberCollectionShopMapper;
import com.xdl.jjg.mapper.EsShopDetailMapper;
import com.xdl.jjg.mapper.EsShopMapper;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.model.domain.EsShopAndDetailDO;
import com.xdl.jjg.model.domain.EsShopDetailDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMemberService;
import com.xdl.jjg.web.service.IEsShopDetailService;
import com.xdl.jjg.web.service.IEsShopService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-18
 */
@Service
public class EsShopServiceImpl extends ServiceImpl<EsShopMapper, EsShop> implements IEsShopService {

    private static Logger logger = LoggerFactory.getLogger(EsShopServiceImpl.class);

    @Autowired
    private EsShopMapper shopMapper;

    @Autowired
    private EsShopDetailMapper esShopDetailMapper;
    @Autowired
    private IEsMemberService esMemberService;

    @Autowired
    private IEsShopDetailService shopDetailService;

    @Autowired
    private EsMemberCollectionShopMapper esMemberCollectionShopMapper;

    @Reference(version = "${dubbo.application.version}" ,timeout = 50000,check = false)
    private IEsGoodsService esGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsTagGoodsService esTagGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsTagsService esTagsService;
    /**
     * 插入店铺表数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/18 10:33:30
     * @param shopDTO    店铺表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<Long> insertShop(EsShopDTO shopDTO) {
        try {
            if (shopDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            String shopName = shopDTO.getShopName();
            EsShop esShop = getShopByName(shopName);
            //店铺名不可重复
            if (esShop !=null){
                throw new ArgumentException(MemberErrorCode.SHOP_HAS_EXIST.getErrorCode(), MemberErrorCode.SHOP_HAS_EXIST.getErrorMsg());
            }
            shopDTO.setShopCreatetime(DateUtils.MILLIS_PER_SECOND);
            EsShop shop = new EsShop();
            BeanUtil.copyProperties(shopDTO, shop);
            this.shopMapper.insert(shop);
            Long shopId = shop.getId();
            return DubboResult.success(shopId);
        } catch (ArgumentException ae){
            logger.error("店铺表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺表数据
     *
     * @param esShopAndDetailDTO 店铺详情
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/18 20:24:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShop(EsShopAndDetailDTO esShopAndDetailDTO) {
        try {
            EsShop esShop = this.shopMapper.selectById(esShopAndDetailDTO.getShopId());
            if (esShop==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            if (esShopAndDetailDTO.getPass()!=null){
                if (esShopAndDetailDTO.getPass()==0){
                    esShop.setState(ShopStatusEnums.OPEN.toString());
                }else {
                    esShop.setState(ShopStatusEnums.REFUSED.toString());
                    this.shopMapper.updateById(esShop);
                    return DubboResult.success();
                }
            }

            EsShop shop = new EsShop();
            EsShopDetail shopDetail=new EsShopDetail();
            esShop.setShopName(esShopAndDetailDTO.getShopName());
            esShop.setCommission(esShopAndDetailDTO.getCommission());
            BeanUtil.copyProperties(esShop,shop);
            this.shopMapper.updateById(shop);

            QueryWrapper<EsShopDetail> query = new QueryWrapper<>();
            query.lambda().eq(EsShopDetail::getShopId,esShopAndDetailDTO.getShopId());
            BeanUtil.copyProperties(esShopAndDetailDTO,shopDetail);
            this.esShopDetailMapper.update(shopDetail,query);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult updateState(String state, Long shopId) {
        try {
            EsShop esShop = this.shopMapper.selectById(shopId);
            if (esShop==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            esShop.setState(state);
            this.shopMapper.updateById(esShop);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺状态更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺状态更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

//    @Override
//    public DubboResult checkShop(Long id,int state) {
//        try{
//            EsShop esShop = this.shopMapper.selectById(id);
//            if (esShop==null){
//                return DubboResult.fail(MemberErrorCode.SHOP_EXIST.getErrorCode(), MemberErrorCode.SHOP_EXIST.getErrorMsg());
//            }
//            if (state==0){
//                esShop.setState(ShopStatusEnums.OPEN.toString());
//            }else {
//                esShop.setState(ShopStatusEnums.REFUSED.toString());
//            }
//            this.shopMapper.updateById(esShop);
//            return DubboResult.success();
//        } catch (ArgumentException ae){
//            logger.error("审核失败", ae);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
//        } catch (Throwable th) {
//            logger.error("审核失败", th);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
//        }
//    }

    @Override
    public DubboResult underShop(long id) {
        try{
            EsShop esShop = this.shopMapper.selectById(id);
            if(esShop==null) {
                return DubboResult.fail(MemberErrorCode.SHOP_EXIST.getErrorCode(), MemberErrorCode.SHOP_EXIST.getErrorMsg());
            }
            esShop.setState(ShopStatusEnums.CLOSED.toString());
            this.shopMapper.updateById(esShop);
            //下架店铺的所有商品
            esGoodsService.adminUnderEsGoods(id);
            //更新缓存
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺表关闭失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺表关闭失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult useShop(long id) {
        try{
            EsShop esShop = this.shopMapper.selectById(id);
            if(esShop==null) {
                return DubboResult.fail(MemberErrorCode.SHOP_EXIST.getErrorCode(), MemberErrorCode.SHOP_EXIST.getErrorMsg());
            }
            esShop.setState(ShopStatusEnums.OPEN.toString());
            this.shopMapper.updateById(esShop);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺表开启失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺表开启失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺表
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/18 15:30:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    public DubboResult<EsShopDO> getShop(Long id) {
        try {
            EsShop shop =this.shopMapper.selectById(id);
            EsShopDO shopDO = new EsShopDO();
            if (shop == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shop, shopDO);
            return DubboResult.success(shopDO);
        } catch (ArgumentException ae){
            logger.error("店铺表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsShopDO> getShopByMemeberId(Long memberId) {
        try {
            DubboResult<EsMemberDO> result = esMemberService.getMember(memberId);
            if (result.isSuccess()){
                EsMemberDO memberDO = result.getData();
                if (memberDO ==null){
                    throw new ArgumentException(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
                }
            }
            QueryWrapper<EsShop> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShop::getMemberId,memberId);
            EsShop shop =this.shopMapper.selectOne(queryWrapper);
            EsShopDO shopDO = new EsShopDO();
            if (shop == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shop, shopDO);
            return DubboResult.success(shopDO);
        } catch (ArgumentException ae){
            logger.error("店铺表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据id获取店铺表详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/24 15:30:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    @Override
    public DubboResult<EsShopAndDetailDO> getShopDetail(Long id) {
        if(null == id){
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            EsShopAndDetailDO esShopAndDetailDO =this.shopMapper.selectShopDetailById(id);
            if (esShopAndDetailDO == null) {
                throw new ArgumentException(MemberErrorCode.SHOP_EXIST.getErrorCode(), MemberErrorCode.SHOP_EXIST.getErrorMsg());
            }
            return DubboResult.success(esShopAndDetailDO);
        } catch (ArgumentException ae){
            logger.error("店铺表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsShopDO> getBuyerShopDetail(Long id) {
        if(null == id){
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            EsShop shop =shopMapper.selectById(id);
            EsShopDO shopDO = new EsShopDO();
            if (shop == null) {
                return DubboResult.success(shopDO);
            }
            BeanUtil.copyProperties(shop, shopDO);

            QueryWrapper<EsShopDetail> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopDetail::getShopId, shopDO.getId());
            EsShopDetail shopDetail = esShopDetailMapper.selectOne(queryWrapper);
            if(shopDetail != null){
                EsShopDetailDO shopDetailDO = new EsShopDetailDO();
                BeanUtil.copyProperties(shopDetail, shopDetailDO);
                QueryWrapper<EsMemberCollectionShop> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(EsMemberCollectionShop::getShopId, id);
                Integer count = esMemberCollectionShopMapper.selectCount(wrapper);
                shopDetailDO.setShopCollect(count);
                shopDO.setShopDetailDO(shopDetailDO);
            }

            Long hotId = 0L;
            Long newId = 0L;
            DubboPageResult<EsTagsDO> tagsDOPageResult = esTagsService.getTagsList(null);
            if(tagsDOPageResult.isSuccess()){
                List<EsTagsDO> list = tagsDOPageResult.getData().getList();
                for(EsTagsDO adminTagsDO : list){
                    if(adminTagsDO.getTagName().equals("热销")){
                        hotId = adminTagsDO.getId();
                    }
                    if(adminTagsDO.getTagName().equals("新品上架")){
                        newId = adminTagsDO.getId();
                    }
                }

            }

            //热销商品
            List<com.xdl.jjg.model.domain.EsMemberGoodsDO> memberHotGoodsDOList = new ArrayList<>();
            if(hotId != 0L){
                DubboPageResult<EsGoodsDO> goodsDOHotResult = esTagGoodsService.getBuyerAdminGoodsTags(id, hotId, GoodTagEnums.HOT.getValue());
                if(goodsDOHotResult.isSuccess()){
                    List<EsGoodsDO> list = goodsDOHotResult.getData().getList();
                    if(CollectionUtils.isNotEmpty(list)){
                        memberHotGoodsDOList = BeanUtil.copyList(list, com.xdl.jjg.model.domain.EsMemberGoodsDO.class);
                    }
                }
            }
            shopDO.setHotGoodList(memberHotGoodsDOList);

            //上新商品
            List<com.xdl.jjg.model.domain.EsMemberGoodsDO> memberNewGoodsDOList = new ArrayList<>();
            if(newId != 0L){
                DubboPageResult<EsGoodsDO> goodsDONewResult = esTagGoodsService.getBuyerAdminGoodsTags(id, newId, GoodTagEnums.NEW.getValue());
                if(goodsDONewResult.isSuccess()){
                    List<EsGoodsDO> list = goodsDONewResult.getData().getList();
                    if(CollectionUtils.isNotEmpty(list)){
                        memberNewGoodsDOList = BeanUtil.copyList(list, com.xdl.jjg.model.domain.EsMemberGoodsDO.class);
                    }
                }
            }
            shopDO.setNewGoodList(memberNewGoodsDOList);
            return DubboResult.success(shopDO);
        } catch (ArgumentException ae){
            logger.error("店铺表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *
     * @param shopName
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/6/18 13:37:16
     * @return EsShop
     */
    private EsShop getShopByName(String shopName) {
        QueryWrapper<EsShop> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsShop::getShopName, shopName);
        EsShop shop = this.shopMapper.selectOne(queryWrapper);
        return shop;

    }

    /**
     * 根据查询店铺表列表
     *
     * @param shopQueryParam 店铺查询条件
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/18 15:44:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopDO>
     */
    @Override
    public DubboPageResult<EsShopDO> getShopList(ShopQueryParam shopQueryParam, int pageSize, int pageNum) {
        try{
            IPage<EsShopDO> page = this.shopMapper.getAllShop(new Page(pageNum,pageSize),shopQueryParam);
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        } catch (ArgumentException ae){
            logger.error("获取分类绑定的品牌，包括未选中的", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取分类绑定的品牌，包括未选中的", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


    /**
     * 根据主键删除店铺表数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/18 15:21:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShop(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            EsShop shop = this.shopMapper.selectById(id);
            if (shop == null) {
                throw new ArgumentException(MemberErrorCode.SHOP_EXIST.getErrorCode(), MemberErrorCode.SHOP_EXIST.getErrorMsg());
            }
            this.shopMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 初始化店铺信息
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/09 14:21:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    public DubboResult saveInit(Long memberId) {
        try{
            //获取会员系信息
           // Buyer buyer = UserContext.getBuyer();
            EsShopDTO shopDTO=new EsShopDTO();
            EsShop esShop = this.getShopByMemberId(memberId);
            DubboResult<EsMemberDO> data = esMemberService.getMember(memberId);
            EsMemberDO member = data.getData();
            if (member==null){
                throw new ArgumentException(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
            }
            //查看会员时候拥有店铺
            if(esShop==null) {
                //初始化店铺信息
                shopDTO.setState(ShopStatusEnums.APPLYING.toString());
                //设置会员信息\
                shopDTO.setMemberId(memberId);
                shopDTO.setMemberName(member.getName());
                DubboResult<Long> insertShop = this.insertShop(shopDTO);
                Long shopId = insertShop.getData();

                EsShopDetailDTO shopDetail = new EsShopDetailDTO();
                this.initShopDetail(shopDetail);
                //设置店铺id
                shopDetail.setShopId(shopId);
                this.shopDetailService.insertShopDetail(shopDetail);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺初始化失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺初始化失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 初始化店铺信息
     * @param shopDetailDTO
     */
    private  void  initShopDetail(EsShopDetailDTO shopDetailDTO) {
        shopDetailDTO.setShopCredit(0);
        shopDetailDTO.setShopPraiseRate(0.0);
        shopDetailDTO.setShopDescriptionCredit(5.0);
        shopDetailDTO.setShopServiceCredit(5.0);
        shopDetailDTO.setShopDeliveryCredit(5.0);
        shopDetailDTO.setShopCollect(0);
        shopDetailDTO.setShopLevel(1);
        shopDetailDTO.setGoodsNum(0);
        shopDetailDTO.setShopLevelApply(0);
        shopDetailDTO.setStoreSpaceCapacity(0.00);
        shopDetailDTO.setSelfOperated(0);
    }

    /**
     * 获取店铺信息
     * @param memberId
     */
    private  EsShop  getShopByMemberId(Long memberId) {
        QueryWrapper<EsShop> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(EsShop::getMemberId,memberId);
        EsShop shop = this.shopMapper.selectOne(queryWrapper);
        return shop;

    }

    /**
     * 申请开店第一步
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/09 14:29:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    public DubboResult<ApplyStep1DO> step1(ApplyStep1DTO applyStep1, Long memberId) {
        try{
            EsShop shop = this.getShopByMemberId(memberId);
            //判断是否拥有店铺
            if (shop ==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_SHOP.getErrorCode(), MemberErrorCode.NOTEXIST_SHOP.getErrorMsg());
            }
            if (!shop.getState().equals(ShopStatusEnums.APPLYING.name())){
                throw new ArgumentException(MemberErrorCode.HASAPPLY_ERROR.getErrorCode(), MemberErrorCode.HASAPPLY_ERROR.getErrorMsg());
            }

            //设置申请开单第一步
            applyStep1.setStep(1);
            EsShopDetailDTO shopDetailDTO=new EsShopDetailDTO();
            shopDetailDTO.setShopId(shop.getId());
            BeanUtil.copyProperties(applyStep1,shopDetailDTO);
            shopDetailService.updateShopDetail(shopDetailDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺申请第三步失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺第三步失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 申请开店第二步
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/09 15:11:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    public DubboResult<com.xdl.jjg.model.domain.ApplyStep2DO> step2(ApplyStep2DTO applyStep2, Long memberId) {
        try {
            EsShop shopByMemberId = this.getShopByMemberId(memberId);
            if (shopByMemberId == null) {
                throw new ArgumentException(MemberErrorCode.NOTEXIST_SHOP.getErrorCode(), MemberErrorCode.NOTEXIST_SHOP.getErrorMsg());
            }
            if (!shopByMemberId.getState().equals(ShopStatusEnums.APPLYING.name())){
                throw new ArgumentException(MemberErrorCode.HASAPPLY_ERROR.getErrorCode(), MemberErrorCode.HASAPPLY_ERROR.getErrorMsg());
            }
            DubboResult<EsShopDetailDO> result = shopDetailService.getByShopId(shopByMemberId.getId());
            EsShopDetailDO esShopDetailDO = result.getData();

            //判断是否拥有店铺
            //this.whetheHaveShop(shop);
            //没有完成第一步不允许此步操作
            if (esShopDetailDO.getStep() == null) {
                throw new ArgumentException(MemberErrorCode.NOTSTRP1_SHOP.getErrorCode(), MemberErrorCode.NOTSTRP1_SHOP.getErrorMsg());
            }
            //未完成第三步则设置为第二步
            if (esShopDetailDO.getStep() < 3) {
                applyStep2.setStep(2);
            }

            if (applyStep2.getLicenceStart() > applyStep2.getLicenceEnd()) {
                throw new ArgumentException(MemberErrorCode.TIME_ERROR.getErrorCode(), MemberErrorCode.TIME_ERROR.getErrorMsg());
            }

            EsShopDetailDTO shopDetailDTO = new EsShopDetailDTO();
            shopDetailDTO.setShopId(shopByMemberId.getId());
            BeanUtil.copyProperties(applyStep2, shopDetailDTO);
            shopDetailService.updateShopDetail(shopDetailDTO);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("店铺申请第二步失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺申请第二步失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    /**
     * 申请开店第三步
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/09 15:19:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    public DubboResult<com.xdl.jjg.model.domain.ApplyStep3DO> step3(ApplyStep3DTO applyStep3, Long memberId) {
        try{
            EsShop shopByMemberId = this.getShopByMemberId(memberId);
            if (shopByMemberId ==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_SHOP.getErrorCode(), MemberErrorCode.NOTEXIST_SHOP.getErrorMsg());
            }
            if (!shopByMemberId.getState().equals(ShopStatusEnums.APPLYING.name())){
                throw new ArgumentException(MemberErrorCode.HASAPPLY_ERROR.getErrorCode(), MemberErrorCode.HASAPPLY_ERROR.getErrorMsg());
            }
            DubboResult<EsShopDetailDO> result = shopDetailService.getByShopId(shopByMemberId.getId());
            EsShopDetailDO esShopDetailDO = result.getData();
            //判断是否拥有店铺
            //this.whetheHaveShop(shop);
            //没有完成第二步不允许此步操作
            if(esShopDetailDO.getStep()==null||esShopDetailDO.getStep()<2){
                throw new ArgumentException(MemberErrorCode.NOTSTRP1_SHOP.getErrorCode(), MemberErrorCode.NOTSTRP1_SHOP.getErrorMsg());
            }
            applyStep3.setStep(3);

            EsShopDetailDTO shopDetailDTO=new EsShopDetailDTO();
            shopDetailDTO.setShopId(shopByMemberId.getId());
            BeanUtil.copyProperties(applyStep3,shopDetailDTO);
            shopDetailService.updateShopDetail(shopDetailDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺申请第三步失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺第三步失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 申请开店第四步
     *
     * @param memberId 用户id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/09 16:18:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    @Override
    public DubboResult<com.xdl.jjg.model.domain.ApplyStep4DO> step4(ApplyStep4DTO applyStep4, Long memberId) {
        try{
            EsShop shopByMemberId = this.getShopByMemberId(memberId);
            if (shopByMemberId ==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_SHOP.getErrorCode(), MemberErrorCode.NOTEXIST_SHOP.getErrorMsg());
            }
            if (!shopByMemberId.getState().equals(ShopStatusEnums.APPLYING.name())){
                throw new ArgumentException(MemberErrorCode.HASAPPLY_ERROR.getErrorCode(), MemberErrorCode.HASAPPLY_ERROR.getErrorMsg());
            }
            DubboResult<EsShopDetailDO> result = shopDetailService.getByShopId(shopByMemberId.getId());
            EsShopDetailDO shopDetailDO = result.getData();
            //判断是否拥有店铺
            //this.whetheHaveShop(shop);
            //没有完成第三步不允许此步操作
            if(shopDetailDO.getStep()==null||shopDetailDO.getStep()<3){
                throw new ArgumentException(MemberErrorCode.NOTSTRP1_SHOP.getErrorCode(), MemberErrorCode.NOTSTRP1_SHOP.getErrorMsg());
            }
            applyStep4.setStep(4);

            boolean checkShopName = this.checkShopName(applyStep4.getShopName());
            if(checkShopName) {
                throw new ArgumentException(MemberErrorCode.SHOP_HAS_EXIST.getErrorCode(), MemberErrorCode.SHOP_HAS_EXIST.getErrorMsg());
            }

            //更新店铺基本信息
            EsShop shop = this.getShopByMemberId(memberId);
            shop.setShopName(applyStep4.getShopName());
            shop.setState(ShopStatusEnums.APPLY.toString());
            shop.setCommission(0.0);
            this.shopMapper.updateById(shop);

            //获取店铺详细信息

            EsShopDetailDTO shopDetailDTO = new EsShopDetailDTO();
            BeanUtil.copyProperties(applyStep4,shopDetailDTO);
            shopDetailDTO.setShopId(shopByMemberId.getId());
            shopDetailService.updateShopDetail(shopDetailDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺申请第四步失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺第四步失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }


    /**
     * 检测店铺名是否重复
     *
     * @param shopName 店铺名称
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/09 14:21:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    private boolean checkShopName(String shopName) {
        QueryWrapper<EsShop> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(EsShop::getShopName,shopName);
        EsShop esShop = this.shopMapper.selectOne(queryWrapper);
        if (esShop==null){
            return false;
        }
        return true;
    }
}
