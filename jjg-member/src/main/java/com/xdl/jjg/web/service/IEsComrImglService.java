package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsComrImglDO;
import com.jjg.member.model.dto.EsComrImglDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 投诉举报图片 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31
 */
public interface IEsComrImglService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:39:30
     * @param esComrImglDTO    店员DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComrImglDO>
     */
    DubboResult insertComr(EsComrImglDTO esComrImglDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 16:40:10
     * @param esComrImglDTO    店员DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComrImglDO>
     */
    DubboResult updateComr(EsComrImglDTO esComrImglDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComrImglDO>
     */
    DubboResult<EsComrImglDO> getComr(Long id);


    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:37:16
     * @param comId  投诉举报主键
     * @param type   类型
     * @return: com.shopx.common.model.result.DubboPageResult<EsComrImglDO>
     */
    DubboPageResult<EsComrImglDO> getComrList(Long comId, Integer type);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:40:44
     * @param id   主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComrImglDO>
     */
    DubboResult deleteComrBuyer(Long id, Integer type);

    DubboResult deleteComr(Integer[] ids, Integer type);

}
