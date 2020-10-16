package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsTagsDO;
import com.jjg.shop.model.dto.EsTagsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsTagsService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param tagsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    DubboResult<EsTagsDO> insertTags(EsTagsDTO tagsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param tagsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    DubboResult<EsTagsDO> updateTags(EsTagsDTO tagsDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    DubboResult<EsTagsDO> getTags(Long id);

    /**
     * 获取商家标签列表
     * @param shopId
     * @return
     */
    DubboPageResult<EsTagsDO> getTagsList(Long shopId);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsTagsDO>
     */
    DubboResult<EsTagsDO> deleteTags(Long id);
}
