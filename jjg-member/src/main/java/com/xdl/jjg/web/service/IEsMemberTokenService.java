package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsMemberTokenDO;
import com.jjg.member.model.dto.EsMemberTokenDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-10 14:47:35
 */
public interface IEsMemberTokenService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberTokenDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult insertMemberToken(EsMemberTokenDTO memberTokenDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberTokenDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult updateMemberToken(EsMemberTokenDTO memberTokenDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult<EsMemberTokenDO> getMemberToken(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberTokenDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberTokenDO>
     */
    DubboPageResult<EsMemberTokenDO> getMemberTokenList(EsMemberTokenDTO memberTokenDTO, int pageSize, int pageNum);

    /**
     * 根据查询条件查询
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberTokenDTO  DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberTokenDO>
     */
    DubboResult<EsMemberTokenDO> getMemberTokenInfo(EsMemberTokenDTO memberTokenDTO);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberTokenDO>
     */
    DubboResult deleteMemberToken(Long id);
}
