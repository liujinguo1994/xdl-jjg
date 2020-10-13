package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMyFootprintDO;
import com.xdl.jjg.model.dto.EsMyFootprintDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  会员活跃信息服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 17:14:43
 */
public interface IEsMyFootprintService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param myFootprintDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult<EsMyFootprintDO> insertMyFootprint(EsMyFootprintDTO myFootprintDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param myFootprintDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult updateMyFootprint(EsMyFootprintDTO myFootprintDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult<EsMyFootprintDO> getMyFootprint(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMyFootprintDO>
     */
    DubboPageResult<EsMyFootprintDO> getMyFootprintPage(int pageSize, int pageNum, Long memberId);

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

    /**
     * 根据主键删除数据
     * @auther: yuanj
     * @date: 2020/01/09 16:40:44
     * @param memberId
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult clearMyFoot(Long memberId);

    /**
     * 批量删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2020/01/09 16:40:44
     * @param ids    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    DubboResult deleteMyFoot(Integer[] ids);

}
