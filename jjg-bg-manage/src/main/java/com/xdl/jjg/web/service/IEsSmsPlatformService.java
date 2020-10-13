package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsSmsPlatformDO;
import com.xdl.jjg.model.vo.EsSmsPlatformVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类-短信网关设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsSmsPlatformService {

    //根据条件更新更新数据
    DubboResult updateSmsPlatform(EsSmsPlatformVO esSmsPlatformVO);

    //查询短信平台列表
    DubboPageResult<EsSmsPlatformVO> getSmsPlatformList(int pageSize, int pageNum);

    //开启某个短信网关
    DubboResult openPlatform(String bean);

    //获取开启的短信网关
    DubboResult<EsSmsPlatformDO> getOpen();
}
