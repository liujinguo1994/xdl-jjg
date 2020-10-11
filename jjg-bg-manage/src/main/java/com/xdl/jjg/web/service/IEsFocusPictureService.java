package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsFocusPictureDO;
import com.xdl.jjg.model.dto.EsFocusPictureDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsFocusPictureService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param focusPictureDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    DubboResult<EsFocusPictureDO> insertFocusPicture(EsFocusPictureDTO focusPictureDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param focusPictureDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    DubboResult<EsFocusPictureDO> updateFocusPicture(EsFocusPictureDTO focusPictureDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    DubboResult<EsFocusPictureDO> getFocusPicture(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param focusPictureDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsFocusPictureDO>
     */
    DubboPageResult<EsFocusPictureDO> getFocusPictureList(EsFocusPictureDTO focusPictureDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    DubboResult deleteFocusPicture(Long id);

    //根据客户端类型查询轮播图列表
    DubboPageResult<EsFocusPictureDO> getList(String clientType);
}
