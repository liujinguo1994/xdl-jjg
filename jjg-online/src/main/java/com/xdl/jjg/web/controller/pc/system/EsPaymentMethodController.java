package com.xdl.jjg.web.controller.pc.system;


import com.jjg.trade.model.dto.EsPaymentMethodDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsPaymentMethodService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esPaymentMethod")
public class EsPaymentMethodController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsPaymentMethodService iesPaymentMethodService;

    @GetMapping()
    public ApiResponse getList (){
        DubboPageResult result = iesPaymentMethodService.getPaymentMethodList(null, 10,10);

        return ApiResponse.success(result.getData().getList());
    }

    @GetMapping("/{id}")
    public ApiResponse getList (@PathVariable String id){
        DubboResult result = iesPaymentMethodService.getPaymentMethod(id);

        return ApiResponse.success(result.getData());
    }

    @PostMapping()
    public void insert(@RequestBody EsPaymentMethodDTO paymentMethodDTO) {
        iesPaymentMethodService.updatePaymentMethod(paymentMethodDTO, paymentMethodDTO.getPluginId());
    }

}

