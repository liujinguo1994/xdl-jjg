package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMyFootprintDO;
import com.jjg.member.model.dto.EsMyFootprintDTO;
import com.xdl.jjg.response.service.DubboResult;

public interface MyFootprintService {



    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMyFootprintDO>
     */
    DubboResult<EsMyFootprintDO> getMyFootprintList(EsMyFootprintDTO myFootprintDTO);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param memberId
     * @param viewTime
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult deleteMyFoot(Long memberId, String viewTime);

    /**
     * 根据店铺id查询
     * @auther: xiaoLin
     * @date: 2019/10/21 16:40:44
     * @param memberId
     * @param shopId
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult getTopMyFoot(Long memberId, Long shopId);

    /**
     * 根据主键删除数据
     * @auther: xiaoLin
     * @date: 2019/10/21 16:40:44
     * @param id
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult deleteMyFootById(Long id);


}
