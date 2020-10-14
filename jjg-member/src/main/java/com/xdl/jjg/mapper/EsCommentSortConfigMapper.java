package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsCommentSortConfigDO;
import com.xdl.jjg.entity.EsCommentSortConfig;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-29 14:18:11
 */
public interface EsCommentSortConfigMapper extends BaseMapper<EsCommentSortConfig> {


    /**
     * 评论分类配置列表
     * @return
     */
    List<EsCommentSortConfigDO> getCommentSortConfigList();

    /**
     * 删除评分分类配置表信息
     */
    void deleteCommentSortConfig();

    /**
     * 查询评分等级详细
     * @return
     */
    EsCommentSortConfigDO getCommentSortLevel(Integer commentType);
}
