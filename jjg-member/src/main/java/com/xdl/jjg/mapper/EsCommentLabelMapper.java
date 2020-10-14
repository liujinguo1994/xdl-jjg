package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsCommentLabelDO;
import com.xdl.jjg.entity.EsCommentLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
public interface EsCommentLabelMapper extends BaseMapper<EsCommentLabel> {

    /**
     * 依据商品分类查询标签
     * @param categoryId
     * @return
     */
     List<EsCommentLabelDO> getAllTags(Long categoryId);

     List<String> getLabelsById(@Param("list") List<Integer> list);

}
