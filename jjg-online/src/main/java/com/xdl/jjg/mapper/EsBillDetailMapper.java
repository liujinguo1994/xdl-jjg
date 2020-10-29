package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsBillDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 结算单 Mapper 接口
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Repository
public interface EsBillDetailMapper extends BaseMapper<EsBillDetail> {

    /**
     * 更新店铺结算单详情结算状态及店铺结算单id
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/08/29 14:41:03
     * @param shopBillId    店铺结算单id
     * @param idList        需要更新得数据
     * @return: void
     */
    void updateStatusForShop(@Param("shopBillId") Long shopBillId, @Param("idList") List<String> idList);

    /**
     * 更新签约公司结算单详情结算状态及签约公司结算单id
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/08/29 14:41:03
     * @param companyBillId     签约公司结算单id
     * @param idList            需要更新得数据
     * @return: void
     */
    void updateStatusForCompany(@Param("companyBillId") Long companyBillId, @Param("idList") List<String> idList);
}
