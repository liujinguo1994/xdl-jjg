package com.xdl.jjg.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年10月2019/10/17日
 * @Description TODO
 */
@Data
public class TupleVO implements Serializable {

    private String value;
    private Long score;

}
