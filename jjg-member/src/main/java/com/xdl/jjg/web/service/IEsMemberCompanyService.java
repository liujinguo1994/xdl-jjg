package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberCompanyDO;
import com.xdl.jjg.model.dto.EsMemberCompanyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberCompanyService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCompanyDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    DubboResult insertMemberCompany(EsMemberCompanyDTO memberCompanyDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberCompanyDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    DubboResult updateMemberCompany(EsMemberCompanyDTO memberCompanyDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    DubboResult<EsMemberCompanyDO> getMemberCompany(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberCompanyDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCompanyDO>
     */
    DubboPageResult<EsMemberCompanyDO> getMemberCompanyList(EsMemberCompanyDTO memberCompanyDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCompanyDO>
     */
    DubboResult deleteMemberCompany(Long id);
}
