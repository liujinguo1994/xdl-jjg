package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsBrandShowDO;
import com.jjg.shop.model.dto.EsBrandShowDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-12 13:23:46
 */
public interface IEsBrandShowService {

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param brandShowDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    DubboResult insertBrandShow(EsBrandShowDTO brandShowDTO);

    /**
     * 根据条件更新更新数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param brandShowDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    DubboResult updateBrandShow(EsBrandShowDTO brandShowDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    DubboResult<EsBrandShowDO> getBrandShow(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @param brandShowDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsBrandShowDO>
     */
    DubboPageResult<EsBrandShowDO> getBrandShowList(EsBrandShowDTO brandShowDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    DubboResult deleteBrandShow(Long id);
}
