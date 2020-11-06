package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommentInfoDO;
import com.jjg.member.model.domain.EsMemberCommentDetailDO;
import com.jjg.member.model.domain.GradeLevelDO;
import com.jjg.member.model.dto.EsMemberCommentCopyDTO;
import com.jjg.member.model.dto.EsQueryDetailCommentDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
@FeignClient(value = "jjg-member")
public interface MemberCommentService {

    /**
     * 依据商品id和订单查询评论信息
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsId    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @GetMapping("/getGoodCommentRate")
    DubboResult<Double> getGoodCommentRate(@RequestParam("goodsId") Long goodsId);


    /**
     * 获取商品印象
     * @date: 2019/06/28 16:40:44
     * @param goodsId    商品主键
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @GetMapping("/getLabelsGroup")
    DubboResult<List<Map<String, Object>>> getLabelsGroup(@RequestParam("goodsId") Long goodsId);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param memberCommentDTO    评论DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @PostMapping("/insertMemberComment")
    DubboResult insertMemberComment(@RequestBody EsMemberCommentCopyDTO memberCommentDTO);


    /**
     * 依据商品id和订单查询评论信息
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsId    评论DTO
     * @param orderSn esOrderDO
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCommentDO>
     */
    @GetMapping("/getPCCommentInfoByGoodsIdAndOrderSn")
    DubboResult<EsCommentInfoDO> getPCCommentInfoByGoodsIdAndOrderSn(@RequestParam("goodsId") Long goodsId, @RequestParam("orderSn") String orderSn, @RequestParam("skuId") Long skuId);
    @GetMapping("/getCommentInfoByGoodsIdAndOrderSn")
    DubboResult<EsCommentInfoDO> getCommentInfoByGoodsIdAndOrderSn(@RequestParam("goodsId") Long goodsId, @RequestParam("orderSn") String orderSn, @RequestParam("skuId") Long skuId);



    /**
     * 查询商品详情页面评价
     * @auther: lins 1220316142@qq.com
     * @param memberCommentDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/getMemberDetailCommentList")
    DubboPageResult<EsMemberCommentDetailDO> getMemberDetailCommentList(@RequestBody EsQueryDetailCommentDTO memberCommentDTO,@RequestParam("memberId")  Long memberId, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);



    /**
     * 统计商品评论数量
     * @auther: lins 1220316142@qq.com
     * @return
     */
    @GetMapping("/getCountComment")
    DubboResult<GradeLevelDO> getCountComment(@RequestParam("goodsId") Long goodsId);


}
