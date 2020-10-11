package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsArticleCategory;
import com.xdl.jjg.mapper.EsArticleCategoryMapper;
import com.xdl.jjg.model.domain.EsArticleCategoryDO;
import com.xdl.jjg.model.dto.EsArticleCategoryDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsArticleCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章分类 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Service
public class EsArticleCategoryServiceImpl extends ServiceImpl<EsArticleCategoryMapper, EsArticleCategory> implements IEsArticleCategoryService {

    private static Logger logger = LoggerFactory.getLogger(EsArticleCategoryServiceImpl.class);

    @Autowired
    private EsArticleCategoryMapper articleCategoryMapper;

    /**
     * 插入文章分类数据
     *
     * @param articleCategoryDTO 文章分类DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertArticleCategory(EsArticleCategoryDTO articleCategoryDTO) {
        try {
            if (StringUtils.isEmpty(articleCategoryDTO.getParentId())) {
                articleCategoryDTO.setParentId(0L);
            }
            EsArticleCategory parent = null;
            // 非顶级分类
            if (articleCategoryDTO.getParentId() != 0) {
                parent = articleCategoryMapper.selectById(articleCategoryDTO.getParentId());
                if (parent == null) {
                    throw new ArgumentException(ErrorCode.PARENT_CATEGORY_NOT_EXIT.getErrorCode(), "父分类不存在");
                }
                // 判断分类级别
                String catPath = parent.getPath().replace("|", ",");
                String[] str = catPath.split(",");
                if (str.length >= 3) {
                    throw new ArgumentException(ErrorCode.CATEGORY_MORE_THAN_TWO.getErrorCode(), "最多为二级分类,添加失败");
                }
            }
            articleCategoryDTO.setAllowDelete(1);
            //articleCategoryDTO.setType(ArticleCategoryType.OTHER.value());

            EsArticleCategory articleCategory = new EsArticleCategory();
            BeanUtil.copyProperties(articleCategoryDTO, articleCategory);
            this.articleCategoryMapper.insert(articleCategory);
            //更新分类路径
            EsArticleCategory category = new EsArticleCategory();
            category.setId(articleCategory.getId());
            if (parent != null) {
                category.setPath(parent.getPath() + articleCategory.getId() + "|");
            } else {// 是顶级类别
                category.setPath("0|" + articleCategory.getId() + "|");
            }
            articleCategoryMapper.updateById(category);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("文章分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("文章分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新文章分类数据
     *
     * @param articleCategoryDTO 文章分类DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateArticleCategory(EsArticleCategoryDTO articleCategoryDTO) {
        try {
            EsArticleCategory cat = articleCategoryMapper.selectById(articleCategoryDTO.getId());
            if (cat == null){
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), "数据不存在");
            }
            //只有类型为other的才可以修改
           /* if (!ArticleCategoryType.OTHER.value().equals(cat.getType())) {
                throw new ArgumentException(ErrorCode.UNABLE_UPDATE.getErrorCode(), "特殊的文章分类，不可修改");
            }*/
            if (StringUtils.isEmpty(articleCategoryDTO.getParentId())) {
                articleCategoryDTO.setParentId(0L);
            }
            articleCategoryDTO.setPath(0 + "|" + cat.getId() + "|");
            // 非顶级分类
            if (articleCategoryDTO.getParentId() != 0) {
                EsArticleCategory parent = articleCategoryMapper.selectById(articleCategoryDTO.getParentId());
                if (parent == null) {
                    throw new ArgumentException(ErrorCode.PARENT_CATEGORY_NOT_EXIT.getErrorCode(), "父分类不存在");
                }
                // 判断分类级别
                String catPath = parent.getPath().replace("|", ",");
                String[] str = catPath.split(",");
                if (str.length >= 3) {
                    throw new ArgumentException(ErrorCode.CATEGORY_MORE_THAN_TWO.getErrorCode(), "最多为二级分类,添加失败");
                }

                articleCategoryDTO.setPath(parent.getPath() + cat.getId() + "|");
            }
            articleCategoryDTO.setAllowDelete(1);
            //articleCategoryDTO.setType(ArticleCategoryType.OTHER.value());

