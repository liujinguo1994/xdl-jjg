package com.xdl.jjg.web.service.impl;/**
 * @author wangaf
 * @date 2020/3/31 13:48
 **/

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsCakeCardDO;
import com.shopx.trade.api.model.domain.dto.EsCakeCardDTO;
import com.shopx.trade.api.model.domain.dto.EsCakeCardQueryDTO;
import com.shopx.trade.api.model.domain.dto.EsCakeImportDTO;
import com.shopx.trade.api.model.domain.dto.FailCakeData;
import com.shopx.trade.api.service.IEsCakeCardService;
import com.shopx.trade.dao.entity.EsCakeCard;
import com.shopx.trade.dao.mapper.EsCakeCardMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.annotations.Arg;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 @Author wangaf 826988665@qq.com
 @Date 2020/3/31
 @Version V1.0
 **/
@Service(version = "${dubbo.application.version}", interfaceClass = IEsCakeCardService.class, timeout = 5000)
public class IEsCakeCardServiceImpl  extends ServiceImpl<EsCakeCardMapper, EsCakeCard> implements IEsCakeCardService {
    private static Logger logger = LoggerFactory.getLogger(IEsCakeCardServiceImpl.class);
    @Autowired
    private EsCakeCardMapper esCakeCardMapper;
    @Override
    public DubboResult<EsCakeCardDO> getByCode(String code) {
        try{
            QueryWrapper<EsCakeCard> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCakeCard::getCode,code);
            EsCakeCard esCakeCard = this.esCakeCardMapper.selectOne(queryWrapper);
            return DubboResult.success(esCakeCard);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    public DubboResult<EsCakeCardDO> getLowCode() {
        try{
            EsCakeCardDO esCakeCardDO = esCakeCardMapper.getLowCode();
            return  DubboResult.success(esCakeCardDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional
    public DubboResult updateCakeCard(EsCakeCardDTO esCakeCardDTO) {
        try{
            EsCakeCard esCakeCard = new EsCakeCard();
            BeanUtil.copyProperties(esCakeCardDTO,esCakeCard);
            this.esCakeCardMapper.updateById(esCakeCard);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsCakeCardDO> list(EsCakeCardQueryDTO param) {
        try{
            QueryWrapper<EsCakeCard> queryWrapper = new QueryWrapper<>();
            if (param.getCode() != null) {
                queryWrapper.lambda().eq(EsCakeCard::getCode,param.getCode());
            }
            if (param.getIsUsed() != null) {
                queryWrapper.lambda().eq(EsCakeCard::getIsUsed,param.getIsUsed());
            }
            if (StringUtils.isNotBlank(param.getOrderSn())) {
                queryWrapper.lambda().eq(EsCakeCard::getOrderSn,param.getOrderSn());
            }
            Page page = new Page(param.getPageNum(), param.getPageSize());
            IPage<EsCakeCard> cardIPage = this.page(page, queryWrapper);
            List<EsCakeCardDO> orderDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(cardIPage.getRecords())) {
                orderDOList = cardIPage.getRecords().stream().map(cakeCard -> {
                    EsCakeCardDO orderDO = new EsCakeCardDO();
                    BeanUtil.copyProperties(cakeCard, orderDO);
                    return orderDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(cardIPage.getTotal(),orderDOList);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsCakeCardDO> getCakeCardByOrderSn(String orderSn) {
        QueryWrapper<EsCakeCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsCakeCard::getOrderSn,orderSn);
        EsCakeCard esCakeCard =  this.esCakeCardMapper.selectOne(queryWrapper);
        if(esCakeCard !=null){
            EsCakeCardDO esCakeCardDO = new EsCakeCardDO();
            BeanUtil.copyProperties(esCakeCard,esCakeCardDO);
            return DubboResult.success(esCakeCardDO);
        }
        return DubboResult.success(null);
    }

    @Override
    public DubboResult<EsCakeCardDO> getModel(Integer id) {
        try{
            EsCakeCard esCakeCard=this.esCakeCardMapper.selectById(id);
            if(esCakeCard == null){
                throw  new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsCakeCardDO esCakeCardDO = new EsCakeCardDO();
            BeanUtil.copyProperties(esCakeCard,esCakeCardDO);
            return DubboResult.success(esCakeCardDO);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional
    public DubboResult<EsCakeCardDO> add(EsCakeCardDTO cakeCardDO) {
        try{
            Long now = System.currentTimeMillis();
            cakeCardDO.setUpdateTime(now);
            cakeCardDO.setCreateTime(now);
            EsCakeCard esCakeCard = new EsCakeCard();
            BeanUtil.copyProperties(cakeCardDO,esCakeCard);
            this.esCakeCardMapper.insert(esCakeCard);
            return DubboResult.success(esCakeCard);
        } catch (ArgumentException ae) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional
    public DubboResult<EsCakeImportDTO> importExcel(byte[] base64) {
        try{
            Workbook workbook = null;
            Integer totalNum=0;//总个数
            Integer successNum=0;//成功数
            Integer failNum=0;//失败数
            //兼容excel 03之前和excel 07版本
            try {
                workbook = new XSSFWorkbook(new ByteArrayInputStream(base64));
            } catch (Exception ex) {
                try {
                    workbook = new HSSFWorkbook(new ByteArrayInputStream(base64));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 获取每一个工作薄
            List<FailCakeData> list=new ArrayList<FailCakeData>();
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet xssfSheet = workbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    EsCakeCardDTO cakeCardDTO=new EsCakeCardDTO();
                    Row xssfRow = xssfSheet.getRow(rowNum);
                    totalNum ++;
                    if (xssfRow != null) {
                        FailCakeData failCakeData = new FailCakeData();

                        String name = String.valueOf(xssfRow.getCell(0));
                        if (name !=null && !name.equals("")){
                            cakeCardDTO.setName(name);
                        } else {
                            failCakeData.setName(name);
                            failCakeData.setCode(String.valueOf(xssfRow.getCell(1)));
                            failCakeData.setPassword(String.valueOf(xssfRow.getCell(2)));
                            failNum++;
                            list.add(failCakeData);
                            continue;
                        }

                        //后台更改余额记录
                        String code = String.valueOf(xssfRow.getCell(1));
                        if (code !=null && ! code.equals("")){
                            DubboResult<EsCakeCardDO> result = this.getByCode(code);
                            if (result.isSuccess() && result.getData() == null) {
                                cakeCardDTO.setCode(code);
                            } else {
                                failCakeData.setName(name);
                                failCakeData.setCode(code);
                                failCakeData.setPassword(String.valueOf(xssfRow.getCell(2)));
                                failNum++;
                                list.add(failCakeData);
                                continue;
                            }
                        }else {
                            failCakeData.setName(name);
                            failCakeData.setCode(code);
                            failCakeData.setPassword(String.valueOf(xssfRow.getCell(2)));
                            failNum++;
                            list.add(failCakeData);
                            continue;
                        }

                        //后台更改余额记录
                        String password = String.valueOf(xssfRow.getCell(2));
                        if (password !=null && !password.equals("")){
                            cakeCardDTO.setPassword(password);
                        }else {
                            failCakeData.setName(name);
                            failCakeData.setCode(code);
                            failCakeData.setPassword(password);
                            failNum++;
                            list.add(failCakeData);
                            continue;
                        }
                        this.add(cakeCardDTO);
                        successNum++;
                    }
                }
            }
            EsCakeImportDTO importDTO=new EsCakeImportDTO();
            importDTO.setTotalNum(totalNum);
            importDTO.setSuccessNum(successNum);
            importDTO.setFailNum(failNum);
            importDTO.setFailData(list);
            return DubboResult.success(importDTO);
        } catch (ArgumentException ae) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsCakeCardDO> edit(EsCakeCardDO cakeCardDO, Integer id) {
        try{
            EsCakeCard esCakeCard = this.esCakeCardMapper.selectById(id);
            if(esCakeCard == null){
                throw  new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(cakeCardDO,esCakeCard);
            this.esCakeCardMapper.updateById(esCakeCard);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
