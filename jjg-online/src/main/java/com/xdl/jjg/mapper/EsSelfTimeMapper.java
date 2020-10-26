package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsSelfTime;

/**
 * <p>
 * 自提时间 Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsSelfTimeMapper extends BaseMapper<EsSelfTime> {

    int selectPersonTotal(Long dateId);
}
