package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsConnectSettingDO;
import com.xdl.jjg.model.dto.EsConnectSettingDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 第三方登录参数 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
public interface IEsConnectSettingService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 14:39:30
     * @param connectSettingDTO    第三方登录参数DTO
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    DubboResult insertConnectSetting(EsConnectSettingDTO connectSettingDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 14:40:10
     * @param connectSettingDTO    第三方登录参数DTO
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    DubboResult updateConnectSetting(EsConnectSettingDTO connectSettingDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/0/18 14:17:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    DubboResult<EsConnectSettingDO> getConnectSetting(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 13:48:53
     * @param connectSettingDTO  第三方登录参数DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsConnectSettingDO>
     */
    DubboPageResult<EsConnectSettingDO> getConnectSettingList(EsConnectSettingDTO connectSettingDTO, int pageSize, int pageNum);

    /**
     * 查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsConnectSettingDO>
     */
    DubboPageResult<EsConnectSettingDO> getList();

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsConnectSettingDO>
     */
    DubboResult deleteConnectSetting(Long id);
}
