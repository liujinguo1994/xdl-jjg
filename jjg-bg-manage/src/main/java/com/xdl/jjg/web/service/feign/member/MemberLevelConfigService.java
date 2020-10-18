package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMemberLevelConfigDO;
import com.jjg.member.model.dto.EsMemberLevelConfigDTO;
import com.jjg.member.model.dto.EsQueryMemberLevelConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface MemberLevelConfigService {

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param memberLevelConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberLevelConfigDO>
     */
    @GetMapping("/getMemberLevelConfigList")
    DubboPageResult<EsMemberLevelConfigDO> getMemberLevelConfigList(@RequestBody EsQueryMemberLevelConfigDTO memberLevelConfigDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @DeleteMapping("/deleteMemberLevelConfig")
    DubboResult deleteMemberLevelConfig(@RequestParam("id")Long id);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberLevelConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @PostMapping("/insertMemberLevelConfig")
    DubboResult insertMemberLevelConfig(@RequestBody EsMemberLevelConfigDTO memberLevelConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param memberLevelConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMemberLevelConfigDO>
     */
    @PostMapping("/updateMemberLevelConfig")
    DubboResult updateMemberLevelConfig(@RequestBody EsMemberLevelConfigDTO memberLevelConfigDTO, @RequestParam("id") Long id);

}
