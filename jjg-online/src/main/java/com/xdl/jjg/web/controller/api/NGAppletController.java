package com.xdl.jjg.web.controller.api;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.system.api.model.domain.EsLogiCompanyDO;
import com.shopx.system.api.model.domain.EsRegionsDO;
import com.shopx.system.api.model.domain.vo.EsLogiCompanyVO;
import com.shopx.system.api.model.domain.vo.EsRegionsVO;
import com.shopx.system.api.model.domain.vo.ExpressDetailVO;
import com.shopx.system.api.service.IEsExpressPlatformService;
import com.shopx.system.api.service.IEsLogiCompanyService;
import com.shopx.system.api.service.IEsRegionsService;
import com.shopx.trade.api.model.domain.EsShipCompanyDetailsDO;
import com.shopx.trade.api.service.IEsShipCompanyDetailsService;
import com.shopx.trade.api.utils.CurrencyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  前端控制器-内购小程序相关接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-04-01
 */
@RestController
@Api(value = "/api/applet",tags = "内购小程序相关接口")
@RequestMapping("/api/applet")
public class NGAppletController {
    private static Logger logger = LoggerFactory.getLogger(NGAppletController.class);

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsLogiCompanyService logiCompanyService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRegionsService regionsService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsShipCompanyDetailsService  esShipCompanyDetailsService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsExpressPlatformService esExpressPlatformService;

    @GetMapping(value = "/{id}/children")
    @ApiOperation(value = "获取某地区的子地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区id", required = true, dataType = "long", paramType = "path",example = "1")
    })
    public List<EsRegionsVO> getChildrenById(@PathVariable Long id) {
        DubboResult<List<EsRegionsDO>> result = regionsService.getChildrenById(id);
        List<EsRegionsVO> voList = new ArrayList<>();
        if (result.isSuccess()) {
            List<EsRegionsDO> data = result.getData();
            if (CollectionUtils.isNotEmpty(data)) {
                voList = BeanUtil.copyList(data, EsRegionsVO.class);
            }
        }
        return voList;
    }


    @ApiOperation(value = "获取有效的快递公司")
    @GetMapping(value = "/getLogiCompanyList")
    public List<EsLogiCompanyVO> getLogiCompanyList(){
        DubboPageResult<EsLogiCompanyDO> result = logiCompanyService.getLogiCompanyList();
        List<EsLogiCompanyVO> voList = new ArrayList<>();
        if (result.isSuccess()){
            List<EsLogiCompanyDO> data = result.getData().getList();
            if (CollectionUtils.isNotEmpty(data)) {
                voList = BeanUtil.copyList(data, EsLogiCompanyVO.class);
            }
        }
        return voList;
    }
    @ApiOperation(value = "获取运费")
    @GetMapping(value = "/getFreightPrice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaId", value = "地区id", required = true, dataType = "long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "normalSum", value = "货品总重量", required = true, dataType = "double", paramType = "query",example = "1")
    })
    public Double getFreightPrice(Double normalSum,Long areaId){
      DubboResult<EsShipCompanyDetailsDO> result =  esShipCompanyDetailsService.getPrice(areaId);
      if(result.isSuccess() && result.getData().getFirstCompany() != null){
          Double  money;
          EsShipCompanyDetailsDO shipCompanyDetailsDO= result.getData();
              if (normalSum > shipCompanyDetailsDO.getFirstCompany()) {
              //运费计算
              Double continueW= CurrencyUtil.sub(normalSum,shipCompanyDetailsDO.getFirstCompany());//续重
              Double aa=CurrencyUtil.add(shipCompanyDetailsDO.getFirstPrice(),CurrencyUtil.mul(Math.ceil(continueW),shipCompanyDetailsDO.getContinuedPrice()));
              money= aa;
          } else {
              money=shipCompanyDetailsDO.getFirstPrice();
          }
          return money;
      }else{
          return 0.0;
      }
    }

    @ApiOperation(value = "查询物流详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "物流公司id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "快递单号", dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/getLogiInfo")
    public ExpressDetailVO getLogiInfo(Long id, String num) {
        DubboResult<ExpressDetailVO> result = esExpressPlatformService.getExpressFormDetail(id, num);
        ExpressDetailVO detailVO = new ExpressDetailVO();
        if (result.isSuccess()){
            detailVO = result.getData();
            return detailVO;
        }else {
            return detailVO;
        }
    }
}
