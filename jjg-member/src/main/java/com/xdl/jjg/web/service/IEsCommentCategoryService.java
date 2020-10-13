package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsCommentCategoryClassifyDO;
import com.xdl.jjg.model.domain.EsCommentCategoryDO;
import com.xdl.jjg.model.dto.EsCommentCategoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
public interface IEsCommentCategoryService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param     categoryId Long
     * @param     labelId Integer[]
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
     DubboPageResult saveCommentCategory(Long categoryId, Integer[] labelId);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param commentCategoryDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    DubboResult updateCommentCategory(EsCommentCategoryDTO commentCategoryDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    DubboResult<EsCommentCategoryDO> getCommentCategory(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param commentCategoryDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    DubboPageResult<EsCommentCategoryDO> getCommentCategoryList(EsCommentCategoryDTO commentCategoryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    DubboResult deleteCommentCategory(Long id);

    /**
     * 查询该分类已绑定的标签和未绑定标签
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param categoryId  Long
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    DubboResult<EsCommentCategoryClassifyDO> getEsCommentCategoryList(Long categoryId);

    /**
     * 根据三级类目id查询标签列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param categoryId  Long
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    DubboPageResult<EsCommentCategoryDO> getEsCommentCategoryBindList(Long categoryId);
}
