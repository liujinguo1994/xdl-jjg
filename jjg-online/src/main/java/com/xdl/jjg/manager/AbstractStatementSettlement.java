package com.xdl.jjg.manager;


import com.jjg.system.model.vo.EsClearingCycleVO;
import com.xdl.jjg.entity.EsBill;
import com.xdl.jjg.mapper.EsBillMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

/**
 * @ClassName: StatementSettlementManager
 * @Description: 结算单
 * @Author: libw  981087977@qq.com
 * @Date: 2019/8/19 16:18
 * @Version: 1.0
 */
public abstract class AbstractStatementSettlement implements StatementSettlementManager{

    @Autowired
    private EsBillMapper billMapper;

    /**
     * 获取汇总总结算单
     *
     * @param clearingCycleVO 结算周期配置类
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 15:25:35
     * @return: com.shopx.trade.dao.entity.EsBill
     */
    protected EsBill getBill(EsClearingCycleVO clearingCycleVO) {
        String[] time = getSettlementTime(clearingCycleVO.getCycleType());
        return billMapper.summary(clearingCycleVO.getType(),time[0], time[1]);
    }

    /**
     * 插入结算单
     * @author: libw 981087977@qq.com
     * @date: 2019/08/27 14:10:21
     * @param bill  结算单信息
     * @return: java.lang.Long  结算单id
     */
    protected Long saveBill(EsBill bill){
        billMapper.insert(bill);
        return bill.getId();
    }

    /**
     * 获取结算周期的开始时间和结束时间
     *
     * @param type 结算周期（1：月结 2：季结 3：半年结）
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 09:25:04
     * @return: java.lang.String[]  下标0为开始时间，下标1为结束时间
     */
    protected String[] getSettlementTime(Integer type) {
        // 两个时间，下标0为开始时间，下标1为结束时间
        String[] time = new String[2];

        switch (type) {
            case 1:
                // TODO 测试需要改成当月，测试后要改回-1（前一个月）
                time[0] = getStartTime(0);
                time[1] = getEndTime(0);
                break;
            case 2:
                time[0] = getStartTime(-3);
                time[1] = getEndTime(-3);
                break;
            case 3:
                time[0] = getStartTime(-6);
                time[1] = getEndTime(-6);
                break;
            default:
                break;
        }
        return time;
    }

    /**
     * 获取每月开始时间
     *
     * @param addMonth 添加的月数
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 10:28:19
     * @return: java.lang.String
     */
    private String getStartTime(int addMonth) {
        Calendar calendar = Calendar.getInstance();
        // 月份计算
        calendar.add(Calendar.MONTH, addMonth);
        // 获取月份第一天
        int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), minDay, 0, 0, 0);

        // 替换毫秒级
        String endTime = String.valueOf(calendar.getTimeInMillis());

        return endTime.substring(0, endTime.length() - 3) + "000";
    }

    /**
     * 获取每月结束时间
     *
     * @param addMonth 添加的月数
     * @author: libw 981087977@qq.com
     * @date: 2019/08/23 10:28:19
     * @return: java.lang.String
     */
    private String getEndTime(int addMonth) {
        Calendar calendar = Calendar.getInstance();
        // 月份计算
        calendar.add(Calendar.MONTH, addMonth);
        // 获取月份第一天
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), maxDay, 23, 59, 59);
        String startTime = String.valueOf(calendar.getTimeInMillis());

        return startTime.substring(0, startTime.length() - 3) + "999";
    }
}