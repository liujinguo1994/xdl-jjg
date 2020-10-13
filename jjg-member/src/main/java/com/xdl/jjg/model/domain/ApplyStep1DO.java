package com.xdl.jjg.model.domain;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 申请开店第一步
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-08
 */
@Data
@ToString
public class ApplyStep1DO {
	/**公司名称*/
    private String companyName;
	
    /**公司地址*/
    private String companyAddress;
	
    /**公司电话*/
    private String companyPhone;
	
    /**电子邮箱*/
    private String companyEmail;
    
    /**员工总数*/
    private Integer employeeNum;
    
    /**注册资金*/
    private Double regMoney;
    
    /**联系人姓名*/
    private String linkName;
    
    /**联系人电话*/
    private String linkPhone;

	/**申请开店进度*/
	private Integer step;


}
