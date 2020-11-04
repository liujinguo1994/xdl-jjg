package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberAddressDO;
import com.jjg.member.model.dto.EsMemberAddressDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface MemberAddressService {


    /**
     * 根据会员id查询列表
     *
     * @param memberId 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    DubboPageResult<EsMemberAddressDO> getMemberAddressListByMemberId(@RequestParam("memberId") Long memberId);

    /**
     * 查询默认收货地址
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> getDefaultMemberAddress(@RequestParam("memberId") Long memberId);
    /**
     * 根据条件更新更新数据
     *
     * @param memberAddressDTO 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> updateMemberAddress(@RequestBody EsMemberAddressDTO memberAddressDTO);

    /**
     * 插入数据
     *
     * @param memberAddressDTO 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> insertMemberAddress(@RequestBody EsMemberAddressDTO memberAddressDTO);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult deleteMemberAddress(@RequestParam("id") Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> getMemberAddress(@RequestParam("id") Long id);

    /**
     * 设置默认收货地址
     *
     * @auther: xl 1220316142@qq.com
     * @date: 2019/12/11 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    DubboResult<EsMemberAddressDO> setDefaultMemberAddress(@RequestParam("id") Long id,@RequestParam("memberId")  Long memberId);


    /**
     * 根据查询条件查询列表
     *
     * @param memberAddressDTO 收货地址表DTO
     * @param pageSize         行数
     * @param pageNum          页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    DubboPageResult<EsMemberAddressDO> getMemberAddressList(@RequestBody EsMemberAddressDTO memberAddressDTO,@RequestParam("pageSize")  int pageSize,@RequestParam("pageNum")  int pageNum);


    /**
     * 根据查询条件查询列表
     *
     * @param esMemberAddressDTO 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    DubboPageResult<EsMemberAddressDO> getMemberAddressLists(@RequestBody EsMemberAddressDTO esMemberAddressDTO);
}
