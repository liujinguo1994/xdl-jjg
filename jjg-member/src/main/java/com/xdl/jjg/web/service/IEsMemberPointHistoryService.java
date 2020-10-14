package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsMemberPointHistoryDO;
import com.jjg.member.model.dto.EsMemberPointHistoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 会员积分明细 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberPointHistoryService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberPointHistoryDTO    会员积分明细DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboResult insertMemberPointHistory(EsMemberPointHistoryDTO memberPointHistoryDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberPointHistoryDTO    会员积分明细DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboResult updateMemberPointHistory(EsMemberPointHistoryDTO memberPointHistoryDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboResult<EsMemberPointHistoryDO> getMemberPointHistory(Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param memberId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryDetail(Long memberId, Integer gradePointType, int pageSize, int pageNum);

    /**
     * 后台-根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberPointHistoryDTO  会员积分明细DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberPointHistoryDO>
     */
    DubboPageResult<EsMemberPointHistoryDO> getMemberPointHistoryList(EsMemberPointHistoryDTO memberPointHistoryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboResult deleteMemberPointHistory(Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param memberId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberPointHistoryDO>
     */
    DubboPageResult<EsMemberPointHistoryDO> pointStatistics(Long memberId);
}
