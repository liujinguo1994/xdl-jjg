package com.xdl.jjg.web.controller.wap.member;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.MathUtil;
import com.shopx.goods.api.model.domain.EsGoodsArchDO;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.model.domain.EsSpecValuesDO;
import com.shopx.goods.api.service.IEsGoodsArchService;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsReceiptHistoryDO;
import com.shopx.member.api.model.domain.InvoiceSerialDO;
import com.shopx.member.api.model.domain.QueryByInvoiceSerialDO;
import com.shopx.member.api.model.domain.dto.*;
import com.shopx.member.api.model.domain.vo.EsReceiptHistoryVO;
import com.shopx.member.api.service.IEsMemberReceiptService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsReceiptHistoryService;
import com.shopx.trade.api.model.domain.EsBuyerOrderDO;
import com.shopx.trade.api.model.domain.EsBuyerOrderItemsDO;
import com.shopx.trade.api.model.domain.EsOrderItemsDO;
import com.shopx.trade.api.model.domain.dto.EsBuyerOrderQueryDTO;
import com.shopx.trade.api.model.domain.dto.EsOrderItemsDTO;
import com.shopx.trade.api.model.domain.vo.EsBuyerOrderItemsVO;
import com.shopx.trade.api.model.domain.vo.EsBuyerOrderVO;
import com.shopx.trade.api.service.IEsOrderItemsService;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsWapMemberReceiptForm;
import com.shopx.trade.web.request.QueryReceiptOrdersForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import nuonuo.open.sdk.NNOpenSDK;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 移动端-发票
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-12 09:28:26
 */
