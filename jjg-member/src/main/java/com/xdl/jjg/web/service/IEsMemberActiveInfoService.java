package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberActiveInfoDO;
import com.xdl.jjg.model.dto.EsMemberActiveInfoDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  会员活跃信息服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:43
 */
public interface IEsMemberActiveInfoService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberActiveInfoDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    DubboResult insertMemberActiveInfo(List<EsMemberActiveInfoDTO> memberActiveInfoDTOs);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberActiveInfoDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    DubboResult updateMemberActiveInfo(EsMemberActiveInfoDTO memberActiveInfoDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    DubboResult<EsMemberActiveInfoDO> getMemberActiveInfo(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberActiveInfoDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberActiveInfoDO>
     */
    DubboPageResult<EsMemberActiveInfoDO> getMemberActiveInfoList(EsMemberActiveInfoDTO memberActiveInfoDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveInfoDO>
     */
    DubboResult deleteMemberActiveInfo(List<String> orders);
}
