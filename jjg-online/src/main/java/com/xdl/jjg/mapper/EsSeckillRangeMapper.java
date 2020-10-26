package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.trade.model.domain.EsSeckillRangeDO;
import com.xdl.jjg.entity.EsSeckillRange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsSeckillRangeMapper extends BaseMapper<EsSeckillRange> {

    List<EsSeckillRangeDO> selectSeckillRangeList(@Param("today") long today);

}
