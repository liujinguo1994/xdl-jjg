package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsGoodsWordsDO;
import com.jjg.shop.model.dto.EsGoodsWordsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 自定义分词 服务类
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:19
 */
public interface IEsGoodsWordsService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsWordsDTO    自定义分词DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsWordsDO>
     */
    DubboResult insertGoodsWords(EsGoodsWordsDTO goodsWordsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsWordsDTO   自定义分词DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsWordsDO>
     */
    DubboResult updateGoodsWords(EsGoodsWordsDTO goodsWordsDTO, Long id);


    /**
     * 根据分词名称删除
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param words    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsWordsDO>
     */
    DubboResult deleteGoodsWords(List<String> words);

    /**
     * 首页联想
     * @param keyword
     * @return
     */
    DubboPageResult<EsGoodsWordsDO> getGoodsWords(String keyword);

    /**
     * 清空分词
     * @return
     */
    DubboResult<EsGoodsWordsDO> deleteGoodsWords();
}
