package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsShopDetailDO;
import com.jjg.system.model.vo.EsClearingCycleVO;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsBill;
import com.xdl.jjg.entity.EsExportOrder;
import com.xdl.jjg.entity.EsShopBill;
import com.xdl.jjg.entity.EsSupplierBill;
import com.xdl.jjg.manager.AbstractStatementSettlement;
import com.xdl.jjg.mapper.EsBillDetailMapper;
import com.xdl.jjg.mapper.EsBillMapper;
import com.xdl.jjg.mapper.EsSupplierBillMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.ExcelUtil;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.web.service.feign.member.ShopDetailService;
import com.xdl.jjg.web.service.feign.shop.ShopService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SupplierSettlementImpl
 * @Description: 供应商结算
 * @Author: bamboo  asp.bamboo@gmail.com
 * @Date: 2019/8/19 16:44
 * @Version: 1.0
 */
@Component
public class SupplierSettlementImpl extends AbstractStatementSettlement {

    @Autowired
    private EsBillMapper billMapper;

    @Autowired
    private EsSupplierBillMapper supplierBillMapper;

    @Autowired
    private EsBillDetailMapper billDetailMapper;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopDetailService shopDetailService;

    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0,0);

    /**
     * 订单结算
     *
     * @param clearingCycleVO 结算周期配置类
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 15:20:55
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void settlement(EsClearingCycleVO clearingCycleVO) {
        // 获取结算单数据
        EsBill bill = this.getBill(clearingCycleVO);

        if (bill == null) {
            throw new ArgumentException(TradeErrorCode.NO_SETTLEABLE_DATA.getErrorCode(),
                    TradeErrorCode.NO_SETTLEABLE_DATA.getErrorMsg());
        }

        // 所有汇总的id
        List<String> idList = bill.getIdList();

        // 以店铺分组汇总账单
        List<EsSupplierBill> list = this.supplierBillMapper.summary(idList);

        // 设置总结算单佣金和退还佣金
        String billSn = String.valueOf(snowflakeIdWorker.nextId());
        bill.setBillSn(billSn);
        // 插入总结算单
        this.saveBill(bill);

        // 插入账单信息, 更新结算单明细表
        for (EsSupplierBill supplierBill : list) {
            supplierBill.setBillSn(billSn);
            supplierBill.setState(1);
            supplierBillMapper.insert(supplierBill);

            // 更新明细表
            billDetailMapper.updateStatusForShop(supplierBill.getId(), supplierBill.getIdList());
        }
    }

    /**
     * 根据账单编号查询结算单
     *
     * @param billDetailDTO 结算账单参数
     * @param pageSize      行数
     * @param pageNum       页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/30 14:08:07
     * @return: java.util.List<com.shopx.trade.api.model.domain.EsBillDetailDO>
     */
    @Override
    public EsSettlement getBillDetail(EsBillDetailDTO billDetailDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSupplierBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotEmpty(billDetailDTO.getKeyword()), EsSupplierBill::getShopName,
                billDetailDTO.getKeyword());
        queryWrapper.lambda().like(StringUtils.isNotEmpty(billDetailDTO.getKeyword()), EsSupplierBill::getId,
                billDetailDTO.getKeyword());
        queryWrapper.lambda().eq(billDetailDTO.getState() != null, EsSupplierBill::getState, billDetailDTO.getState());
        queryWrapper.lambda().eq(EsSupplierBill::getBillSn, billDetailDTO.getBillSn());

        Page<EsSupplierBill> page = new Page<>(pageSize, pageNum);
        IPage<EsSupplierBill> iPage = supplierBillMapper.selectPage(page, queryWrapper);

        List<EsBillDetailDO> list = BeanUtil.copyList(iPage.getRecords(), EsBillDetailDO.class);

        EsSettlement settlement = new EsSettlement();
        settlement.setBillDetailList(list);
        settlement.setTotal(iPage.getTotal());
        return settlement;
    }

    /**
     * 获取结算单详情
     *
     * @param settlementId 结算单id
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:21:41
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public EsHeaderDO getSettlementInfo(Long settlementId) {
        EsSupplierBill supplierBill = this.supplierBillMapper.selectById(settlementId);

        // 获取店铺信息
        DubboResult result = shopDetailService.getByShopId(supplierBill.getShopId());
        if (!result.isSuccess()) {
            throw new ArgumentException(result.getCode(), result.getMsg());
        }
        EsShopDetailDO shopDetail = (EsShopDetailDO)result.getData();

        EsHeaderDO headerDO = new EsHeaderDO();
        // 组织名称
        headerDO.setOrgName(supplierBill.getShopName());
        // 银行开户名
        headerDO.setBankAccountName(shopDetail.getBankAccountName());
        // 银行开户账号
        headerDO.setBankNumber(shopDetail.getBankNumber());
        // 开户银行支行名称
        headerDO.setBankName(shopDetail.getBankName());
        // 开户银行所在省名称
        headerDO.setBankProvince(shopDetail.getBankProvince());
        // 开户银行所在市名称
        headerDO.setBankCity(shopDetail.getBankCity());
        // 开户银行所在县名称
        headerDO.setBankCounty(shopDetail.getBankCounty());
        // 开户银行所在镇民名称
        headerDO.setBankTown(shopDetail.getBankTown());
        // 结算总金额
        headerDO.setPrice(supplierBill.getPrice());
        // 结算金额
        headerDO.setBillMoney(supplierBill.getBillMoney());
        return headerDO;
    }

    /**
     * 获取结算单总订单明细
     *
     * @param settlementId 结算单id
     * @param pageSize      行数
     * @param pageNum       页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: settlement
     */
    @Override
    public EsSettlement getSettlementOrderDetail(Long settlementId, int pageSize, int pageNum) {
        EsSettlement settlement = new EsSettlement();
        Page<EsShopBill> page = new Page<>(pageSize, pageNum);
        IPage<EsSettlementDetailDO> pageList = supplierBillMapper.getSettlementDetail(page, settlementId);
        List<EsSettlementDetailDO> list = BeanUtil.copyList(pageList.getRecords(), EsSettlementDetailDO.class);

        settlement.setTotal(page.getTotal());
        settlement.setSettlementDetailList(list);
        return settlement;
    }

    /**
     * 获取结算单退款订单金额明细
     *
     * @param settlementId 结算单id
     * @param pageSize      行数
     * @param pageNum       页码
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:25:19
     * @return: settlement
     */
    @Override
    public EsSettlement getSettlementRefundOrderDetail(Long settlementId, int pageSize, int pageNum) {
        EsSettlement settlement = new EsSettlement();
        Page<EsShopBill> page = new Page<>(pageSize, pageNum);
        IPage<EsSettlementDetailDO> pageList = supplierBillMapper.getSettlementRefundDetail(page, settlementId);
        List<EsSettlementDetailDO> list = BeanUtil.copyList(pageList.getRecords(), EsSettlementDetailDO.class);

        settlement.setTotal(page.getTotal());
        settlement.setSettlementDetailList(list);
        return settlement;
    }

    /**
     * 导出EXCEL
     *
     * @param settlementId 结算单id
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public ExcelDO exportExcel(Long settlementId) {
        ExcelDO excel = new ExcelDO();
        EsSupplierBill supplierBill = this.supplierBillMapper.selectById(settlementId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        // Excel名称
        String name = supplierBill.getShopName() + formatter.format(new Date(supplierBill.getStartTime())) + " - "
                + formatter.format(new Date(supplierBill.getEndTime()));

        final String sheet1Title = "订单汇总";
        final String sheet2Title = "退款单汇总";

        // 创建Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 获得标题样式
        HSSFCellStyle titleStyle = ExcelUtil.getCellStyle(workbook, "宋体", (short) 14, HorizontalAlignment.CENTER.getCode());
        // 获得列样式
        HSSFCellStyle cellStyle = ExcelUtil.getCellStyle(workbook, "宋体", (short) 11, HorizontalAlignment.CENTER.getCode() );

        // 生成第一个Sheet页
        createSheetOne(workbook, titleStyle, cellStyle, sheet1Title, settlementId);
        // 生成第二个Sheet页
        createSheetTwo(workbook, titleStyle, cellStyle, sheet2Title, settlementId);

        excel.setWorkbook(workbook);
        return excel;
    }

    /**
     * 更改结算状态
     *
     * @param settlementId  结算单id
     * @param status        结算状态
     * @author: libw 981087977@qq.com
     * @date: 2019/08/31 10:27:58
     * @return: com.shopx.common.model.result.DubboResult
     */
    @Override
    public void updateStatus(Long settlementId, int status){
        EsSupplierBill supplierBill = new EsSupplierBill();
        supplierBill.setId(settlementId);
        supplierBill.setState(status);
        this.supplierBillMapper.updateById(supplierBill);
    }

    /**
     * Excel第一个Sheet页
     * @author: libw 981087977@qq.com
     * @date: 2019/09/02 13:51:24
     * @param workbook
     * @param titleStyle
     * @param cellStyle
     * @param sheetTitle
     * @return: void
     */
    private void createSheetOne(HSSFWorkbook workbook, HSSFCellStyle titleStyle, HSSFCellStyle cellStyle,
                                String sheetTitle, Long settlementId) {
        // 起始行
        int row = 0;

        // 1、 生成第一个sheet页(商品汇总)设置标题
        HSSFSheet hssfSheet = ExcelUtil.setTitleCell(workbook, sheetTitle, row, titleStyle, 4);

        // 1.1、 设置日期
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        HSSFRow hssfRow = hssfSheet.createRow(row + 1);
        ExcelUtil.setCell(hssfRow, 0, "制表日期: " + date, cellStyle, 4);

        // 1.2、 将表头合并
        hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        hssfSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

        // 1.3、 设置列名
        // 列名
        String[] cellName = {"序号", "订单编号", "商品名称", "数量", "商品单价", "优惠金额"};
        ExcelUtil.setCellName(hssfSheet, row + 2, cellName, cellStyle);
        row += 2;
        // 合并单元格的起始行
        int firstRow = row;
        // 序号
        Integer num = 1;
        // 1.4、 查询商品汇总数据
        List<EsExportOrder> list = supplierBillMapper.getExportOrder(settlementId);
        // 订单数
        int count = list.size();
        for (int i = 0; i < count; i++) {
            hssfRow = hssfSheet.createRow(row + i);
            EsExportOrder info = list.get(i);

            // 订单编号
            String orderSn = info.getOrderSn();
            // 查询下一个订单编号
            String lastOrderSn = "";
            if (i == count - 1) {
                lastOrderSn = orderSn;
            } else {
                lastOrderSn = list.get(i + 1).getOrderSn();
            }
            // 序号
            ExcelUtil.setCell(hssfRow, 0, String.valueOf(num), cellStyle, 0);
            // 订单编号
            ExcelUtil.setCell(hssfRow, 1, info.getOrderSn(), cellStyle, 0);
            // 商品名称
            ExcelUtil.setCell(hssfRow, 2, info.getGoodsName(), cellStyle, 0);
            // 数量
            ExcelUtil.setCell(hssfRow, 3, String.valueOf(info.getGoodsNum()) , cellStyle, 0);
            // 商品单价
            ExcelUtil.setCell(hssfRow, 4, info.getGoodsName(), cellStyle, 0);
            // 订单金额
            ExcelUtil.setCell(hssfRow, 5, info.getGoodsName(), cellStyle, 0);
            // 优惠金额
            ExcelUtil.setCell(hssfRow, 6, String.valueOf(info.getDiscountMoney()), cellStyle, 0);

            // 当前ID与下一个ID不同
            if (!StringUtils.equals(orderSn, lastOrderSn)) {
                // 合并单元格
                ExcelUtil.cellMerged(workbook, hssfSheet, firstRow, row + i, new int[]{0, 1, 2, 3, 7, 9}, 0);

                // 重新赋值开始行
                firstRow = row + i + 1;
                num++;
            }

        }

        // 1.5、 自适应单元格
        ExcelUtil.cellSelfAdaption(hssfSheet, 6);

    }

    /**
     * Excel第二个Sheet页
     * @author: libw 981087977@qq.com
     * @date: 2019/09/02 13:44:23
     * @param workbook      excel
     * @param titleStyle    标题样式
     * @param cellStyle     列单元样式
     * @param sheetTitle    sheet页名称
     * @return: void
     */
    private void createSheetTwo(HSSFWorkbook workbook, HSSFCellStyle titleStyle, HSSFCellStyle cellStyle,
                                String sheetTitle, Long settlementId) {
        // 起始行
        int row = 0;

        // 1、 生成第一个sheet页(商品汇总)设置标题
        HSSFSheet hssfSheet = ExcelUtil.setTitleCell(workbook, sheetTitle, row, titleStyle, 4);

        // 1.1、 设置日期
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        HSSFRow hssfRow = hssfSheet.createRow(row + 1);
        ExcelUtil.setCell(hssfRow, 0, "制表日期: " + date, cellStyle, 4);

        // 1.2、 将表头合并
        hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        hssfSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

        // 1.3、 设置列名
        // 列名
        String[] cellName = {"序号", "订单编号", "售后编号", "商品名称", "数量", "商品单价", "优惠金额"};
        ExcelUtil.setCellName(hssfSheet, row + 2, cellName, cellStyle);
        row += 2;
        // 合并单元格的起始行
        int firstRow = row;
        // 序号
        Integer num = 1;
        // 1.4、 查询商品汇总数据
        List<EsExportOrder> list = supplierBillMapper.getExportRefundOrder(settlementId);
        // 订单数
        int count = list.size();
        for (int i = 0; i < count; i++) {
            hssfRow = hssfSheet.createRow(row + i);
            EsExportOrder info = list.get(i);

            // 订单编号
            String orderSn = info.getOrderSn();
            // 查询下一个订单编号
            String lastOrderSn = "";
            if (i == count - 1) {
                lastOrderSn = orderSn;
            } else {
                lastOrderSn = list.get(i + 1).getOrderSn();
            }
            // 序号
            ExcelUtil.setCell(hssfRow, 0, String.valueOf(num), cellStyle, 0);
            // 订单编号
            ExcelUtil.setCell(hssfRow, 1, info.getOrderSn(), cellStyle, 0);
            // 售后编号
            ExcelUtil.setCell(hssfRow, 2, info.getRefundSn(), cellStyle, 0);
            // 商品名称
            ExcelUtil.setCell(hssfRow, 3, info.getGoodsName(), cellStyle, 0);
            // 数量
            ExcelUtil.setCell(hssfRow, 4, String.valueOf(info.getGoodsNum()) , cellStyle, 0);
            // 商品单价
            ExcelUtil.setCell(hssfRow, 5, info.getGoodsName(), cellStyle, 0);
            // 订单金额
            ExcelUtil.setCell(hssfRow, 6, info.getGoodsName(), cellStyle, 0);
            // 优惠金额
            ExcelUtil.setCell(hssfRow, 7, String.valueOf(info.getDiscountMoney()), cellStyle, 0);

            // 当前ID与下一个ID不同
            if (!StringUtils.equals(orderSn, lastOrderSn)) {
                // 合并单元格
                ExcelUtil.cellMerged(workbook, hssfSheet, firstRow, row + i, new int[]{0, 1, 2, 3, 7, 9}, 0);

                // 重新赋值开始行
                firstRow = row + i + 1;
                num++;
            }

        }

        // 1.5、 自适应单元格
        ExcelUtil.cellSelfAdaption(hssfSheet, 6);
    }

    @Override
    protected EsBill getBill(EsClearingCycleVO clearingCycleVO){
        String[] time = getSettlementTime(clearingCycleVO.getCycleType());
        return billMapper.summaryForSupplier(clearingCycleVO.getType(),time[0], time[1]);
    }
}
