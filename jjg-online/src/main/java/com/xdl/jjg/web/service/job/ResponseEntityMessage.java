package com.xdl.jjg.web.service.job;/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2020/5/27 11:10
 */

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ResponseEntity
 * @Description: TODO
 * @Author Administrator
 * @Date 2020/5/27 
 * @Version V1.0
 **/
@Data
public class ResponseEntityMessage implements Serializable {

    private Integer recordsFiltered;
    private Object data;
    private Integer recordsTotal;

}
