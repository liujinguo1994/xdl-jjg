package com.xdl.jjg.response.service;

public enum ErrorCodeEnum {
    RESPONSE_SUCCESS(0000, "请求成功"), REQUEST_REPEAT(0001, "重复请求"), RES_TRANSFER_FAILED(1004, "回应转账异常"), RES_SYS_ERROR(1007, "回应系统异常"), RES_PARAM_ERROR(1012, "回应参数异常"), RES_HASH_FAILED(1014, "交易Hash失败"), RES_HASH_PENDING(1013, "交易Hash等待中"), RES_BALANCE_INSUFFICIENT(1008, "gas费不足"), SYS_ERROR(3000, "系统异常！"), PARAM_ERROR(3999, "参数异常！")
    /**
     * 移动端 start
     */
    , USER_REGISTER_ERROR(3100, "用户信息注册失败！"), USER_NOT_EXIST(3101, "用户信息不存在！"), USER_TOKEN_EXPIRE(3102, "用户token已失效 请重新登录！"), DX_CODE_EXPIRE(3103, "短信验证码已失效!"), DX_CODE_INCORRECT(3104, "短信验证码输入错误!"), DX_CODE_SEND_ERROR(3105, "短信验证码发送失败!"), CODE_NOT_EXPIRE(3106, "验证码尚未过期 请勿重复发送验证码!"), INPUT_PHONE(3107, "请输入手机号码!"), PHONE_ERROR(3108, "手机号码格式错误!"), PHONE_EXIT(3109, "该手机号已注册!"), INPUT_PHONE_ERROR(3110, "请输入正确的用户名密码!"), SEARCH_ERROR(3111, "未查询到相关信息!")
    /**
     * 移动端 end
     */
    /**
     * 管理后台 start
     */
    , CAROUSEL_TOPPING_ERROR(3500, "轮播图置顶失败!请先取消置顶"), TOPPING_ERROR(3501, "请检查操作的数据是否合规！"), EXIT_DATE_ERROR(3502, "该类型已存在，请更改类型名称"), ADD_REPEAT_ERROR(3503, "请勿重复添加")
    /**
     * 管理后台 end
     */

    , BOTTOM_LAYER_NOT_EXIST(2001, "底层不存在"), FAIT_TYPE_NOT_EXIST(2002, "法币类型不存在"), COIN_NOT_EXIST(2100, "币种不存在"), ADD_COIN_FAILED(2101, "币种添加失败"), UPT_COIN_FAILED(2102, "更新币种失败"), DEL_COIN_FAILED(2103, "删除币种失败"), QUOTES_NOT_EXIST(2110, "行情不存在"), ADD_QUOTES_FAILED(2111, "行情添加失败"), UPT_QUOTES_FAILED(2112, "更新行情失败"), DEL_QUOTES_FAILED(2113, "删除行情失败"), FLASH_CONVERT_NOT_EXIST(2120, "闪兑对不存在"), ADD_FLASH_CONVERT_FAILED(2121, "闪兑对添加失败"), UPT_FLASH_CONVERT_FAILED(2122, "更新闪兑对失败"), DEL_FLASH_CONVERT_FAILED(2123, "删除闪兑对失败"), MQ_SEND_FAILED(2201, "MQ发送消息失败"), TRANSFER_REQUEST_FAILED(2202, "转账发送请求失败"), RESPONSE_FAILED(2301, "网络请求失败"), TRANSFER_TYPE_ERROR(2400, "转账类型不正确"), PRIVATE_KEY_LENGTH_ERROR(2401, "私钥长度不正确"), BALANCE_NOT_ENOUGH(2500, "钱包余额不足");
    int errorCode;
    String errorMassage;

    ErrorCodeEnum(int errorCode, String errorMassage) {
        this.errorCode = errorCode;
        this.errorMassage = errorMassage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMassage() {
        return errorMassage;
    }

    public void setErrorMassage(String errorMassage) {
        this.errorMassage = errorMassage;
    }

    public static ErrorCodeEnum valueOf(int errorCode) {
        for (ErrorCodeEnum errorCodeEnum :
                values()) {
            if (errorCode == errorCodeEnum.errorCode) {
                return errorCodeEnum;
            }
        }
        return null;
    }
}
