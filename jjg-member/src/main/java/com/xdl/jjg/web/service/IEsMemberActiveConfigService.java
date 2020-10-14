package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsMemberActiveConfigDO;
import com.jjg.member.model.dto.EsActiveMemberDTO;
import com.jjg.member.model.dto.EsMemberActiveConfigDTO;
import com.jjg.member.model.dto.EsQueryConditionActiveInfoDTO;
import com.xdl.jjg.model.domain.EsSellerMemberActiveConfigDO;
import com.xdl.jjg.model.domain.EsSellerMemberInFoDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  会员活跃度配置类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:01
 */
public interface IEsMemberActiveConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param esMemberActiveDTOs    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    DubboResult insertMemberActiveConfig(List<EsActiveMemberDTO> esMemberActiveDTOs, Long userId);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberActiveConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    DubboResult updateMemberActiveConfig(EsMemberActiveConfigDTO memberActiveConfigDTO, Long id);

    /**
     * 根据memberType获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    DubboResult<EsMemberActiveConfigDO> getMemberActiveConfig(Integer memberType, Long userId);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberActiveConfigDO>
     */
    DubboPageResult<EsSellerMemberActiveConfigDO> getMemberActiveConfigList(Long shopId);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    DubboResult deleteMemberActiveConfig(Long id);

    /**
     * 查询会员列表
      * @param userId
     * @return
     */
    DubboPageResult<EsSellerMemberInFoDO> getMemberInfoByUserId(int page, int pageSize, EsQueryConditionActiveInfoDTO es);
}
