package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsSelfDate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 自提日期 Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsSelfDateMapper extends BaseMapper<EsSelfDate> {

    List<EsSelfDate> selectDateList(QueryWrapper<EsSelfDate> queryDateWrapper, @Param("deliveryId") Long deliveryId);
}
