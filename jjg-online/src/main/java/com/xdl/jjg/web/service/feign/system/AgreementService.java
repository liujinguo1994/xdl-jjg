package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsAgreementDO;
import com.xdl.jjg.response.service.DubboResult;

public interface AgreementService {


    //获取注册协议
    DubboResult<EsAgreementDO> getEsAgreement();
}
