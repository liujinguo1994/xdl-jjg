package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsAddCommentPictureDO;
import com.jjg.member.model.dto.EsAddCommentPictureDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  追加评论图片 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:43
 */
public interface IEsAddCommentPictureService {

    /**
     * 插入数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @param addCommentPictureDTO     追加评论图片DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    DubboResult insertAddCommentPicture(EsAddCommentPictureDTO addCommentPictureDTO);

    /**
     * 根据条件更新更新数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @param addCommentPictureDTO    追加评论图片DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    DubboResult updateAddCommentPicture(EsAddCommentPictureDTO addCommentPictureDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    DubboResult<EsAddCommentPictureDO> getAddCommentPicture(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @param addCommentPictureDTO   追加评论图片DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsAddCommentPictureDO>
     */
    DubboPageResult<EsAddCommentPictureDO> getAddCommentPictureList(EsAddCommentPictureDTO addCommentPictureDTO, int pageSize, int pageNum);
    DubboPageResult<EsAddCommentPictureDO> getAddCommentPictureList(Long commentId);
    /**
     * 根据主键删除数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    DubboResult deleteAddCommentPicture(Long id);

    DubboResult addPictureBatch(Long addCommentId, List<String> list);
}
