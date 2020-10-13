package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsMemberActiveConfig;
import com.xdl.jjg.model.domain.EsSellerMemberActiveConfigDO;
import com.xdl.jjg.model.domain.EsSellerMemberInFoDO;
import com.xdl.jjg.model.dto.EsQueryMemberActiveInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 卖家端会员信息 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:02
 */
public interface EsMemberActiveConfigMapper extends BaseMapper<EsMemberActiveConfig> {
    /**
     * 删除会员活跃度配置表
     */
    void deleteMemberActiveConfig(Long shopId);

    /**
     * 查询配置信息
     * @return
     */
    List<EsSellerMemberActiveConfigDO> getListInfo(@Param("shopId") Long shopId);

    /**
     * 查询卖家端会员信息
     *
     * @param page
     * @param esQueryMemberActiveInfoDTO
     * @return
     */
    IPage<EsSellerMemberInFoDO> getMemberInfoByUserId(Page page, @Param("es") EsQueryMemberActiveInfoDTO esQueryMemberActiveInfoDTO);

    /**
     * 查询卖家会员信息当卖家未设置时候
     * @param page
     * @param esQueryMemberActiveInfoDTO
     * @return
     */
    IPage<EsSellerMemberInFoDO>  getCommenMemberInfoByUserId(Page page, @Param("es") EsQueryMemberActiveInfoDTO esQueryMemberActiveInfoDTO);
}
