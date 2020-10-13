package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 邮件发送dto
 */
@Data
public class EsSendEmailDTO implements Serializable {

    private static final long serialVersionUID = 5817183105391564L;


    /**
     * 邮件标题(必填)
     */
    private String title;

    /**
     * 邮件接收者(必填)
     */
    private String email;

    /**
     * 邮件内容(必填)
     */
    private String content;


    //是否发送成功(0:失败，1:成功)
    private Integer success = 1;


}