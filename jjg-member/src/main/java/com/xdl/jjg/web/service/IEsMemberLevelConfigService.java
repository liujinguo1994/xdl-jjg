package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsMemberLevelConfigDO;
import com.jjg.member.model.dto.EsMemberLevelConfigDTO;
import com.jjg.member.model.dto.EsQueryMemberLevelConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  会员等级配置服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 09:42:02
 */
public interface IEsMemberLevelConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberLevelConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    DubboResult insertMemberLevelConfig(EsMemberLevelConfigDTO memberLevelConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberLevelConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    DubboResult updateMemberLevelConfig(EsMemberLevelConfigDTO memberLevelConfigDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    DubboResult<EsMemberLevelConfigDO> getMemberLevelConfig(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberLevelConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberLevelConfigDO>
     */
    DubboPageResult<EsMemberLevelConfigDO> getMemberLevelConfigList(EsQueryMemberLevelConfigDTO memberLevelConfigDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    DubboResult deleteMemberLevelConfig(Long id);

    /**
     * 根据成长值查询会员等级
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
     DubboResult<EsMemberLevelConfigDO> getMemberLevelByGrade(Integer grade);
}
