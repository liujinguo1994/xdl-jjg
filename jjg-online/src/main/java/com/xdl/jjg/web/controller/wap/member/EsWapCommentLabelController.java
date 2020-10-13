
package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsTagLabelListDO;
import com.shopx.member.api.model.domain.vo.EsTagLabelListVO;
import com.shopx.member.api.service.IEsCommentLabelService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
@Api(value = "/wap/esCommentLabel", tags = "移动端-评论标签")
@RestController
@RequestMapping("/wap/esCommentLabel")
public class EsWapCommentLabelController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCommentLabelService iesCommentLabelService;

    @ApiOperation(value = "查询评价标签", notes = "查询评价标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "商品三级分类id", required = true, dataType = "Long", paramType = "path", example = "1")
    })
    @GetMapping("/getCommentTag/{categoryId}")
    @ResponseBody
    public ApiResponse getMemberCollectionShopListByType(@PathVariable("categoryId") Long categoryId) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsTagLabelListDO> result = iesCommentLabelService.getCommentLabes(categoryId);
        EsTagLabelListVO esTagLabelListVO = new EsTagLabelListVO();
        if (result.isSuccess()) {
            BeanUtil.copyProperties(result.getData(), esTagLabelListVO);
            return ApiResponse.success(esTagLabelListVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


}


