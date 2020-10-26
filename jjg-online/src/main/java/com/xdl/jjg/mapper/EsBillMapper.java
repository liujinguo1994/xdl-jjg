package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 结算单-es_bill Mapper 接口
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Repository
public interface EsBillMapper extends BaseMapper<EsBill> {

    /**
     * 汇总总结算单
     *
     *
     * @param type      类型
     * @param startTime 结算开始时间
     * @param endTime   结算结束时间
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 13:38:55
     * @return: void
     */
    EsBill summary(@Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 插入结算单
     *
     * @param bill 结算单信息
     * @author: libw 981087977@qq.com
     * @date: 2019/08/29 13:55:48
     * @return: int
     */
    @Override
    int insert(EsBill bill);

    /**
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/09/05 15:10:01
     * @param type
     * @param startTime
     * @param endTime
     * @return: com.shopx.trade.dao.entity.EsBill
     */
    EsBill summaryForSupplier(Integer type, String startTime, String endTime);
}
