package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.enums.PageCreateEnum;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.service.IEsPageCreateManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 前端控制器-静态页
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esPageCreateManager")
@Api(value = "/esPageCreateManager", tags = "静态页")
public class EsPageCreateManagerController {

    @Autowired
    private IEsPageCreateManagerService pageCreateManagerService;

    @ApiOperation("生成静态页")
    @ApiImplicitParam(name = "choosePages", value = "静态页：INDEX/GOODS/HELP 代表首页/商品页/帮助页", dataType = "String", paramType = "query", allowMultiple = true)
    @PostMapping(value = "/create")
    @ResponseBody
    public ApiResponse create(@ApiIgnore @RequestParam("choosePages") String[] choosePages) {
        //参数校验
        if (choosePages == null || choosePages.length == 0) {
            return ApiResponse.fail(1001, "生成静态页参数为空");
        }
        for (String p : choosePages) {
            if (!p.equals(PageCreateEnum.GOODS.name()) && !p.equals(PageCreateEnum.HELP.name()) && !p.equals(PageCreateEnum.INDEX.name())) {
                return ApiResponse.fail(1002, "生成静态页参数有误");
            }
        }
        DubboResult<Boolean> result = pageCreateManagerService.create(choosePages);
        if (result.isSuccess()) {
            Boolean b = result.getData();
            if (b) {
                return ApiResponse.success("任务开始");
            } else {
                return ApiResponse.fail(1003, "有静态页生成任务正在进行中，需等待本次任务完成后才能再次生成。");
            }
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }

    }
}
