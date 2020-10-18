package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsParametersDO;
import com.jjg.shop.model.dto.EsParametersDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface ParametersService {

    /**
     *
     * @param id 参数主键ID
     * @param sortType 移动类型 up 上移 down 下移
     * @return
     */
    @PostMapping("/sortParameters")
    DubboResult<EsParametersDO> sortParameters(@RequestParam("id") Long id,@RequestParam("sortType") String sortType);
    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @DeleteMapping("/deleteParameters")
    DubboResult<EsParametersDO> deleteParameters(@RequestParam("id") Long id);
    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parametersDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @PostMapping("/insertParameters")
    DubboResult<EsParametersDO> insertParameters(@RequestBody EsParametersDTO parametersDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param parametersDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @PostMapping("/updateParameters")
    DubboResult<EsParametersDO> updateParameters(@RequestBody EsParametersDTO parametersDTO, @RequestParam("id") Long id);


}