            EsArticleCategory articleCategory = new EsArticleCategory();
            BeanUtil.copyProperties(articleCategoryDTO, articleCategory);
            QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticleCategory::getId, articleCategoryDTO.getId());
            this.articleCategoryMapper.update(articleCategory, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("文章分类更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("文章分类更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取文章分类详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     */
    @Override
    public DubboResult<EsArticleCategoryDO> getArticleCategory(Long id) {
        try {
            QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticleCategory::getId, id);
            EsArticleCategory articleCategory = this.articleCategoryMapper.selectOne(queryWrapper);
            EsArticleCategoryDO articleCategoryDO = new EsArticleCategoryDO();
            if (articleCategory == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(articleCategory, articleCategoryDO);
            return DubboResult.success(articleCategoryDO);
        } catch (ArgumentException ae){
            logger.error("文章分类查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("文章分类查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询文章分类列表
     *
     * @param articleCategoryDTO 文章分类DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboPageResult<EsArticleCategoryDO>
     */
    @Override
    public DubboPageResult<EsArticleCategoryDO> getArticleCategoryList(EsArticleCategoryDTO articleCategoryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().like(StringUtil.notEmpty(articleCategoryDTO.getName()),EsArticleCategory::getName,articleCategoryDTO.getName()).eq(EsArticleCategory::getParentId,0).orderByAsc(EsArticleCategory::getSort);

            Page<EsArticleCategory> page = new Page<>(pageNum, pageSize);
            IPage<EsArticleCategory> iPage = this.page(page, queryWrapper);
            List<EsArticleCategoryDO> articleCategoryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                articleCategoryDOList = iPage.getRecords().stream().map(articleCategory -> {
                    EsArticleCategoryDO articleCategoryDO = new EsArticleCategoryDO();
                    BeanUtil.copyProperties(articleCategory, articleCategoryDO);
                    return articleCategoryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),articleCategoryDOList);
        } catch (ArgumentException ae){
            logger.error("文章分类分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("文章分类分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除文章分类数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteArticleCategory(Long id) {
        try {
            if (id == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            EsArticleCategory cat = articleCategoryMapper.selectById(id);
            //只有类型为other的才可以删除
           /* if (cat == null || !ArticleCategoryType.OTHER.value().equals(cat.getType())) {
                throw new ArgumentException(ErrorCode.UNABLE_DELETE.getErrorCode(), "特殊的文章分类，不可删除");
            }*/
            //判断是否有子分类
            QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticleCategory::getParentId,id);
            List<EsArticleCategory> list = articleCategoryMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(list)){
                throw new ArgumentException(ErrorCode.EXIT_CHILDREN_CATEGORY_NOT_DEL.getErrorCode(),"存在子分类,不能删除");
            }
            QueryWrapper<EsArticleCategory> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsArticleCategory::getId, id);
            this.articleCategoryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("文章分类删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("文章分类删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //查询二级文章分类列表
    @Override
    public DubboPageResult<EsArticleCategoryDO> getChildren(Long id) {
        try {
            QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticleCategory::getParentId,id).orderByAsc(EsArticleCategory::getSort);
            List<EsArticleCategory> list = articleCategoryMapper.selectList(queryWrapper);
            List<EsArticleCategoryDO> doList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)){
                 doList = (List<EsArticleCategoryDO>) BeanUtil.copyList(list, new EsArticleCategoryDO().getClass());
            }
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae){
            logger.error("查询二级文章分类列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询二级文章分类列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    //查询文章分类树
    @Override
    public DubboPageResult<EsArticleCategoryDO> getTree() {
        try {
            QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(EsArticleCategory::getId);
            List<EsArticleCategory> list = articleCategoryMapper.selectList(queryWrapper);
            List<EsArticleCategoryDO> doList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)){
                doList = (List<EsArticleCategoryDO>) BeanUtil.copyList(list, new EsArticleCategoryDO().getClass());
            }
            List<EsArticleCategoryDO> newCategoryList = new ArrayList<>();
            for (EsArticleCategoryDO category : doList) {
                if (category.getParentId() == 0) {
                    List<EsArticleCategoryDO> children = getChildren(doList, category.getId());
                    category.setChildren(children);
                    newCategoryList.add(category);
                }
            }
            return DubboPageResult.success(newCategoryList);
        } catch (ArgumentException ae){
            logger.error("查询文章分类树失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询文章分类树失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 在一个集合中查找子
     *
     * @param categoryList 所有分类集合
     * @param parentid     父id
     * @return 找到的子集合
     */
    private List<EsArticleCategoryDO> getChildren(List<EsArticleCategoryDO> categoryList, Long parentid) {
        List<EsArticleCategoryDO> children = new ArrayList<>();
        for (EsArticleCategoryDO category : categoryList) {
            if (category.getParentId().compareTo(parentid) == 0) {
                category.setChildren(this.getChildren(categoryList, category.getId()));
                children.add(category);
            }
        }
        return children;
    }
}
