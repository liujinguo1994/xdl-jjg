package com.xdl.jjg.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constants.OssFileType;
import com.xdl.jjg.model.domain.*;
import com.xdl.jjg.model.dto.EsGoodsAveragePriceDTO;
import com.xdl.jjg.model.dto.EsGoodsHotSellDTO;
import com.xdl.jjg.model.dto.EsGoodsPaymentConversionRateDTO;
import com.xdl.jjg.model.dto.EsGoodsSalesDetailDTO;
import com.xdl.jjg.model.form.EsGoodsAveragePriceForm;
import com.xdl.jjg.model.form.EsGoodsHotSellForm;
import com.xdl.jjg.model.form.EsGoodsPaymentConversionRateForm;
import com.xdl.jjg.model.form.EsGoodsSalesDetailForm;
import com.xdl.jjg.model.vo.*;
import com.xdl.jjg.oss.upload.OSSUploader;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.service.FileDTO;
import com.xdl.jjg.response.service.FileVO;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGoodsStatisticsService;
import com.xdl.jjg.web.service.IEsGoodsTotalStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Api(value = "/statistics/goods", tags = "商品统计")
@RestController
@RequestMapping("/esGoodsStatistics")
public class EsGoodsStatisticsController {
    @Autowired
    private IEsGoodsStatisticsService esGoodsStatisticsService;
    @Autowired
    private IEsGoodsTotalStatisticsService esGoodsTotalStatisticsService;

    @Autowired
    private OSSUploader ossUploader;