@Api(value = "/wap/member/receipt", tags = "移动端-发票")
@RestController
@RequestMapping("/wap/member/receipt")
public class EsWapReceiptController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberReceiptService iesMemberReceiptService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderItemsService iEsOrderItemsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService iEsGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsArchService iEsGoodsArchService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsReceiptHistoryService iEsReceiptHistoryService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberReceiptService iEsMemberReceiptService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService iEsMemberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOrderService iEsOrderService;
    @Autowired
    private JedisCluster jedisCluster;
    //url
    @Value("${zhuo.member.receipt.url}")
    private String receiptUrl;
    //发票key
    @Value("${receipt.appKey}")
    private String appKey;
    //发票secret
    @Value("${receipt.appSecret}")
    private String appSecret;
    //企业税号
    @Value("${business.tax.num}")
    private String businessNum;
    //收款人
    @Value("${zhuo.member.payee}")
    private String payee;
    //推送方式
    @Value("${zhuo.member.push.mode}")
    private String pushMode;
    //复核人
    @Value("${zhuo.member.checker}")
    private String checker;
    //开票员
    @Value("${zhuo.member.clerk}")
    private String clerk;
    //销方地址
    @Value("${zhuo.member.salerAddress}")
    private String salerAddress;
    //销方电话
    @Value("${zhuo.member.salerTel}")
    private String salerTel;
    //销方开户行信息
    @Value("${zhuo.member.salerAccount}")
    private String salerAccount;
    //发票token有效时常
    @Value("${receipt.token.time}")
    private int receiptToken;
    //销售方税号
    @Value("${zhuo.member.salerTaxNum}")
    private String salerTaxNum;
    //开发票方法
    @Value("${zhuo.member.open.receipt.method}")
    private String openMethod;
    //查询发票方法
    @Value("${zhuo.member.query.receipt.method}")
    private String queryMethod;

    @ApiOperation(value = "开发票")
    @PostMapping("/insertCommentReceipt")
    @ResponseBody
    public ApiResponse insertCommentReceipt(EsWapMemberReceiptForm esMemberReceiptForm) {
        try {
            EsReceiptHistoryDTO esReceiptHistoryDTO = new EsReceiptHistoryDTO();
            BeanUtil.copyProperties(esMemberReceiptForm, esReceiptHistoryDTO);
            Long memberId = ShiroKit.getUser().getId();
            if (null == memberId) {
                return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
            }
            esReceiptHistoryDTO.setMemberId(memberId);
            //查询开票人基本信息
            DubboResult<EsMemberDO> resultMember = iEsMemberService.getAdminMember(memberId);
            esReceiptHistoryDTO.setMemberName(resultMember.getData().getName());
            Long receiptId;
            //开具专票或企业发票不开电子版
            EsMemberReceiptDTO esMemberReceiptDTO = new EsMemberReceiptDTO();
            BeanUtil.copyProperties(esMemberReceiptForm, esMemberReceiptDTO);
            DubboResult<Long> resultReceipt = iEsMemberReceiptService.insertMemberReceipt(esMemberReceiptDTO);
            if (!resultReceipt.isSuccess()) {
                return ApiResponse.fail(resultReceipt.getCode(), resultReceipt.getMsg());
            }
            //非个人的不开具电子版本
            if (esMemberReceiptForm.getReceiptType().equals(String.valueOf(MemberConstant.subjectReceipt)) || esMemberReceiptForm.getReceiptTitle().contains(MemberConstant.receiptTitle)) {
                return ApiResponse.success();
            }
            receiptId = resultReceipt.getData();
            //开具电子版发票
            DubboResult<EsGoodsDO> resultGoods = iEsGoodsService.buyGetEsGoods(esMemberReceiptForm.getGoodsId());
            if (!resultGoods.isSuccess()) {
                return ApiResponse.fail(resultGoods.getCode(), resultGoods.getMsg());
            }
            if (null == resultGoods.getData()) {
                return ApiResponse.fail(MemberErrorCode.GOODS_IS_EMPTY.getErrorCode(), MemberErrorCode.GOODS_IS_EMPTY.getErrorMsg());
            }
            //普票
            if (resultGoods.getData().getInvoiceType() != MemberConstant.ommentReceipt) {
                return ApiResponse.fail(MemberErrorCode.NOT_SUPPORT_RECEIPT.getErrorCode(), MemberErrorCode.NOT_SUPPORT_RECEIPT.getErrorMsg());
            }
            //商品id查询商品编号，依据商品编号查询商品档案信息
            DubboResult<EsGoodsArchDO> archResult = iEsGoodsArchService.getGoodsArchSn(resultGoods.getData().getGoodsSn());
            if (!archResult.isSuccess()) {
                return ApiResponse.fail(archResult.getCode(), archResult.getMsg());
            }
            if (null == archResult.getData()) {
                return ApiResponse.fail(MemberErrorCode.GOODS_ARCH_EMPTY.getErrorCode(), MemberErrorCode.GOODS_ARCH_EMPTY.getErrorMsg());
            }
            //查询订单商品明细
            DubboResult<EsOrderItemsDO> resultItems = iEsOrderItemsService.getEsOrderItemsInfo(esMemberReceiptForm.getItemsId());
            if (!resultItems.isSuccess()) {
                return ApiResponse.fail(resultItems.getCode(), resultItems.getMsg());
            }
            List<EsSpecValuesDO> resultSpecValueList = JSON.parseObject(resultItems.getData().getSpecJson(), new TypeReference<List<EsSpecValuesDO>>() {
            });
            // DubboResult<EsOrderDO> esOrderResult = iEsOrderService.getEsBuyerOrderInfo(resultItems.getData().getOrderSn());
           /* if(esOrderResult.isSuccess()){
               //可查询店铺id
            }*/
            //获取发票token值
            ApiResponse data = this.getRecepitToken();
            String token = data.getData().toString();
            NNOpenSDK sdk = NNOpenSDK.getIntance();
            ReceiptTaxDTO receiptTaxDTO = new ReceiptTaxDTO();
            EsReceiptTaxDTO esReceiptTaxDTO = new EsReceiptTaxDTO();
            List<ReceiptTaxInvoiceDetailDTO> detailDTOList = new ArrayList<>();
            ReceiptTaxInvoiceDetailDTO receiptTaxInvoiceDetailDTO = new ReceiptTaxInvoiceDetailDTO();
            if (CollectionUtils.isNotEmpty(resultSpecValueList)) {
                String specValue = "";
                for (EsSpecValuesDO esSpecValuesDO : resultSpecValueList) {
                    specValue += esSpecValuesDO.getSpecValue();
                }
                receiptTaxInvoiceDetailDTO.setSpecType(specValue);
            }
            receiptTaxInvoiceDetailDTO.setWithTaxFlag("1");
            if (archResult.getData().getTaxRate().compareTo(Double.parseDouble(MemberConstant.reateValue)) == 0) {
                // receiptTaxInvoiceDetailDTO.setWithTaxFlag("0");
                //普通零税率时候
                receiptTaxInvoiceDetailDTO.setZeroRateFlag("3");
            } else {
                // receiptTaxInvoiceDetailDTO.setWithTaxFlag("1");
            }
            receiptTaxInvoiceDetailDTO.setGoodsName(resultGoods.getData().getGoodsName());
            //设置单位
            receiptTaxInvoiceDetailDTO.setUnit(archResult.getData().getUnit());
            //设置税率
            receiptTaxInvoiceDetailDTO.setTaxRate(archResult.getData().getTaxRate().toString());
            //设置购买数量
            receiptTaxInvoiceDetailDTO.setNum(resultItems.getData().getNum().toString());
            //设置单价
            receiptTaxInvoiceDetailDTO.setPrice(resultItems.getData().getMoney().toString());
            //计算发票金额
            Double receiptMony = MathUtil.multiply(resultItems.getData().getNum(), resultItems.getData().getMoney());
            esReceiptHistoryDTO.setReceiptAmount(MathUtil.round(receiptMony, 2));
            //计算税额
            Double resultMlty = MathUtil.multiply(MathUtil.multiply(resultItems.getData().getMoney(), resultItems.getData().getNum()), archResult.getData().getTaxRate());
            receiptTaxInvoiceDetailDTO.setTax(MathUtil.round(resultMlty, 2).toString());
            //税收分类编码
            receiptTaxInvoiceDetailDTO.setGoodsCode(archResult.getData().getCateCode());
            detailDTOList.add(receiptTaxInvoiceDetailDTO);
            //订单编号+商品id
            esReceiptTaxDTO.setOrderNo(esMemberReceiptForm.getOrderSn() + esMemberReceiptForm.getGoodsId() + "88");
            esReceiptTaxDTO.setListName("详见销货清单");
            esReceiptTaxDTO.setPayee(payee);
            esReceiptTaxDTO.setPushMode(pushMode);
            //购买人名称
            esReceiptTaxDTO.setBuyerName(resultMember.getData().getName());
            esReceiptTaxDTO.setBuyerPhone(esMemberReceiptForm.getRegTel());
            esReceiptTaxDTO.setChecker(checker);
            esReceiptTaxDTO.setClerk(clerk);
            esReceiptTaxDTO.setSalerAddress(salerAddress);
            esReceiptTaxDTO.setSalerTel(salerTel);
            esReceiptTaxDTO.setSalerAccount(salerAccount);
            //确定是否打印至清单
            esReceiptTaxDTO.setListFlag(esMemberReceiptForm.getInvoiceDetails());
            esReceiptTaxDTO.setSalerTaxNum(salerTaxNum);
            //发票交易时间
            String nowTimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            esReceiptTaxDTO.setInvoiceDate(nowTimes);
            esReceiptTaxDTO.setProxyInvoiceFlag("0");
            esReceiptTaxDTO.setInvoiceType("1");
            esReceiptTaxDTO.setInvoiceDetail(detailDTOList);
            receiptTaxDTO.setOrder(esReceiptTaxDTO);
            String jsonString = JSON.toJSONString(receiptTaxDTO);
            System.out.println(jsonString);
            String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，由企业自己生成32位随机码
            String result = sdk.sendPostSyncRequest(receiptUrl, senid, appKey, appSecret, token, salerTaxNum, openMethod, jsonString);
            Thread.sleep(7000);
            JSONObject jsonObject = JSONObject.parseObject(result);
            InvoiceSerialDO invoiceSerialDO = JSONObject.toJavaObject(jsonObject, InvoiceSerialDO.class);
            if (!invoiceSerialDO.getCode().equals(MemberConstant.receiptSuccess)) {
                esReceiptHistoryDTO.setState(MemberConstant.failReceiptState);
                this.insertReceiptHistory(esReceiptHistoryDTO, invoiceSerialDO, receiptId, false);
                return ApiResponse.fail(Integer.parseInt(invoiceSerialDO.getCode()), invoiceSerialDO.getDescribe());
            }
            //开票完成写入发票历史表中
            this.insertReceiptHistory(esReceiptHistoryDTO, invoiceSerialDO, receiptId, true);
            //修改商品订单详细表发票流水号
            EsOrderItemsDTO esOrderItemsDTO = new EsOrderItemsDTO();
            //订单明细表主键
            esOrderItemsDTO.setId(esMemberReceiptForm.getItemsId());
            esOrderItemsDTO.setInvoiceNumber(invoiceSerialDO.getResult().getInvoiceSerialNum());
            DubboResult<EsOrderItemsDO> resultOrders = iEsOrderItemsService.updateOrderItemsMessage(esOrderItemsDTO);
            //修改订单发票流水号失败
            if (!resultOrders.isSuccess()) {
                return ApiResponse.fail(resultOrders.getCode(), resultOrders.getMsg());
            }
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(MemberErrorCode.OPEN_RECEIIPT_ERROR.getErrorCode(), MemberErrorCode.OPEN_RECEIIPT_ERROR.getErrorMsg());
        }
    }

    /**
     * 开票完成写入发票历史表中
     */
    public ApiResponse insertReceiptHistory(EsReceiptHistoryDTO receiptHistoryDTO, InvoiceSerialDO invoiceSerialDO, Long receiptId, Boolean judge) {
        //查询发票信息
        EsReceiptHistoryDTO esReceiptHistoryDTO = new EsReceiptHistoryDTO();
        BeanUtil.copyProperties(receiptHistoryDTO, esReceiptHistoryDTO);
        ApiResponse resultInfo = null;
        QueryByInvoiceSerialDO info = null;
        if (judge) {
            resultInfo = this.getRecepitInfo(invoiceSerialDO.getResult().getInvoiceSerialNum());
            info = (QueryByInvoiceSerialDO) resultInfo.getData();
            esReceiptHistoryDTO.setState(MemberConstant.succesecReceiptState);
        }
        if (null != receiptId) {
            esReceiptHistoryDTO.setHistoryId(receiptId);
        }
        if (judge) {
            //塞入发票流水号
            esReceiptHistoryDTO.setInvoiceSerialNum(info.getResult().get(0).getInvoiceSerialNum());
            //塞入发票存贮地址
            esReceiptHistoryDTO.setInvoiceFileUrl(info.getResult().get(0).getInvoiceFileUrl());
            //塞入发票图片地址
            esReceiptHistoryDTO.setInvoiceImageUrl(info.getResult().get(0).getInvoiceImageUrl());
            //塞入发票代码
            esReceiptHistoryDTO.setInvoiceCode(info.getResult().get(0).getInvoiceCode());
            //塞入发票号码
            esReceiptHistoryDTO.setInvoiceNum(info.getResult().get(0).getInvoiceNum());
        } else {
            esReceiptHistoryDTO.setFailReason(invoiceSerialDO.getDescribe());
        }
        DubboResult resultInsert = iEsReceiptHistoryService.insertReceiptHistory(esReceiptHistoryDTO);
        if (!resultInsert.isSuccess()) {
            throw new ArgumentException(resultInsert.getCode(), resultInsert.getMsg());
        }
        return ApiResponse.success();
    }

    /**
     * 依据发票流水号查询发票相关信息
     *
     */
    public ApiResponse getRecepitInfo(String serialNum) {
        try {
            ApiResponse data = this.getRecepitToken();
            String token = data.getData().toString();
            NNOpenSDK sdk = NNOpenSDK.getIntance();
            //拼接发票流水号
            String content = "{\"invoiceSerialNum\":[\"" + serialNum + "\"]}";
            String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，由企业自己生成32位随机码
            String result = sdk.sendPostSyncRequest(receiptUrl, senid, appKey, appSecret, token, salerTaxNum, queryMethod, content);
            Thread.sleep(4000);
            JSONObject jsonObject = JSONObject.parseObject(result);
            QueryByInvoiceSerialDO invoiceSerialDO = JSONObject.toJavaObject(jsonObject, QueryByInvoiceSerialDO.class);
            if (MemberConstant.queryReceiptSuccess.equals(invoiceSerialDO.getCode())) {
                if (CollectionUtils.isNotEmpty(invoiceSerialDO.getResult()) && !StringUtils.isBlank(invoiceSerialDO.getResult().get(0).getInvoiceFileUrl())) {
                    return ApiResponse.success(invoiceSerialDO);
                }
            }
            return ApiResponse.fail(MemberErrorCode.RECEIIPT_ERROR.getErrorCode(), MemberErrorCode.RECEIIPT_ERROR.getErrorMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(MemberErrorCode.RECEIIPT_ERROR.getErrorCode(), MemberErrorCode.RECEIIPT_ERROR.getErrorMsg());
        }
    }


    //获取发票接口token值
    public ApiResponse getRecepitToken() {
        //如果缓存有token则直接取值
        String token = jedisCluster.get("receipt" + "_" + "token");
        if (!StringUtils.isBlank(token)) {
            return ApiResponse.success(token);
        }
        //如果token没有值则调用服务获取token
        synchronized (com.shopx.trade.web.controller.wap.member.EsWapReceiptController.class) {
            String json = NNOpenSDK.getIntance().getMerchantToken(appKey, appSecret);
            //如果token获取成功
            if (json.contains("access_token")) {
                JSONObject obj = JSONObject.parseObject(json);
                String receiptToken = obj.get("access_token").toString();
                //如果token为空则报错
                if (StringUtils.isBlank(receiptToken)) {
                    return ApiResponse.fail(MemberErrorCode.MEMBER_ERROR_TOKEN.getErrorCode(), json);
                }
                int expiresIn = Integer.parseInt(obj.get("expires_in").toString());
                jedisCluster.setex("receipt" + "_" + "token", expiresIn, receiptToken);
                return ApiResponse.success(receiptToken);
            } else {
                return ApiResponse.fail(MemberErrorCode.MEMBER_ERROR_TOKEN.getErrorCode(), json);
            }
        }
    }


    @ApiOperation(value = "根据发票流水号获取会员历史发票信息")
    @ApiImplicitParam(name = "invoiceSerialNum", value = "发票流水号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/getInfoByInvoiceSerialNum/{invoiceSerialNum}")
    @ResponseBody
    public ApiResponse getReceiptInfo(@PathVariable String invoiceSerialNum) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsReceiptHistoryDO> result = this.iEsReceiptHistoryService.getReceiptHistoryByInvoiceSerialNum(invoiceSerialNum);
        if (result.isSuccess()) {
            EsReceiptHistoryVO vo = new EsReceiptHistoryVO();
            BeanUtil.copyProperties(result.getData(), vo);
            return ApiResponse.success(vo);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "查询订单和发票", notes = "查询订单和发票", response = EsBuyerOrderVO.class)
    @GetMapping("/getOrdersAndReceipt")
    @ResponseBody
    public ApiResponse getOrdersAndReceipt(QueryReceiptOrdersForm queryReceiptOrdersForm) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsBuyerOrderQueryDTO esBuyerOrderQueryDTO = new EsBuyerOrderQueryDTO();
        esBuyerOrderQueryDTO.setMemberId(userId);
        esBuyerOrderQueryDTO.setKeyword(queryReceiptOrdersForm.getKeyword());
        DubboPageResult<EsBuyerOrderDO> result =this.iEsOrderService.getEsMemberReceiptList(esBuyerOrderQueryDTO,
                queryReceiptOrdersForm.getPageSize(),queryReceiptOrdersForm.getPageNum());
        if (result.isSuccess()) {
            List<EsBuyerOrderVO> list = new ArrayList<>();
            List<EsBuyerOrderDO> listDO = result.getData().getList();
            list = listDO.stream().map(buyerOrder -> {
                List<EsBuyerOrderItemsDO> buyerOrderItemsDOs = buyerOrder.getEsBuyerOrderItemsDO();
                List<EsBuyerOrderItemsVO> orderItemsVOList = BeanUtil.copyList(buyerOrderItemsDOs, EsBuyerOrderItemsVO.class);
                EsBuyerOrderVO buyerOrderVO = new EsBuyerOrderVO();
                BeanUtil.copyProperties(buyerOrder, buyerOrderVO);
                buyerOrderVO.setEsBuyerOrderItemsVO(orderItemsVOList);
                return buyerOrderVO;
            }).collect(Collectors.toList());
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }



}

