package com.xdl.jjg.web.service;


import com.xdl.jjg.model.dto.EsSearchKeyWordDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-10-26
 */
public interface IEsSearchKeyWordService {

    /**
     * 会员搜索历史数据添加
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param searchKeyWordDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    DubboResult insertSearchKeyWord(EsSearchKeyWordDTO searchKeyWordDTO);

    /**
     * 商城首页 根据会员查询该会员的搜索历史
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSearchKeyWordDO>
     */
    DubboPageResult getSearchKeyWordList(Long memberId);

    /**
     * 购物车页面根据会员ID 获取该会员的前三条 查询历史
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSearchKeyWordDO>
     */
    DubboPageResult getSearchKeyWord(Long memberId);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    DubboResult deleteSearchKeyWord(Long id);

    /**
     * 根据会员ID 清空所有搜索历史
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param memberId    memberId
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    DubboResult deleteSearchKeyWordBatch(Long memberId);
}
