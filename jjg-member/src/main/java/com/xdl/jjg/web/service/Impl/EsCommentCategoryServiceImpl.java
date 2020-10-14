package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCommentCategoryDO;
import com.jjg.member.model.dto.EsCommentCategoryDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCommentCategory;
import com.xdl.jjg.entity.EsCommentLabel;
import com.xdl.jjg.mapper.EsCommentCategoryMapper;
import com.xdl.jjg.mapper.EsCommentLabelMapper;
import com.xdl.jjg.model.co.EsGoodsCO;
import com.xdl.jjg.model.domain.EsCategoryBrandDO;
import com.xdl.jjg.model.domain.EsCategoryDO;
import com.xdl.jjg.model.domain.EsCommentCategoryClassifyDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCommentCategoryService;
import com.xdl.jjg.web.service.feignShopService.CategoryService;
import com.xdl.jjg.web.service.feignShopService.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
@Service
public class EsCommentCategoryServiceImpl extends ServiceImpl<EsCommentCategoryMapper, EsCommentCategory> implements IEsCommentCategoryService {

    private static Logger logger = LoggerFactory.getLogger(EsCommentCategoryServiceImpl.class);

    @Autowired
    private EsCommentCategoryMapper commentCategoryMapper;

    @Autowired
    private CategoryService iEsCategoryService;

    @Autowired
    private EsCommentLabelMapper esCommentLabelMapper;

    @Autowired
    private GoodsService goodsService;

    /**
     * 插入数据
     *
     * @param categoryId Long
     * @param labelId    Integer[]
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboPageResult<EsCategoryBrandDO> saveCommentCategory(Long categoryId, Integer[] labelId) {
        try {
            //查询分类是否存在
            DubboResult<EsCategoryDO> esCategory = iEsCategoryService.getCategory(categoryId);
            if (null == esCategory.getData()) {
               throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "商品分类不存在");
            }
            //查询标签是否存在
            QueryWrapper<EsCommentLabel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsCommentLabel::getId, labelId);
            List<EsCommentLabel> esBrandList = this.esCommentLabelMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esBrandList) || (esBrandList.size() != labelId.length)) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), "传入的评论标签异常!");
            }

            //先删除该分类下所有标签
            QueryWrapper<EsCommentCategory> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsCommentCategory::getCategoryId, categoryId);
            commentCategoryMapper.delete(wrapper);

            List<EsCommentCategoryDO> list = new ArrayList<>();
            for (int i = 0; i < labelId.length; i++) {
                EsCommentCategory esCommentCategory = new EsCommentCategory();
                EsCommentCategoryDO esCommentCategoryDO = new EsCommentCategoryDO();
                esCommentCategory.setCategoryId(categoryId);
                esCommentCategory.setLabelId(labelId[i].longValue());
                this.commentCategoryMapper.insert(esCommentCategory);
                BeanUtil.copyProperties(esCommentCategory, esCommentCategoryDO);
                list.add(esCommentCategoryDO);
            }
            return DubboPageResult.success(list);
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param commentCategoryDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCommentCategory(EsCommentCategoryDTO commentCategoryDTO) {
        try {
            if (null == commentCategoryDTO || commentCategoryDTO.getId() == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCommentCategory commentCategory = new EsCommentCategory();
            BeanUtil.copyProperties(commentCategoryDTO, commentCategory);
            QueryWrapper<EsCommentCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentCategory::getId, commentCategoryDTO.getId());
            this.commentCategoryMapper.update(commentCategory, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    @Override
    public DubboResult<EsCommentCategoryDO> getCommentCategory(Long id) {
        try {
            QueryWrapper<EsCommentCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCommentCategory::getId, id);
            EsCommentCategory commentCategory = this.commentCategoryMapper.selectOne(queryWrapper);
            EsCommentCategoryDO commentCategoryDO = new EsCommentCategoryDO();
            if (commentCategory == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(),MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(commentCategory, commentCategoryDO);
            return DubboResult.success(commentCategoryDO);
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
     * @param commentCategoryDTO DTO
     * @param pageSize           页码
     * @param pageNum            页数
     * @auther:HQL 236154186@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    @Override
    public DubboPageResult<EsCommentCategoryDO> getCommentCategoryList(EsCommentCategoryDTO commentCategoryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCommentCategory> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCommentCategory> page = new Page<>(pageNum, pageSize);
            IPage<EsCommentCategory> iPage = this.page(page, queryWrapper);
            List<EsCommentCategoryDO> commentCategoryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                commentCategoryDOList = iPage.getRecords().stream().map(commentCategory -> {
                    EsCommentCategoryDO commentCategoryDO = new EsCommentCategoryDO();
                    BeanUtil.copyProperties(commentCategory, commentCategoryDO);
                    return commentCategoryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), commentCategoryDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCommentCategory(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCommentCategory> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCommentCategory::getId, id);
            this.commentCategoryMapper.delete(deleteWrapper);
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


    /**
     * 查询标签列表
     *
     * @param goodId Long
     * @auther:lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    @Override
    public DubboResult<EsCommentCategoryClassifyDO> getEsCommentCategoryList(Long goodId) {
        try {
            EsCommentCategoryClassifyDO commentCategoryClassifyDO = new EsCommentCategoryClassifyDO();
            DubboResult<EsGoodsCO> goodsResult = goodsService.getEsGoods(goodId);
            Long categoryId = null;
            if(!goodsResult.isSuccess()){
                return DubboResult.fail(goodsResult.getCode(), goodsResult.getMsg());
            }
            EsGoodsCO goodsCO= goodsResult.getData();
            categoryId = goodsCO.getCategoryId();
            if(categoryId != null){
                List<EsCommentCategoryDO> esCommentCategoryDOList = commentCategoryMapper.getEsCommentCategoryList(categoryId);
                Map<Integer, List<EsCommentCategoryDO>> map = esCommentCategoryDOList.stream()
                        .collect(Collectors.groupingBy(EsCommentCategoryDO::getType));

                if(map.containsKey(0)){
                    commentCategoryClassifyDO.setGoodLabels(map.get(0));
                }
                if(map.containsKey(1)){
                    commentCategoryClassifyDO.setExpressLabels(map.get(1));
                }
                if(map.containsKey(2)){
                    commentCategoryClassifyDO.setServiceLabels(map.get(2));
                }
            }
            return DubboResult.success(commentCategoryClassifyDO);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 查询该分类已经绑定的标签和未绑定的标签
     *
     * @param categoryId Long
     * @auther:lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    @Override
    public DubboPageResult<EsCommentCategoryDO> getEsCommentCategoryBindList(Long categoryId) {
        try {
            List<EsCommentCategoryDO> esCommentCategoryDOList = commentCategoryMapper.getEsCommentCategoryBindList(categoryId);
            return DubboPageResult.success(esCommentCategoryDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
