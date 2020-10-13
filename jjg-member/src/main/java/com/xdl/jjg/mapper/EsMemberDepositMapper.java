package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsMemberDeposit;
import com.xdl.jjg.model.domain.EsMemberDepositDO;
import com.xdl.jjg.model.dto.EsAdminDepositDTO;
import com.xdl.jjg.model.dto.EsMemberDepositDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员余额明细 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberDepositMapper extends BaseMapper<EsMemberDeposit> {

    /**
     * 查询会员余额明细
     *
     * @param page
     * @param esMemberDepositDTO
     * @return
     */
    IPage<EsMemberDepositDO> getRecentlyThreeMonth(Page page, @Param("esMemberDepositDTO") EsMemberDepositDTO esMemberDepositDTO);

    /**
     * 批量插入会员消费明细
     * @param list
     */
    void insertMemberDepositBatch(@Param("lists") List<EsAdminDepositDTO> list);

    /**
     * 手机端查询会员余额明细
     *
     * @param page
     * @param esMemberDepositDTO
     * @return
     */
    IPage<EsMemberDepositDO> getWapBalance(Page page, @Param("esMemberDepositDTO") EsMemberDepositDTO esMemberDepositDTO);


}