    @ApiOperation(value = "商品销售明细", hidden = true)
    @GetMapping(value = "/goodsSalesDetail")
    public ApiResponse<EsGoodsSalesDetailVO> goodsSalesDetail(@ApiParam(name = "商品销售明细", value = "form") @Valid EsGoodsSalesDetailForm form) {
        EsGoodsSalesDetailDTO esGoodsSalesDetailDTO = new EsGoodsSalesDetailDTO();
        BeanUtil.copyProperties(form, esGoodsSalesDetailDTO);
        DubboPageResult<EsGoodsSalesDetailDO> result = esGoodsStatisticsService.getGoodsSalesDetailList(esGoodsSalesDetailDTO, new Page(form.getPageNum(), form.getPageSize()));
        if (!result.isSuccess()) {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
        List<EsGoodsSalesDetailVO> esGoodsSalesDetailVOS = BeanUtil.copyList(result.getData().getList(), EsGoodsSalesDetailVO.class);
        return ApiPageResponse.pageSuccess(esGoodsSalesDetailVOS);
    }

    @ApiOperation(value = "导出商品销售明细", hidden = true)
    @GetMapping(value = "/goodsSalesDetailExport")
    public ApiResponse goodsSalesDetailExport(@ApiParam(name = "商品销售明细", value = "form") @Valid EsGoodsSalesDetailForm form) {
        EsGoodsSalesDetailDTO esGoodsSalesDetailDTO = new EsGoodsSalesDetailDTO();
        BeanUtil.copyProperties(form, esGoodsSalesDetailDTO);
        DubboPageResult<EsGoodsSalesDetailDO> result = esGoodsStatisticsService.getGoodsSalesDetailList(esGoodsSalesDetailDTO, new Page(form.getPageNum(), form.getPageSize()));
        if (!result.isSuccess()) {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
        List<EsGoodsSalesDetailVO> esGoodsSalesDetailVOS = BeanUtil.copyList(result.getData().getList(), EsGoodsSalesDetailVO.class);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("goodsId", "商品ID");
        writer.addHeaderAlias("goodsName", "商品名称");
        writer.addHeaderAlias("orderNum", "下单量");
        writer.addHeaderAlias("orderGoodsNum", "下单商品总数");
        writer.addHeaderAlias("orderPriceTotal", "下单价格总和");
        writer.merge(4, "商品销售明细");
        writer.write(esGoodsSalesDetailVOS, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.flush(stream);
        writer.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(".xlsx");
        fileDTO.setStream(inputStream);
        //上传阿里云
        FileVO fileVO = ossUploader.upload(fileDTO, OssFileType.EXCEL);
        IoUtil.close(inputStream);
        return ApiResponse.success(fileVO.getUrl());
    }

    @ApiOperation(value = "商品热卖榜")
    @GetMapping(value = "/goodsHotSell")
    public ApiResponse<EsGoodsHotSellVO> goodsHotSell(@ApiParam(name = "商品热卖榜") @Valid EsGoodsHotSellForm form) {
        EsGoodsHotSellDTO esGoodsHotSellDTO = new EsGoodsHotSellDTO();
        BeanUtil.copyProperties(form, esGoodsHotSellDTO);
        DubboResult<IPage<EsGoodsHotSellDO>> result = esGoodsStatisticsService.getGoodsHotSell(esGoodsHotSellDTO, new Page(form.getPageNum(), form.getPageSize()));
        List<EsGoodsHotSellVO> esGoodsHotSellVOS = BeanUtil.copyList(result.getData().getRecords(), EsGoodsHotSellVO.class);
        return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsHotSellVOS);
    }

    @ApiOperation(value = "商品热卖榜导出")
    @GetMapping(value = "/goodsHotSellExport")
    public ApiResponse<EsGoodsHotSellVO> goodsHotSellExport(@ApiParam(name = "商品热卖榜") @Valid EsGoodsHotSellForm form) {
        EsGoodsHotSellDTO esGoodsHotSellDTO = new EsGoodsHotSellDTO();
        BeanUtil.copyProperties(form, esGoodsHotSellDTO);
        DubboResult<IPage<EsGoodsHotSellDO>> result = esGoodsStatisticsService.getGoodsHotSell(esGoodsHotSellDTO, new Page(form.getPageNum(), form.getPageSize()));
        List<EsGoodsHotSellVO> esGoodsHotSellVOS = BeanUtil.copyList(result.getData().getRecords(), EsGoodsHotSellVO.class);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.merge(5, "商品热卖榜");
        writer.writeRow(CollUtil.newArrayList("商品名称", "分类名称", "商品规格", "所属店铺", "总销售量", "总销售金额"));
        esGoodsHotSellVOS.forEach(hotSell -> {
            writer.writeRow(CollUtil.newArrayList(hotSell.getGoodsName(), hotSell.getCategoryName(), hotSell.getSpecText(), hotSell.getShopName(), hotSell.getSalesNum(), hotSell.getTotalMoney()));
        });
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.flush(stream);
        writer.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(".xlsx");
        fileDTO.setStream(inputStream);
        //上传阿里云
        FileVO fileVO = ossUploader.upload(fileDTO, OssFileType.EXCEL);
        IoUtil.close(inputStream);
        return ApiResponse.success(fileVO.getUrl());
    }

    @ApiOperation(value = "商品转化率榜")
    @GetMapping(value = "/goodsPaymentConversionRate")
    public ApiResponse<EsGoodsPaymentConversionRateVO> goodsPaymentConversionRate(@ApiParam(name = "商品转化率榜") @Valid EsGoodsPaymentConversionRateForm form) {
        EsGoodsPaymentConversionRateDTO esGoodsPaymentConversionRateDTO = new EsGoodsPaymentConversionRateDTO();
        BeanUtil.copyProperties(form, esGoodsPaymentConversionRateDTO);
        DubboResult<IPage<EsGoodsPaymentConversionRateDO>> result = esGoodsStatisticsService.getGoodsPaymentConversionRate(esGoodsPaymentConversionRateDTO, new Page(form.getPageNum(), form.getPageSize()));
        List<EsGoodsPaymentConversionRateVO> esGoodsPaymentConversionRateVOS = BeanUtil.copyList(result.getData().getRecords(), EsGoodsPaymentConversionRateVO.class);
        return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsPaymentConversionRateVOS);
    }

    @ApiOperation(value = "商品转化率榜导出")
    @GetMapping(value = "/goodsPaymentConversionRateExport")
    public ApiResponse<EsGoodsPaymentConversionRateVO> goodsPaymentConversionRateExport(@ApiParam(name = "商品转化率榜") @Valid EsGoodsPaymentConversionRateForm form) {
        EsGoodsPaymentConversionRateDTO esGoodsPaymentConversionRateDTO = new EsGoodsPaymentConversionRateDTO();
        BeanUtil.copyProperties(form, esGoodsPaymentConversionRateDTO);
        DubboResult<IPage<EsGoodsPaymentConversionRateDO>> result = esGoodsStatisticsService.getGoodsPaymentConversionRate(esGoodsPaymentConversionRateDTO, new Page(form.getPageNum(), form.getPageSize()));
        List<EsGoodsPaymentConversionRateVO> esGoodsPaymentConversionRateVOS = BeanUtil.copyList(result.getData().getRecords(), EsGoodsPaymentConversionRateVO.class);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.merge(5, "商品转化率榜");
        writer.writeRow(CollUtil.newArrayList("商品名称", "分类名称", "商品规格", "转化率", "总销售量", "总销售金额"));
        esGoodsPaymentConversionRateVOS.forEach(goods -> {
            writer.writeRow(CollUtil.newArrayList(goods.getGoodsName(), goods.getCategoryName(), goods.getSpecText(), goods.getConversionRate(), goods.getSalesNum(), goods.getTotalMoney()));
        });
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.flush(stream);
        writer.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(".xlsx");
        fileDTO.setStream(inputStream);
        //上传阿里云
        FileVO fileVO = ossUploader.upload(fileDTO, OssFileType.EXCEL);
        IoUtil.close(inputStream);
        return ApiResponse.success(fileVO.getUrl());
    }

    @ApiOperation(value = "商品客单价")
    @GetMapping(value = "/goodsAveragePrice")
    public ApiResponse<EsGoodsAveragePriceVO> goodsAveragePrice(@ApiParam(name = "商品客单价") @Valid EsGoodsAveragePriceForm form) {
        EsGoodsAveragePriceDTO esGoodsAveragePriceDTO = new EsGoodsAveragePriceDTO();
        BeanUtil.copyProperties(form, esGoodsAveragePriceDTO);
        DubboResult<IPage<EsGoodsAveragePriceDO>> result = esGoodsStatisticsService.getGoodsAveragePrice(esGoodsAveragePriceDTO, new Page(form.getPageNum(), form.getPageSize()));
        List<EsGoodsAveragePriceVO> esGoodsAveragePriceVOS = BeanUtil.copyList(result.getData().getRecords(), EsGoodsAveragePriceVO.class);
        return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsAveragePriceVOS);
    }

