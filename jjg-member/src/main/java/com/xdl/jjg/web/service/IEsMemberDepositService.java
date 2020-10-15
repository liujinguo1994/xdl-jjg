package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsMemberDepositDO;
import com.jjg.member.model.dto.EsMemberBalanceDTO;
import com.jjg.member.model.dto.EsMemberDepositDTO;
import com.jjg.member.model.dto.EsQueryAdminMemberDepositDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 会员余额明细 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberDepositService {

    /**
     * 插入数据
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    DubboResult insertMemberDeposit(EsMemberDepositDTO memberDepositDTO);

    /**
     * 批量插入数据
     *
     * @param esMemberDepositDTOList 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    DubboResult insertMemberBatchDeposit(List<EsMemberDepositDTO> esMemberDepositDTOList);

    /**
     * 插入数据会员余额
     *
     * @param esMemberBalanceDTO 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    DubboResult insertMemberDepositBalance(EsMemberBalanceDTO esMemberBalanceDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    DubboResult updateMemberDeposit(EsMemberDepositDTO memberDepositDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    DubboResult<EsMemberDepositDO> getMemberDeposit(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         行数
     * @param pageNum          页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    DubboPageResult<EsMemberDepositDO> getMemberDepositList(EsMemberDepositDTO memberDepositDTO, int pageSize, int pageNum);


    /**
     * 根据查询会员余额明细列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
     DubboPageResult<EsMemberDepositDO> getAdminMemberDepositList(EsQueryAdminMemberDepositDTO memberDepositDTO, int pageSize, int pageNum);


    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDepositDO>
     */
    DubboResult deleteMemberDeposit(Long id);


    /**
     * 根据查询条件查询列表
     *
     * @param memberDepositDTO 会员余额明细DTO
     * @param pageSize         行数
     * @param pageNum          页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDepositDO>
     */
    DubboPageResult<EsMemberDepositDO> getWapMemberDepositList(EsMemberDepositDTO memberDepositDTO, int pageSize, int pageNum);
}
