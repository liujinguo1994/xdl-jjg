package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsCompany;

import java.util.List;

/**
 * <p>
 * 签约公司 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsCompanyMapper extends BaseMapper<EsCompany> {

    /**
     * 查询单个公司信息
     * @param esCompany
     * @return
     */
    public EsCompany getCompanyInfo(EsCompany esCompany);

    /**
     * 查询所有公司列表信息
     * @return
     */
    public List<EsCompany> companyList();

}
