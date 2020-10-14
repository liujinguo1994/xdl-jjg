package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCompanyDO;
import com.jjg.member.model.dto.EsCompanyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 签约公司 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-26
 */
public interface IEsCompanyService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:01:30
     * @param companyDTO    签约公司DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult insertCompany(EsCompanyDTO companyDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:11:33
     * @param companyDTO    签约公司DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult updateCompany(EsCompanyDTO companyDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:25:40
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult<EsCompanyDO> getCompany(Long id);

    /**
     * 根据公司名称查询公司
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:25:40
     * @param companyCode    公司标志符
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult<EsCompanyDO> getCompanyByCode(String companyCode);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:08:30
     * @param companyDTO  签约公司DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCompanyDO>
     */
    DubboPageResult<EsCompanyDO> getCompanyList(EsCompanyDTO companyDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:30:35
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    DubboResult deleteCompany(Integer[] id);
}