    @ApiOperation(value = "商品客单价导出")
    @GetMapping(value = "/goodsAveragePriceExport")
    public ApiResponse<EsGoodsAveragePriceVO> goodsAveragePriceExport(@ApiParam(name = "商品客单价") @Valid EsGoodsAveragePriceForm form) {
        EsGoodsAveragePriceDTO esGoodsAveragePriceDTO = new EsGoodsAveragePriceDTO();
        BeanUtil.copyProperties(form, esGoodsAveragePriceDTO);
        DubboResult<IPage<EsGoodsAveragePriceDO>> result = esGoodsStatisticsService.getGoodsAveragePrice(esGoodsAveragePriceDTO, new Page(form.getPageNum(), form.getPageSize()));
        List<EsGoodsAveragePriceVO> esGoodsAveragePriceVOS = BeanUtil.copyList(result.getData().getRecords(), EsGoodsAveragePriceVO.class);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.merge(4, "商品客单价");
        writer.writeRow(CollUtil.newArrayList("商品名称", "分类名称", "商品规格", "总销售量", "客单价"));
        esGoodsAveragePriceVOS.forEach(goods -> {
            writer.writeRow(CollUtil.newArrayList(goods.getGoodsName(), goods.getCategoryName(), goods.getSpecText(), goods.getSalesNum(), goods.getAveragePrice()));
        });
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writer.flush(stream);
        writer.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setName(".xlsx");
        fileDTO.setStream(inputStream);
        //上传阿里云
        FileVO fileVO = ossUploader.upload(fileDTO, OssFileType.EXCEL);
        IoUtil.close(inputStream);
        return ApiResponse.success(fileVO.getUrl());
    }

    @ApiOperation(value = "商品总数统计")
    @GetMapping(value = "/goodsTotalStatistics")
    public ApiResponse<EsGoodsTotalStatisticsVO> goodsTotalStatistics() {
        DubboResult<EsGoodsTotalStatisticsDO> result = esGoodsTotalStatisticsService.adminGoodsTotalStatistics();
        EsGoodsTotalStatisticsVO esGoodsTotalStatisticsVO = new EsGoodsTotalStatisticsVO();
        BeanUtil.copyProperties(result.getData(), esGoodsTotalStatisticsVO);
        return ApiResponse.success(esGoodsTotalStatisticsVO);
    }
}
