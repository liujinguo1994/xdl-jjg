package com.xdl.jjg.web.service;


import com.jjg.member.model.dto.EsConnectDTO;
import com.xdl.jjg.model.domain.EsConnectDO;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.Map;

/**
 * <p>
 * 信任登录 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
public interface IEsConnectService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:39:30
     * @param connectDTO    信任登录DTO
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult insertConnect(EsConnectDTO connectDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:10
     * @param connectDTO    信任登录DTO
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult updateConnect(EsConnectDTO connectDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult<EsConnectDO> getConnect(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 13:42:53
     * @param connectDTO  信任登录DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsConnectDO>
     */
    DubboPageResult<EsConnectDO> getConnectList(EsConnectDTO connectDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult deleteConnect(Long id);

    /**
     * 根据条件解绑
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:10
     * @param type    信任登录DTO
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult unbind(String type, Long memberId);


    /**
     * 微信授权回调
     *@auther: yuanj 595831329@qq.com
     *@date: 2019/07/18 10:40:44
   * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult wechatAuthCallBack();

    /**
     * 微信绑定登录
     *
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 11:10:44
     * @param uuid 客户端唯一标示
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    Map bindLogin(String uuid);


    /**
     * 微信退出解绑操作
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 11:17:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult wechatOut();


    /**
     * 发起信任登录
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 11:17:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult initiate(String type, String port, String member);


    /**
     * 发起信任登录
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 11:17:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    DubboResult<EsMemberDO> callBack(String type, String port, String member, String uuid);


}
