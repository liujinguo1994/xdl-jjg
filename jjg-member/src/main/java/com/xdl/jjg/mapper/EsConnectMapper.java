package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsConnect;
import com.xdl.jjg.model.dto.EsConnectDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 信任登录 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsConnectMapper extends BaseMapper<EsConnect> {

    /**
     * 解绑
     * @param esConnectDTO
     */
    void unbindWechaAndQQ(@Param("es") EsConnectDTO esConnectDTO);

}
