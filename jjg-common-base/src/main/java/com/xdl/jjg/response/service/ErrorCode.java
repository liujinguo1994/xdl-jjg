package com.xdl.jjg.response.service;

import lombok.Data;
import lombok.ToString;


/**
 * 错误code
 * @author liu
 */
@ToString
@Data
public class ErrorCode extends BaseErrorCode {

//    public static final ErrorCode SYS_ERROR = new ErrorCode(3000,"系统异常！");
//    public static final ErrorCode PARAM_ERROR = new ErrorCode(3100,"参数异常！");
//    public static final ErrorCode USER_REGISTER_ERROR = new ErrorCode(3100,"用户信息注册失败！");
//    public static final ErrorCode USER_UPDATE_ERROR = new ErrorCode(3100,"用户信息修改！");
//    public static final ErrorCode USER_NOT_EXIST = new ErrorCode(3100,"用户信息不存在！");
//
//    public static final ErrorCode DX_CODE_EXPIRE = new ErrorCode(3100,"短信验证码已失效!");
//    public static final ErrorCode DX_CODE_INCORRECT = new ErrorCode(3100,"短信验证码输入错误!");




    public ErrorCode(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
