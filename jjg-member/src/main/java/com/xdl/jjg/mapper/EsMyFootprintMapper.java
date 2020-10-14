package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsMyFootprintDO;
import com.xdl.jjg.entity.EsMyFootprint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  会员活跃信息Mapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:44
 */
public interface EsMyFootprintMapper extends BaseMapper<EsMyFootprint> {
    /**
     * 查询浏览记录
     * @param memberId
     * @return
     */
    List<EsMyFootprintDO> getFootLists(Long memberId);
    /**
     * 查询浏览记录分页查询
     * @param memberId
     * @return
     */
    IPage<EsMyFootprintDO> getFootListsPage(Page page, @Param("memberId") Long memberId);

    /**
     * 删除浏览记录
     * @param memberId
     * @param viewTime
     */
    void deleteMyFoot(@Param("memberId") Long memberId, @Param("viewTime") String viewTime);

    /**
     * 获取近10条浏览记录
     * @param memberId
     * @param shopId
     */
    List<EsMyFootprintDO> getTopMyFoot(@Param("memberId") Long memberId, @Param("shopId") Long shopId);

}
