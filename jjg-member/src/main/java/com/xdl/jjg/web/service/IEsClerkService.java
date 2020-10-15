package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsClerkDO;
import com.jjg.member.model.domain.EsClerkListDO;
import com.jjg.member.model.dto.ClerkQueryParamDTO;
import com.jjg.member.model.dto.EsClerkDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店员 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-28
 */
public interface IEsClerkService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 9:39:30
     * @param clerkDTO    店员DTO
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    DubboResult insertClerk(EsClerkDTO clerkDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param clerkDTO    店员DTO
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    DubboResult updateClerk(EsClerkDTO clerkDTO);

    /**
     * 根据条件更新会员表，店员表数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param clerkDTO    店员DTO
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    DubboResult updateSellerClerk(EsClerkDTO clerkDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 10:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    DubboResult<EsClerkDO> getClerk(Long id);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/10 16:20:16
     * @param mmeberId    会员id
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    DubboResult<EsClerkDO> getByMemberId(Long mmeberId);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 10:37:16
     * @param clerkQueryParamDTO  店员DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsClerkDO>
     */
    DubboPageResult<EsClerkListDO> getClerkList(ClerkQueryParamDTO clerkQueryParamDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 10:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    DubboResult deleteClerk(Long id, Long shopId);

    /**
     * 恢复店员
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 10:30:44
     * @param id    主键id
     * @return:
     */
    DubboResult recovery(Long id, Long shopId);
}
