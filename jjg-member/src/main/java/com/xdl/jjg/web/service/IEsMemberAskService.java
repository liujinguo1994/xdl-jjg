package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsMemberAskDO;
import com.jjg.member.model.dto.EsMemberAskDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 咨询(手机端做) 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberAskService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberAskDTO    咨询(手机端做)DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    DubboResult insertMemberAsk(EsMemberAskDTO memberAskDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberAskDTO    咨询(手机端做)DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    DubboResult updateMemberAsk(EsMemberAskDTO memberAskDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    DubboResult<EsMemberAskDO> getMemberAsk(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberAskDTO  咨询(手机端做)DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAskDO>
     */
    DubboPageResult<EsMemberAskDO> getMemberAskList(EsMemberAskDTO memberAskDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAskDO>
     */
    DubboResult deleteMemberAsk(Long id);
}
