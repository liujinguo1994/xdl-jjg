package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsMemberRfmConfig;

/**
 * <p>
 *  RFMMapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:55
 */
public interface EsMemberRfmConfigMapper extends BaseMapper<EsMemberRfmConfig> {

    /**
     * 删除rfm配置表所有数据
     */
    void deleteRfmList();

}
