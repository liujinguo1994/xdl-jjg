package com.xdl.jjg.web.service;


import com.jjg.member.model.dto.EsGradeWeightConfigDTO;
import com.xdl.jjg.model.domain.EsGradeWeightConfigDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:11
 */
public interface IEsGradeWeightConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param gradeWeightConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    DubboResult insertGradeWeightConfig(List<EsGradeWeightConfigDTO> gradeWeightConfigDTOList);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param gradeWeightConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    DubboResult updateGradeWeightConfig(List<EsGradeWeightConfigDTO> gradeWeightConfigDTOList);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    DubboResult<EsGradeWeightConfigDO> getGradeWeightConfig(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGradeWeightConfigDO>
     */
    DubboPageResult<EsGradeWeightConfigDO> getGradeWeightConfigList();

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGradeWeightConfigDO>
     */
    DubboResult deleteGradeWeightConfig(Long id);
}
