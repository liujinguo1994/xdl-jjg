package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsAdminMemberDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.domain.EsSellerMemberAdminDO;
import com.jjg.member.model.dto.EsAdminDepositDTO;
import com.jjg.member.model.dto.EsQueryAdminMemberDTO;
import com.jjg.member.model.vo.EsExportMemberVO;
import com.xdl.jjg.entity.EsMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
public interface EsMemberMapper extends BaseMapper<EsMember> {

    /**
     * 依据手机号查询会员信息
     *
     * @param esMember
     * @return
     */
    Long selectMobileById(EsMember esMember);

    /**
     * 修改绑定手机号
     */
    int updateByMobile(EsMember esMember);

    /**
     * 根据主键id查询会员信息
     */
    EsMemberDO selectById(Long id);

    /**
     * 依据手机号或者姓名查询会员信息
     *
     * @return
     */
    EsMemberDO selectByNameOrMobile(String nameOrMobile);

    /**
     * 依据手机号查询会员信息
     *
     * @return
     */
    EsMemberDO selectByMobile(String mobile);

    /**
     * 依据姓名查询会员信息
     *
     * @return
     */
    EsMemberDO selectByName(String name);

    /**
     * 查询后台会员列表
     *
     * @return
     */
    IPage<EsAdminMemberDO> selectAdminMemberList(Page page, @Param("esName") EsQueryAdminMemberDTO esQueryAdminMemberDTO);

    /**
     * 查询所有用户姓名
     *
     * @return
     */
    List<String> getNameExistListInfo();

    /**
     * 查询所有用手机号
     *
     * @return
     */
    List<String> getMobileExistList();

    /**
     * 批量插入会员
     * @param list
     */
    void insertMemberBatch(@Param("lists") List<EsAdminDepositDTO> list);

    /**
     * 批量修改会员信息
     * @param list
     */
    void updateMemberBatch(@Param("lists") List<EsAdminDepositDTO> list);

    void updateMemberBalance(@Param("es") EsAdminDepositDTO esAdminDepositDTO);

    EsSellerMemberAdminDO getSellerAdmin(@Param("memberId") Long memberId, @Param("shopId") Long shopId);

    void updateBalanceByMobile(@Param("memberDO") EsMemberDO memberDO);

    List<EsExportMemberVO> selectExportMemberList(@Param("dto") EsQueryAdminMemberDTO dto);
}
