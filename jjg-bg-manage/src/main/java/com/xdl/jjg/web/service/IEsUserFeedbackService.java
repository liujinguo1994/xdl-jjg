package com.xdl.jjg.web.service;


import com.xdl.jjg.model.dto.EsUserFeedbackDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-06-03
 */
public interface IEsUserFeedbackService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param userFeedbackDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    DubboResult insertUserFeedback(EsUserFeedbackDTO userFeedbackDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param userFeedbackDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    DubboResult updateUserFeedback(EsUserFeedbackDTO userFeedbackDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    DubboResult getUserFeedback(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param userFeedbackDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsUserFeedbackDO>
     */
    DubboPageResult getUserFeedbackList(EsUserFeedbackDTO userFeedbackDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    DubboResult deleteUserFeedback(Long id);
}
