package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsAutoCommentConfig;

/**
 * <p>
 *  系统自动评论配置Mapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:40
 */
public interface EsAutoCommentConfigMapper extends BaseMapper<EsAutoCommentConfig> {

    /**
     * 删除自动评论表内容
     */
     void delete();

}
