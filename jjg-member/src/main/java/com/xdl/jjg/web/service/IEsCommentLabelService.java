package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCommentLabelDO;
import com.jjg.member.model.dto.EsCommentLabelDTO;
import com.xdl.jjg.model.domain.EsTagLabelListDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
public interface IEsCommentLabelService {

    /**
     * 插入数据
     *
     * @param commentLabelDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    DubboResult insertCommentLabel(EsCommentLabelDTO commentLabelDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param commentLabelDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    DubboResult updateCommentLabel(EsCommentLabelDTO commentLabelDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    DubboResult<EsCommentLabelDO> getCommentLabel(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param commentLabelDTO DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentLabelDO>
     */
    DubboPageResult<EsCommentLabelDO> getCommentLabelList(EsCommentLabelDTO commentLabelDTO, int pageSize, int pageNum);

    /**
     * 查询所有标签列表和分类
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentLabelDO>
     */
    DubboResult<EsTagLabelListDO> getCommentLabes(Long categoryId);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    DubboResult deleteCommentLabel(Long id);

    /**
     * 批量删除标签内容
     *
     * @param ids 主键ids
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult deleteEsCommentLabelBatch(Integer[] ids);

}
