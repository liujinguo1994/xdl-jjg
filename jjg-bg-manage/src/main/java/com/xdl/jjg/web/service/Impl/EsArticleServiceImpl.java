package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsArticle;
import com.xdl.jjg.entity.EsArticleCategory;
import com.xdl.jjg.mapper.EsArticleCategoryMapper;
import com.xdl.jjg.mapper.EsArticleMapper;
import com.xdl.jjg.model.domain.EsArticleDO;
import com.xdl.jjg.model.dto.EsArticleDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsArticleService;
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

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Service
public class EsArticleServiceImpl extends ServiceImpl<EsArticleMapper, EsArticle> implements IEsArticleService {

    private static Logger logger = LoggerFactory.getLogger(EsArticleServiceImpl.class);

    @Autowired
    private EsArticleMapper articleMapper;

    @Autowired
    private EsArticleCategoryMapper articleCategoryMapper;

    /**
     * 插入文章数据
     *
     * @param articleDTO 文章DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertArticle(EsArticleDTO articleDTO) {
        try {
            if (articleDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsArticle article = new EsArticle();
            BeanUtil.copyProperties(articleDTO, article);
            //article.setShowPosition(ArticleShowPosition.OTHER.name());
            this.articleMapper.insert(article);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("文章新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("文章新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新文章数据
     *
     * @param articleDTO 文章DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateArticle(EsArticleDTO articleDTO) {
        try {
            if (articleDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsArticle esArticle = articleMapper.selectById(articleDTO.getId());
            if (esArticle == null){
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), "数据不存在");
            }
            EsArticle article = new EsArticle();
            BeanUtil.copyProperties(articleDTO, article);
            QueryWrapper<EsArticle> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticle::getId, articleDTO.getId());
            //article.setShowPosition(esArticle.getShowPosition());
            this.articleMapper.update(article, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("文章更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("文章更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取文章详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     */
    @Override
    public DubboResult<EsArticleDO> getArticle(Long id) {
        try {
            QueryWrapper<EsArticle> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticle::getId, id);
            EsArticle article = this.articleMapper.selectOne(queryWrapper);
            EsArticleDO articleDO = new EsArticleDO();
            if (article == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(article, articleDO);
            return DubboResult.success(articleDO);
        } catch (ArgumentException ae){
            logger.error("文章查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("文章查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询文章列表
     *
     * @param articleDTO 文章DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboPageResult<EsArticleDO>
     */
    @Override
    public DubboPageResult<EsArticleDO> getArticleList(EsArticleDTO articleDTO, int pageSize, int pageNum) {
        try {
            Page<EsArticleDO> page = new Page<>(pageNum, pageSize);
            IPage<EsArticleDO> iPage = null;
            //分类id为空则查询全部
            if (StringUtils.isEmpty(articleDTO.getCategoryId())){
                 iPage = articleMapper.getAllList(page,articleDTO.getArticleName());
            }else {
                //分类id不为空则根据分类去查
                List<Long> categoryIdList = new ArrayList<>();
                QueryWrapper<EsArticleCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsArticleCategory::getParentId,articleDTO.getCategoryId());
                List<EsArticleCategory> categoryList = articleCategoryMapper.selectList(queryWrapper);
                //如果是对底级分类查询 则只需要查询其分类就可以，否则需要将其下级分类数据全部查询出来
                if (categoryList.size() > 0) {
                    categoryList.stream().forEach(esArticleCategory -> {
                        categoryIdList.add(esArticleCategory.getId());
                    });
                }
                categoryIdList.add(articleDTO.getCategoryId());
                Long[] categoryIds = new Long[categoryIdList.size()];
                categoryIdList.toArray(categoryIds);
                iPage = articleMapper.getListByCategoryId(page, articleDTO.getArticleName(), categoryIds);
            }

            if (iPage.getTotal() == 0){
                return DubboPageResult.success(iPage.getTotal(),new ArrayList<EsArticleDO>());
            }
            return DubboPageResult.success(iPage.getTotal(),iPage.getRecords());
        } catch (ArgumentException ae){
            logger.error("文章分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("文章分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除文章数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-07-24
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteArticle(Long id) {
        try {
            if (id == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            //EsArticle article = articleMapper.selectById(id);
            /*if (article == null || !ArticleShowPosition.valueOf(article.getShowPosition()).equals(ArticleShowPosition.OTHER)) {
                throw new ArgumentException(ErrorCode.ARTICLE_UNABLE_DELETE.getErrorCode(), "该文章不可删除，只可修改");
            }*/
            QueryWrapper<EsArticle> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsArticle::getId, id);
            this.articleMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("文章删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("文章删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //获取帮助页面总数
    @Override
    public DubboResult<Integer> articleCount() {
        try {
            QueryWrapper<EsArticle> queryWrapper = new QueryWrapper<>();
            Integer count = articleMapper.selectCount(queryWrapper);
            return DubboResult.success(count);
        } catch (ArgumentException ae){
            logger.error("获取帮助页面总数失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取帮助页面总数失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    //分页查询文章列表
    @Override
    public DubboPageResult<EsArticleDO> getList(int pageSize, int pageNum) {
        try {
            Page<EsArticle> page = new Page<>(pageNum, pageSize);
            QueryWrapper<EsArticle> queryWrapper = new QueryWrapper<>();
            IPage<EsArticle> iPage = articleMapper.selectPage(page, queryWrapper);
            List<EsArticleDO> doList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())){
                doList = BeanUtil.copyList(iPage.getRecords(), EsArticleDO.class);
            }
            return DubboPageResult.success(iPage.getTotal(),doList);
        } catch (ArgumentException ae){
            logger.error("分页查询文章列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询文章列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

     // 根据分类id查询文章列表
    @Override
    public DubboPageResult<EsArticleDO> getByCategoryId(Long categoryId) {
        try {
            if (categoryId == null){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), "参数错误");
            }
            QueryWrapper<EsArticle> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsArticle::getCategoryId,categoryId);
            List<EsArticle> esArticles = articleMapper.selectList(queryWrapper);
            List<EsArticleDO> doList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esArticles)){
                doList = BeanUtil.copyList(esArticles, EsArticleDO.class);
            }
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae){
            logger.error("根据分类id查询文章列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("根据分类id查询文章列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
