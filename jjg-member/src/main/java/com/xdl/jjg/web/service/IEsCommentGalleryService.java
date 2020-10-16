package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCommentGalleryDO;
import com.jjg.member.model.dto.EsCommentGalleryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 评论图片 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsCommentGalleryService {

    /**
     * 插入数据
     *
     * @param commentGalleryDTO 评论图片DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    DubboResult insertCommentGallery(EsCommentGalleryDTO commentGalleryDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param commentGalleryDTO 评论图片DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    DubboResult updateCommentGallery(EsCommentGalleryDTO commentGalleryDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    DubboResult<EsCommentGalleryDO> getCommentGallery(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param commentGalleryDTO 评论图片DTO
     * @param pageSize          行数
     * @param pageNum           页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentGalleryDO>
     */
    DubboPageResult<EsCommentGalleryDO> getCommentGalleryList(EsCommentGalleryDTO commentGalleryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentGalleryDO>
     */
    DubboResult deleteCommentGallery(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param commentId 评论commentId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentGalleryDO>
     */
    DubboPageResult<EsCommentGalleryDO> getCommentGalleryList(Long commentId);

    /**
     * 根据查询条件查询图片列表
     *
     * @param commentId 评论commentId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentGalleryDO>
     */
    DubboPageResult<String> getCommentImageList(Long commentId);


}
