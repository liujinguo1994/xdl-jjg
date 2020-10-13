package com.xdl.jjg.web.service;


import com.xdl.jjg.model.vo.EsWaybillVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsWaybillService {

    /**
     * 根据条件更新更新数据
     */
    DubboResult updateWaybill(EsWaybillVO waybillVO);

    /**
     * 查询电子面单列表
     */
    DubboPageResult<EsWaybillVO> getWaybillList();

    /*
     *开启某个电子面单
     */
    DubboResult openWaybill(String bean);
}
