/**
 * 2018/1/3
 */
package com.xdl.jjg.util.security;

import java.security.Security;

/**
 * 加密基类
 */
public abstract class SecurityCoder {
    private static Byte ADDFLAG = 0;

    static {
        if (ADDFLAG == 0) {
            // 加入BouncyCastleProvider支持
            Security.addProvider(new BouncyCastleProvider());
            ADDFLAG = 1;
        }
    }
}
