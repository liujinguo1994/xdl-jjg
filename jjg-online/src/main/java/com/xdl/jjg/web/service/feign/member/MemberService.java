package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.domain.EsMyMeansDO;
import com.jjg.member.model.dto.EsMemberBalanceDTO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberService {




    /**
     * 修改最后登陆时间
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> updateMemberLastLoginTime(@RequestBody EsMemberDTO memberDTO);
    /**
     * 会员注册
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> insertMember(@RequestBody EsMemberDTO memberDTO);
    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param name
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberInfoByName(@RequestParam("name") String name);

    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param mobile
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberInfoByMobile(@RequestParam("mobile") String mobile);
    /**
     * 统计会员资产
     * @param memberId
     * @return
     */
    DubboResult<EsMyMeansDO> meansCensus(@RequestParam("memberId") Long memberId);

    /**
     * 后台根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getAdminMember(@RequestParam("id") Long id);
    /**
     * 修改密码
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberPass(@RequestBody EsMemberDTO memberDTO);
    /**
     * 判断输入的旧密码是否正确
     *
     * @param passWord 密码
     * @param userId   用户名
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberByPassWord(@RequestParam("passWord") String passWord,@RequestParam("userId")  Long userId);

    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param nameAndMobile
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberInfoByNameOrMobile(@RequestParam("nameAndMobile") String nameAndMobile);
    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMember(@RequestParam("id") Long id);


    //根据登录态标识查询会员信息
    DubboResult<EsMemberDO> getMemberBySkey(@RequestParam("skey") String skey);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberById(@RequestParam("id") Long id);


    /**
     * 修改信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberInfo(@RequestBody EsMemberDTO memberDTO);

    /**
     * 修改会员余额
     *
     * @param memberBalanceDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberBalance(@RequestBody EsMemberBalanceDTO memberBalanceDTO);

}
