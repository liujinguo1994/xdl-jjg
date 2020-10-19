package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsAdminMemberDO;
import com.jjg.member.model.domain.EsMemberAdminDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.jjg.member.model.dto.EsQueryAdminMemberDTO;
import com.jjg.member.model.vo.EsExportMemberVO;
import com.jjg.member.model.vo.EsImportBalanceVO;
import com.jjg.member.model.vo.EsImportMemberVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberService {

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
    @GetMapping("/getAdminMemberList")
    DubboPageResult<EsAdminMemberDO> getAdminMemberList(@RequestBody EsQueryAdminMemberDTO esQueryAdminMemberDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 后台新增会员
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @PostMapping("/insertAdminMember")
    DubboResult<EsMemberDO> insertAdminMember(@RequestBody EsMemberDTO memberDTO);

    /**
     * 修改信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @PostMapping("/updateMemberInfo")
    DubboResult updateMemberInfo(@RequestBody EsMemberDTO memberDTO);

    /**
     * 修改会员状态
     *
     * @param memberId 会员id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @PostMapping("/updateMemberState")
    DubboResult updateMemberState(@RequestParam("memberId") Long memberId,@RequestParam("state") Integer state);

    /**
     * 后台根据id获取数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @GetMapping("/getAdminMember")
    DubboResult<EsMemberDO> getAdminMember(@RequestParam("id") Long id);

    //批量导入会员
    @PostMapping("/importMember")
    DubboResult<EsImportMemberVO> importMember(@RequestBody byte[] base64);
    //导出会员
    @PostMapping("/exportMember")
    DubboPageResult<EsExportMemberVO> exportMember(@RequestBody EsQueryAdminMemberDTO dto);
    //批量调整会员余额
    @PostMapping("/importBalance")
    DubboResult<EsImportBalanceVO> importBalance(@RequestBody byte[] base64);

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
    @GetMapping("/getMemberList")
    DubboPageResult<EsMemberAdminDO> getMemberList(@RequestBody EsMemberDTO memberDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

}
