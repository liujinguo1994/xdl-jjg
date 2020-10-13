package com.xdl.jjg.plugin.waybill;


import com.xdl.jjg.model.vo.EsConfigItemVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 快递鸟电子面板插件
 */
@Component
public class KDNPlugin implements WayBillEvent {


    @Override
    public List<EsConfigItemVO> definitionConfigItem() {
        List<EsConfigItemVO> list = new ArrayList<>();
        EsConfigItemVO sellerMchidItem = new EsConfigItemVO();
        sellerMchidItem.setName("EBusinessID");
        sellerMchidItem.setText("电商ID");
        sellerMchidItem.setType("text");

        EsConfigItemVO selleAppidItem = new EsConfigItemVO();
        selleAppidItem.setName("AppKey");
        selleAppidItem.setText("密钥");
        selleAppidItem.setType("text");

        EsConfigItemVO sellerKeyItem = new EsConfigItemVO();
        sellerKeyItem.setName("ReqURL");
        sellerKeyItem.setText("请求url");
        sellerKeyItem.setType("text");

        list.add(sellerMchidItem);
        list.add(selleAppidItem);
        list.add(sellerKeyItem);
        return list;
    }

    @Override
    public String getPluginId() {
        return "kdnPlugin";
    }


    @Override
    public String getPluginName() {
        return "快递鸟";
    }

    @Override
    public Integer getOpen() {
        return 0;
    }
}

