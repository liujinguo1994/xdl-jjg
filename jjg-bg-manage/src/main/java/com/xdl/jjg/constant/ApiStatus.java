package com.xdl.jjg.constant;


import com.xdl.jjg.response.exception.UnCheckedWebException;
import com.xdl.jjg.response.service.BaseResultSupport;
import com.xdl.jjg.response.web.BaseApiStatus;

/**
 * 扩展性业务API CODE
 */
public class ApiStatus extends BaseApiStatus {


    /**
     * 用户锁定
     */
    public static final int AC_LOCKD_ACCOUNT = 20001;

    public static UnCheckedWebException wrapperException(BaseResultSupport resultSupport) {
        int code = resultSupport.getCode();
        String msg = resultSupport.getMsg();
        return new UnCheckedWebException(code, msg);
    }


    public static UnCheckedWebException wrapperException(RuntimeException ex) {
        return new UnCheckedWebException(SYST_SYSTEM_ERROR, FAIL_MESSAGE, ex);
    }


}