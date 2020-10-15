package com.xdl.jjg.web.service;

import com.jjg.member.model.domain.*;
import com.jjg.member.model.dto.*;
import com.jjg.member.model.vo.EsExportMemberVO;
import com.jjg.member.model.vo.EsImportBalanceVO;
import com.jjg.member.model.vo.EsImportMemberVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsMemberService {

    /**
     * 会员注册
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> insertMember(EsMemberDTO memberDTO);

    /**
     * 买家端添加会员
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
     DubboResult insertSellerMember(EsSellerMemberAdminDTO esSellerMemberAdminDTO);

    /**
     * 查询卖家端添加会员信息
     *
     * @param esSellerMemberAdminDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsSellerMemberAdminDO> getSellerMember(Long memberId, Long shopId);


    /**
     * 后台新增会员
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> insertAdminMember(EsMemberDTO memberDTO);

    /**
     * 修改最后登陆时间
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> updateMemberLastLoginTime(EsMemberDTO memberDTO);

    /**
     * 修改最后登陆时间
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateSellerMember(EsSellerMemberAdminDTO esSellerMemberAdminDTO);

    /**
     * 修改会员个人信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> updateMember(EsMemberDTO memberDTO);


    /**
     * 修改密码
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberPass(EsMemberDTO memberDTO);

    /**
     * 修改信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberInfo(EsMemberDTO memberDTO);

    /**
     * 修改会员余额
     *
     * @param memberBalanceDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberBalance(EsMemberBalanceDTO memberBalanceDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMember(Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberById(Long id);


    /**
     * 后台根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getAdminMember(Long id);

    /**
     * 判断输入的旧密码是否正确
     *
     * @param passWord 密码
     * @param userId   用户名
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberByPassWord(String passWord, Long userId);

    /**
     * 后台-根据查询条件查询列表
     *
     * @param memberDTO 会员表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboPageResult<EsMemberAdminDO> getMemberList(EsMemberDTO memberDTO, int pageSize, int pageNum);


    /**
     * 根据查询条件查询列表
     *
     * @param esQueryAdminMemberDTO 会员表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboPageResult<EsAdminMemberDO> getAdminMemberList(EsQueryAdminMemberDTO esQueryAdminMemberDTO, int pageSize, int pageNum);


    /**
     * 根据查询列表查询所有会员
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboPageResult<EsMemberDO> getMemberListNoPage();

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult deleteMember(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param memberDTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberExistList(EsMemberDTO memberDTO);

    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param nameAndMobile
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberInfoByNameOrMobile(String nameAndMobile);

    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param mobile
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberInfoByMobile(String mobile);

    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param name
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberInfoByName(String name);

    /**
     * 判断会员id是否存在
     *
     * @param id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> getMemberByIdInfo(Long id);


    /**
     * 更改绑定手机号
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> updateByMobile(EsMemberDTO memberDTO);

    /**
     * 修改会员个人信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult<EsMemberDO> updateMemberByInfo(EsMemberDTO memberDTO);

    /**
     * 获取已注册所有用户姓名
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    DubboPageResult<String> getAllNameExistList();

    /**
     * 获取已注册所有手机号
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboPageResult<String> getAllMobileExistList();

    /**
     * 批量新增会员操作
     * @return
     */
    DubboResult batchInsertMember(List<EsAdminDepositDTO> memberDTOList);

    /**
     * 批量修改会员
     * @return
     */
    DubboResult batchUpdateMemberDeposit(List<EsAdminDepositDTO> esAdminDepositDTOS);

    /**
     * 修改会员成长值
     * @param growthValue
     * @return
     */
    DubboResult updateGrowthValue(Long userId, Integer growthValue, Boolean judge, Integer type);

    /**
     * 统计会员资产
     * @param memberId
     * @return
     */
    DubboResult<EsMyMeansDO> meansCensus(Long memberId);

    /**
     * 修改会员状态
     *
     * @param memberId 会员id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    DubboResult updateMemberState(Long memberId, Integer state);

    //批量调整会员余额
    DubboResult<EsImportBalanceVO> importBalance(byte[] base64);

    //批量导入会员
    DubboResult<EsImportMemberVO> importMember(byte[] base64);

    //导出会员
    DubboPageResult<EsExportMemberVO> exportMember(EsQueryAdminMemberDTO dto);

    //小程序登出
    DubboResult quitLogin(String skey);

    //根据登录态标识查询会员信息
    DubboResult<EsMemberDO> getMemberBySkey(String skey);
}
