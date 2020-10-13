package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMemberQueryActiveDO;
import com.xdl.jjg.model.dto.EsQueryMemberTypeDTO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 卖家查询后台会员信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsSellerQueryMemberSerice {

    /**
     * 卖家端后台条件查询列表
     *
     * @param esQueryMemberTypeDTO 会员信息DTO
     * @param pageSize          行数
     * @param pageNum           页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSellerQueryMemberDO>
     */
    DubboResult<EsMemberQueryActiveDO> getMemberInfoBySeller(EsQueryMemberTypeDTO esQueryMemberTypeDTO, int pageSize, int pageNum);


}
