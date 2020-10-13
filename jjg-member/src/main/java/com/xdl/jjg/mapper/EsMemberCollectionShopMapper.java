package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsMemberCollectionShop;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员收藏店铺表 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberCollectionShopMapper extends BaseMapper<EsMemberCollectionShop> {

    /**
     * 查询排序最大值
     * @return
     */
    Integer getMaxSort();

    /**
     * 查询店铺当天收藏次数
     * @param memberId
     * @param timesNow
     * @return
     */
    int getCollectShopNum(@Param("memberId") Long memberId, @Param("timesNow") String timesNow);

}
