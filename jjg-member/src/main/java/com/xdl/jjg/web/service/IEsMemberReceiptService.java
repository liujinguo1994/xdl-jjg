package com.xdl.jjg.web.service;


import com.jjg.member.model.dto.EsMemberReceiptDTO;
import com.xdl.jjg.model.domain.EsMemberReceiptDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 会员发票信息 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberReceiptService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberReceiptDTO    会员发票信息DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    DubboResult<Long> insertMemberReceipt(EsMemberReceiptDTO memberReceiptDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberReceiptDTO    会员发票信息DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    DubboResult updateMemberReceipt(EsMemberReceiptDTO memberReceiptDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    DubboResult<EsMemberReceiptDO> getMemberReceipt(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberReceiptDTO  会员发票信息DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberReceiptDO>
     */
    DubboPageResult<EsMemberReceiptDO> getMemberReceiptList(EsMemberReceiptDTO memberReceiptDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberReceiptDO>
     */
    DubboResult deleteMemberReceipt(Long id);
}
