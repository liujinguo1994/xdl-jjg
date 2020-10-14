package com.jjg.member.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


@Data
public class EsTransmitFileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultipartFile file;


}
