package com.hhwy.system.core.register.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SysRegister {

    private Long Id;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String companyName;
    private String jobName;
    private String leaveWord;
    private String creditCode;

    private String remark;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;

}
